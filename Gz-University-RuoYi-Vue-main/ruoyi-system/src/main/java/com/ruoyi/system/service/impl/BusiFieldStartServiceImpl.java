package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.mapper.BusiProjectMapper;
import com.ruoyi.system.domain.BusiProject;
import com.ruoyi.system.mapper.BusiFieldStartMapper;
import com.ruoyi.system.domain.BusiFieldStart;
import com.ruoyi.system.service.IBusiFieldStartService;

/**
 * 外业启动申请Service业务层处理
 */
@Service
public class BusiFieldStartServiceImpl implements IBusiFieldStartService
{
    @Autowired
    private BusiFieldStartMapper busiFieldStartMapper;

    @Autowired
    private BusiProjectMapper busiProjectMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询外业启动申请
     */
    @Override
    public BusiFieldStart selectBusiFieldStartByStartId(Long startId)
    {
        BusiFieldStart result = busiFieldStartMapper.selectBusiFieldStartByStartId(startId);
        fillNames(result);
        return result;
    }

    /**
     * ============================================================
     * 修复报错的关键：实现单参数方法
     * 逻辑：直接调用下面的三参数方法，传 null 即可
     * ============================================================
     */
    @Override
    public List<BusiFieldStart> selectBusiFieldStartList(BusiFieldStart busiFieldStart)
    {
        // 这里的 null 表示不进行特殊 Tab 过滤，走默认查询逻辑
        return selectBusiFieldStartList(busiFieldStart, null, null);
    }

    /**
     * ============================================================
     * 核心逻辑：带参数的复杂查询
     * ============================================================
     */
    @Override
    public List<BusiFieldStart> selectBusiFieldStartList(BusiFieldStart busiFieldStart, String tabType, Long userId)
    {
        // 1. 根据 Tab 页签，强制覆盖查询条件
        if ("ALL".equals(tabType)) {
            busiFieldStart.setAuditStatus("1"); // 必须是已通过
        }
        else if ("AUDIT".equals(tabType)) {
            busiFieldStart.setAuditStatus("0"); // 必须是待审核
        }
        else if ("MINE".equals(tabType) && userId != null) {
            String currentId = String.valueOf(userId);
            // 拼接 SQL: (create_by = '101' OR FIND_IN_SET('101', members_inner))
            String sqlScope = " AND (create_by = '" + currentId + "' " +
                    " OR FIND_IN_SET('" + currentId + "', members_inner))";
            busiFieldStart.getParams().put("dataScope", sqlScope);
        }

        // 2. 执行查询
        List<BusiFieldStart> list = busiFieldStartMapper.selectBusiFieldStartList(busiFieldStart);

        // 3. 循环翻译
        for (BusiFieldStart item : list) {
            fillNames(item);
        }
        return list;
    }

    /**
     * 新增
     */
    @Override
    public int insertBusiFieldStart(BusiFieldStart busiFieldStart)
    {
        busiFieldStart.setAuditStatus("0");
        busiFieldStart.setCreateTime(DateUtils.getNowDate());
        return busiFieldStartMapper.insertBusiFieldStart(busiFieldStart);
    }

    /**
     * 修改
     */
    @Override
    public int updateBusiFieldStart(BusiFieldStart busiFieldStart)
    {
        // 【核心修改】：如果是 负责人、老师、管理员，修改时不重置状态
        boolean isManager = SecurityUtils.hasRole("fildstart_manager")
                || SecurityUtils.hasRole("teacher")
                || SecurityUtils.hasRole("admin");

        if (!isManager) {
            // 只有普通学生修改“驳回”的数据时，才重置为“待审核”
            BusiFieldStart oldData = busiFieldStartMapper.selectBusiFieldStartByStartId(busiFieldStart.getStartId());
            if (oldData != null && "2".equals(oldData.getAuditStatus())) {
                busiFieldStart.setAuditStatus("0");
                busiFieldStart.setAuditReason("");
            }
        }

        busiFieldStart.setUpdateTime(DateUtils.getNowDate());
        return busiFieldStartMapper.updateBusiFieldStart(busiFieldStart);
    }

    /**
     * 审核
     */
    @Override
    public int auditBusiFieldStart(Long startId, String status, String reason) {
        BusiFieldStart updateStart = new BusiFieldStart();
        updateStart.setStartId(startId);
        updateStart.setAuditStatus(status);
        updateStart.setAuditReason(reason);
        updateStart.setUpdateTime(DateUtils.getNowDate());
        return busiFieldStartMapper.updateBusiFieldStart(updateStart);
    }

    @Override
    public int deleteBusiFieldStartByStartIds(Long[] startIds) {
        return busiFieldStartMapper.deleteBusiFieldStartByStartIds(startIds);
    }

    @Override
    public int deleteBusiFieldStartByStartId(Long startId) {
        return busiFieldStartMapper.deleteBusiFieldStartByStartId(startId);
    }

    /**
     * 辅助方法
     */
    private void fillNames(BusiFieldStart item) {
        if (item == null) return;
        if (item.getProjectId() != null) {
            BusiProject p = busiProjectMapper.selectBusiProjectByProjectId(item.getProjectId());
            if (p != null) {
                item.setProjectName(p.getProjectName());
            }
        }
        if (StringUtils.isNotEmpty(item.getMembersInner())) {
            String[] ids = item.getMembersInner().split(",");
            List<String> names = new ArrayList<>();
            for (String idStr : ids) {
                try {
                    SysUser u = sysUserMapper.selectUserById(Long.valueOf(idStr));
                    if (u != null) {
                        names.add(u.getNickName());
                    }
                } catch (Exception e) {}
            }
            item.setMembersInnerNames(String.join(",", names));
        }
    }
}