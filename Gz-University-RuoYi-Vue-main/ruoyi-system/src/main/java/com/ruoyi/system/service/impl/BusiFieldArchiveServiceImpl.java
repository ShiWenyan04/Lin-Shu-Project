package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.system.mapper.BusiFieldArchiveMapper;
import com.ruoyi.system.domain.BusiFieldArchive;
import com.ruoyi.system.service.IBusiFieldArchiveService;

// ⬇️ 引入关联的 Mapper 和 Entity
import com.ruoyi.system.mapper.BusiFieldEndMapper;
import com.ruoyi.system.domain.BusiFieldEnd;
import com.ruoyi.system.mapper.BusiFieldStartMapper;
import com.ruoyi.system.domain.BusiFieldStart;
import com.ruoyi.system.mapper.BusiProjectMapper;
import com.ruoyi.system.domain.BusiProject;

@Service
public class BusiFieldArchiveServiceImpl implements IBusiFieldArchiveService
{
    @Autowired
    private BusiFieldArchiveMapper busiFieldArchiveMapper;

    @Autowired
    private BusiFieldEndMapper busiFieldEndMapper; // 用来查已结束的任务

    @Autowired
    private BusiFieldStartMapper busiFieldStartMapper; // 用来查任务详情

    @Autowired
    private BusiProjectMapper busiProjectMapper; // 用来查项目名

    @Override
    public BusiFieldArchive selectBusiFieldArchiveByArchiveId(Long archiveId) {
        return busiFieldArchiveMapper.selectBusiFieldArchiveByArchiveId(archiveId);
    }

    @Override
    public List<BusiFieldArchive> selectBusiFieldArchiveList(BusiFieldArchive busiFieldArchive) {
        return selectBusiFieldArchiveList(busiFieldArchive, null, null);
    }

    /**
     * 核心：列表查询 (带 Tab 过滤)
     */
    @Override
    public List<BusiFieldArchive> selectBusiFieldArchiveList(BusiFieldArchive busiFieldArchive, String tabType, Long userId) {
        if ("ALL".equals(tabType)) {
            busiFieldArchive.setAuditStatus("1");
        } else if ("AUDIT".equals(tabType)) {
            busiFieldArchive.setAuditStatus("0");
        } else if ("MINE".equals(tabType) && userId != null) {
            busiFieldArchive.getParams().put("dataScope", " AND create_by = '" + userId + "'");
        }
        return busiFieldArchiveMapper.selectBusiFieldArchiveList(busiFieldArchive);
    }

    /**
     * 🚀 核心：查询“我已完成”且“未归档”的任务
     * 逻辑：
     * 1. 查 End 表：createBy = 我 AND auditStatus = 1 (已通过)
     * 2. 查 Archive 表：获取所有已归档的 startId
     * 3. 过滤：排除掉已经在 Archive 表里的 startId
     */
    @Override
    public List<Object> selectMyFinishedStarts(Long userId) {
        // 1. 查我所有已通过的结束填报
        BusiFieldEnd endQuery = new BusiFieldEnd();
        endQuery.setCreateBy(String.valueOf(userId)); // 必须是我创建的
        // 这里假设 EndMapper.xml 里没有写 createBy 的 where 条件(类似上次Start的坑)，
        // 建议你用 dataScope 强行注入，或者确信 XML 里有 createBy
        endQuery.getParams().put("dataScope", " AND create_by = '" + userId + "'");
        endQuery.setAuditStatus("1"); // 必须是已通过的

        List<BusiFieldEnd> finishedEnds = busiFieldEndMapper.selectBusiFieldEndList(endQuery);

        if (finishedEnds.isEmpty()) return new ArrayList<>();

        // 2. 查我已经归档了哪些任务，避免重复归档
        BusiFieldArchive archiveQuery = new BusiFieldArchive();
        archiveQuery.getParams().put("dataScope", " AND create_by = '" + userId + "'");
        List<BusiFieldArchive> myArchives = busiFieldArchiveMapper.selectBusiFieldArchiveList(archiveQuery);
        List<Long> archivedStartIds = myArchives.stream().map(BusiFieldArchive::getStartId).collect(Collectors.toList());

        // 3. 组装结果
        List<Object> options = new ArrayList<>();
        for (BusiFieldEnd end : finishedEnds) {
            // 如果这个 StartId 已经归档过了，跳过
            if (archivedStartIds.contains(end.getStartId())) {
                continue;
            }

            // 查 Start 详情
            BusiFieldStart start = busiFieldStartMapper.selectBusiFieldStartByStartId(end.getStartId());
            if (start == null) continue;

            // 查项目名
            String pname = "未关联项目";
            if (start.getProjectId() != null) {
                BusiProject p = busiProjectMapper.selectBusiProjectByProjectId(start.getProjectId());
                if (p != null) pname = p.getProjectName();
            }

            Map<String, Object> map = new HashMap<>();
            map.put("startId", start.getStartId());
            map.put("projectName", pname);
            map.put("destination", start.getDestination());
            map.put("leaderName", start.getLeaderName());
            options.add(map);
        }
        return options;
    }

