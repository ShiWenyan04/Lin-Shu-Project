package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BusiAssistantGuidance;

/**
 * 本科论文指导Service接口
 */
public interface IBusiAssistantGuidanceService
{
    public BusiAssistantGuidance selectBusiAssistantGuidanceById(Long id);

    public List<BusiAssistantGuidance> selectBusiAssistantGuidanceList(BusiAssistantGuidance busiAssistantGuidance);

    // 🚀 关键新增：带 Tab 权限过滤的查询接口
    public List<BusiAssistantGuidance> selectBusiAssistantGuidanceList(BusiAssistantGuidance busiAssistantGuidance, String tabType, Long userId);

    // 🚀 关键新增：审核接口
    public int auditBusiAssistantGuidance(Long id, String status, String reason);

    public int insertBusiAssistantGuidance(BusiAssistantGuidance busiAssistantGuidance);
    public int updateBusiAssistantGuidance(BusiAssistantGuidance busiAssistantGuidance);
    public int deleteBusiAssistantGuidanceByIds(Long[] ids);
    public int deleteBusiAssistantGuidanceById(Long id);
}