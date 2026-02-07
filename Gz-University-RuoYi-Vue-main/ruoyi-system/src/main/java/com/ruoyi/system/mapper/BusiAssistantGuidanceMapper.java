package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BusiAssistantGuidance;

/**
 * 本科论文指导Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-08
 */
public interface BusiAssistantGuidanceMapper 
{
    /**
     * 查询本科论文指导
     * 
     * @param id 本科论文指导主键
     * @return 本科论文指导
     */
    public BusiAssistantGuidance selectBusiAssistantGuidanceById(Long id);

    /**
     * 查询本科论文指导列表
     * 
     * @param busiAssistantGuidance 本科论文指导
     * @return 本科论文指导集合
     */
    public List<BusiAssistantGuidance> selectBusiAssistantGuidanceList(BusiAssistantGuidance busiAssistantGuidance);

    /**
     * 新增本科论文指导
     * 
     * @param busiAssistantGuidance 本科论文指导
     * @return 结果
     */
    public int insertBusiAssistantGuidance(BusiAssistantGuidance busiAssistantGuidance);

    /**
     * 修改本科论文指导
     * 
     * @param busiAssistantGuidance 本科论文指导
     * @return 结果
     */
    public int updateBusiAssistantGuidance(BusiAssistantGuidance busiAssistantGuidance);

    /**
     * 删除本科论文指导
     * 
     * @param id 本科论文指导主键
     * @return 结果
     */
    public int deleteBusiAssistantGuidanceById(Long id);

    /**
     * 批量删除本科论文指导
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusiAssistantGuidanceByIds(Long[] ids);
}