    /**
     * 新增
     */
    @Override
    public int insertBusiFieldArchive(BusiFieldArchive archive) {
        archive.setAuditStatus("0");
        archive.setCreateTime(DateUtils.getNowDate());

        // 🚀 自动补全冗余字段：保存时把项目名、地点存入归档表
        if (archive.getStartId() != null) {
            BusiFieldStart start = busiFieldStartMapper.selectBusiFieldStartByStartId(archive.getStartId());
            if (start != null) {
                archive.setLeaderName(start.getLeaderName());
                archive.setLocation(start.getDestination());

                if (start.getProjectId() != null) {
                    BusiProject p = busiProjectMapper.selectBusiProjectByProjectId(start.getProjectId());
                    if (p != null) archive.setProjectName(p.getProjectName());
                }
            }
        }

        return busiFieldArchiveMapper.insertBusiFieldArchive(archive);
    }

    /**
     * 修改外业数据归档
     */
    @Override
    public int updateBusiFieldArchive(BusiFieldArchive archive) {
        // 【核心修改】：如果是 负责人、老师、管理员，修改时不重置状态
        boolean isManager = SecurityUtils.hasRole("fieldAchive_manager")
                || SecurityUtils.hasRole("teacher")
                || SecurityUtils.hasRole("admin");

        if (!isManager) {
            // 只有普通学生修改“驳回”的数据时，才重置为“待审核”
            BusiFieldArchive old = busiFieldArchiveMapper.selectBusiFieldArchiveByArchiveId(archive.getArchiveId());
            if (old != null && "2".equals(old.getAuditStatus())) {
                archive.setAuditStatus("0");
                archive.setAuditReason("");
            }
        }

        archive.setUpdateTime(DateUtils.getNowDate());
        return busiFieldArchiveMapper.updateBusiFieldArchive(archive);
    }

    @Override
    public int auditBusiFieldArchive(Long archiveId, String status, String reason) {
        BusiFieldArchive update = new BusiFieldArchive();
        update.setArchiveId(archiveId);
        update.setAuditStatus(status);
        update.setAuditReason(reason);
        update.setUpdateTime(DateUtils.getNowDate());
        return busiFieldArchiveMapper.updateBusiFieldArchive(update);
    }

    @Override
    public int deleteBusiFieldArchiveByArchiveIds(Long[] archiveIds) {
        return busiFieldArchiveMapper.deleteBusiFieldArchiveByArchiveIds(archiveIds);
    }

    @Override
    public int deleteBusiFieldArchiveByArchiveId(Long archiveId) {
        return busiFieldArchiveMapper.deleteBusiFieldArchiveByArchiveId(archiveId);
    }
}