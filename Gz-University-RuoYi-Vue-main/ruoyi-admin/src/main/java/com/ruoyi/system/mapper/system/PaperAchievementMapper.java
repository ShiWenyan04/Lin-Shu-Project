package com.ruoyi.system.mapper.system;

import java.util.List;
import com.ruoyi.system.domain.PaperAchievement;

/**
 * 论文成果管理Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-20
 */
public interface PaperAchievementMapper 
{
    /**
     * 查询论文成果管理
     * 
     * @param id 论文成果管理主键
     * @return 论文成果管理
     */
    public PaperAchievement selectPaperAchievementById(Long id);

    /**
     * 查询论文成果管理列表
     * 
     * @param paperAchievement 论文成果管理
     * @return 论文成果管理集合
     */
    public List<PaperAchievement> selectPaperAchievementList(PaperAchievement paperAchievement);

    /**
     * 新增论文成果管理
     * 
     * @param paperAchievement 论文成果管理
     * @return 结果
     */
    public int insertPaperAchievement(PaperAchievement paperAchievement);

    /**
     * 修改论文成果管理
     * 
     * @param paperAchievement 论文成果管理
     * @return 结果
     */
    public int updatePaperAchievement(PaperAchievement paperAchievement);

    /**
     * 删除论文成果管理
     * 
     * @param id 论文成果管理主键
     * @return 结果
     */
    public int deletePaperAchievementById(Long id);

    /**
     * 批量删除论文成果管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePaperAchievementByIds(Long[] ids);
}
