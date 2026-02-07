package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.system.mapper.BusiFieldEndMapper;
import com.ruoyi.system.domain.BusiFieldEnd;
import com.ruoyi.system.service.IBusiFieldEndService;

import com.ruoyi.system.mapper.BusiFieldStartMapper;
import com.ruoyi.system.domain.BusiFieldStart;
import com.ruoyi.system.mapper.BusiProjectMapper;
import com.ruoyi.system.domain.BusiProject;

/**
 * 外业行程结束Service业务层处理
 * * @author ruoyi
 * @date 2025-12-22
 */
@Service
public class BusiFieldEndServiceImpl implements IBusiFieldEndService
{
    @Autowired
    private BusiFieldEndMapper busiFieldEndMapper;

    @Autowired
    private BusiFieldStartMapper busiFieldStartMapper;

    @Autowired
    private BusiProjectMapper busiProjectMapper;

    @Override
    public BusiFieldEnd selectBusiFieldEndByEndId(Long endId)
    {
        BusiFieldEnd end = busiFieldEndMapper.selectBusiFieldEndByEndId(endId);
        fillProjectInfo(end);
        return end;
    }

    @Override
    public List<BusiFieldEnd> selectBusiFieldEndList(BusiFieldEnd busiFieldEnd)
    {
        return selectBusiFieldEndList(busiFieldEnd, null, null);
    }

    /**
     * 核心：列表查询
     */
    @Override
    public List<BusiFieldEnd> selectBusiFieldEndList(BusiFieldEnd busiFieldEnd, String tabType, Long userId)
    {
        // 1. 权限过滤
        if ("ALL".equals(tabType)) {
            busiFieldEnd.setAuditStatus("1");
        }
        else if ("AUDIT".equals(tabType)) {
            busiFieldEnd.setAuditStatus("0");
        }
        else if ("MINE".equals(tabType) && userId != null) {
            busiFieldEnd.getParams().put("dataScope", " AND create_by = '" + userId + "'");
        }

        // 2. 执行查询
        List<BusiFieldEnd> list = busiFieldEndMapper.selectBusiFieldEndList(busiFieldEnd);

        // 3. 关联信息填充
        for (BusiFieldEnd item : list) {
            fillProjectInfo(item);
        }
        return list;
    }

    /**
     * 🚀 核心修复：下拉框查询
     * 必须是：createBy = 当前用户 AND auditStatus = 1
     */
    @Override
    public List<Object> selectMyAvailableStarts(Long userId) {
        BusiFieldStart query = new BusiFieldStart();
        // 强制设置创建人，确保小明只能看见小明的数据
        query.setCreateBy(String.valueOf(userId));
        query.setAuditStatus("1");

        List<BusiFieldStart> starts = busiFieldStartMapper.selectBusiFieldStartList(query);

        List<Object> options = new ArrayList<>();
        for (BusiFieldStart s : starts) {
            String pname = "未关联项目";
            if (s.getProjectId() != null) {
                BusiProject p = busiProjectMapper.selectBusiProjectByProjectId(s.getProjectId());
                if(p != null) pname = p.getProjectName();
            }

            Map<String, Object> map = new HashMap<>();
            map.put("startId", s.getStartId());
            map.put("projectName", pname);
            map.put("destination", s.getDestination() == null ? "" : s.getDestination());
            map.put("leaderName", s.getLeaderName());

            options.add(map);
        }
        return options;
    }

    @Override
    public int insertBusiFieldEnd(BusiFieldEnd busiFieldEnd)
    {
        busiFieldEnd.setAuditStatus("0");
        busiFieldEnd.setCreateTime(DateUtils.getNowDate());
        return busiFieldEndMapper.insertBusiFieldEnd(busiFieldEnd);
    }

    /**
     * 修改外业行程结束
     */
    @Override
    public int updateBusiFieldEnd(BusiFieldEnd busiFieldEnd)
    {
        // 【核心修改】：如果是 负责人、老师、管理员，修改时不重置状态
        boolean isManager = com.ruoyi.common.utils.SecurityUtils.hasRole("fieldend_manager")
                || com.ruoyi.common.utils.SecurityUtils.hasRole("teacher")
                || com.ruoyi.common.utils.SecurityUtils.hasRole("admin");

        if (!isManager) {
            // 只有普通学生修改“驳回”的数据时，才重置为“待审核”
            BusiFieldEnd oldData = busiFieldEndMapper.selectBusiFieldEndByEndId(busiFieldEnd.getEndId());
            if (oldData != null && "2".equals(oldData.getAuditStatus())) {
                busiFieldEnd.setAuditStatus("0");
                busiFieldEnd.setAuditReason("");
            }
        }

        busiFieldEnd.setUpdateTime(DateUtils.getNowDate());
        return busiFieldEndMapper.updateBusiFieldEnd(busiFieldEnd);
    }

    @Override
    public int auditBusiFieldEnd(Long endId, String status, String reason) {
        BusiFieldEnd update = new BusiFieldEnd();
        update.setEndId(endId);
        update.setAuditStatus(status);
        update.setAuditReason(reason);
        update.setUpdateTime(DateUtils.getNowDate());
        return busiFieldEndMapper.updateBusiFieldEnd(update);
    }

    @Override
    public int deleteBusiFieldEndByEndIds(Long[] endIds)
    {
        return busiFieldEndMapper.deleteBusiFieldEndByEndIds(endIds);
    }

    @Override
    public int deleteBusiFieldEndByEndId(Long endId)
    {
        return busiFieldEndMapper.deleteBusiFieldEndByEndId(endId);
    }

    private void fillProjectInfo(BusiFieldEnd item) {
        if (item == null || item.getStartId() == null) return;
        BusiFieldStart start = busiFieldStartMapper.selectBusiFieldStartByStartId(item.getStartId());
        if (start != null && start.getProjectId() != null) {
            BusiProject p = busiProjectMapper.selectBusiProjectByProjectId(start.getProjectId());
            if (p != null) {
                item.setProjectName(p.getProjectName());
            }
        }
    }
}