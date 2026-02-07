package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BusiAssistantGuidanceMapper;
import com.ruoyi.system.domain.BusiAssistantGuidance;
import com.ruoyi.system.service.IBusiAssistantGuidanceService;

/**
 * 本科论文指导Service业务层处理
 */
@Service
public class BusiAssistantGuidanceServiceImpl implements IBusiAssistantGuidanceService
{
    @Autowired
    private BusiAssistantGuidanceMapper busiAssistantGuidanceMapper;

    @Override
    public BusiAssistantGuidance selectBusiAssistantGuidanceById(Long id)
    {
        return busiAssistantGuidanceMapper.selectBusiAssistantGuidanceById(id);
    }

    @Override
    public List<BusiAssistantGuidance> selectBusiAssistantGuidanceList(BusiAssistantGuidance busiAssistantGuidance)
    {
        return selectBusiAssistantGuidanceList(busiAssistantGuidance, null, null);
    }

    // 🚀 核心修复逻辑
    @Override
    public List<BusiAssistantGuidance> selectBusiAssistantGuidanceList(BusiAssistantGuidance busiAssistantGuidance, String tabType, Long userId)
    {
        if ("ALL".equals(tabType)) {
            // 全部记录：只看已通过
            busiAssistantGuidance.setAuditStatus("1");
        }
        else if ("AUDIT".equals(tabType)) {
            // 审核管理：只看待审核
            busiAssistantGuidance.setAuditStatus("0");

            // 🔥🔥🔥 关键修复点 🔥🔥🔥
            // 强制清空 AOP 注入的权限参数，防止老师只能看到自己创建的记录
            if (busiAssistantGuidance.getParams() != null) {
                busiAssistantGuidance.getParams().clear();
            }
        }
        else if ("MINE".equals(tabType) && userId != null) {
            // 我的提交：利用 dataScope 注入 SQL，强制只查 create_by = 当前用户
            busiAssistantGuidance.getParams().put("dataScope", " AND create_by = '" + userId + "'");
        }

        return busiAssistantGuidanceMapper.selectBusiAssistantGuidanceList(busiAssistantGuidance);
    }

    @Override
    public int insertBusiAssistantGuidance(BusiAssistantGuidance busiAssistantGuidance)
    {
        busiAssistantGuidance.setAuditStatus("0");
        busiAssistantGuidance.setCreateTime(DateUtils.getNowDate());
        return busiAssistantGuidanceMapper.insertBusiAssistantGuidance(busiAssistantGuidance);
    }

    @Override
    public int updateBusiAssistantGuidance(BusiAssistantGuidance busiAssistantGuidance)
    {
        // 驳回状态下修改，重置为待审核
        BusiAssistantGuidance old = busiAssistantGuidanceMapper.selectBusiAssistantGuidanceById(busiAssistantGuidance.getId());
        if (old != null && "2".equals(old.getAuditStatus())) {
            busiAssistantGuidance.setAuditStatus("0");
            busiAssistantGuidance.setAuditReason("");
        }
        busiAssistantGuidance.setUpdateTime(DateUtils.getNowDate());
        return busiAssistantGuidanceMapper.updateBusiAssistantGuidance(busiAssistantGuidance);
    }

    @Override
    public int auditBusiAssistantGuidance(Long id, String status, String reason) {
        BusiAssistantGuidance update = new BusiAssistantGuidance();
        update.setId(id);
        update.setAuditStatus(status);
        update.setAuditReason(reason);
        update.setUpdateTime(DateUtils.getNowDate());
        return busiAssistantGuidanceMapper.updateBusiAssistantGuidance(update);
    }

    @Override
    public int deleteBusiAssistantGuidanceByIds(Long[] ids)
    {
        return busiAssistantGuidanceMapper.deleteBusiAssistantGuidanceByIds(ids);
    }

    @Override
    public int deleteBusiAssistantGuidanceById(Long id)
    {
        return busiAssistantGuidanceMapper.deleteBusiAssistantGuidanceById(id);
    }
}