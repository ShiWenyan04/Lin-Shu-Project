package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.PaperAchievement;

/**
 * 论文成果管理Service接口
 * * @author ruoyi
 * @date 2025-12-20
 */
public interface IPaperAchievementService
{
    /**
     * 查询论文成果管理
     */
    public PaperAchievement selectPaperAchievementById(Long id);

    /**
     * 查询论文成果管理列表
     */
    public List<PaperAchievement> selectPaperAchievementList(PaperAchievement paperAchievement);

    /**
     * 新增论文成果管理
     */
    public int insertPaperAchievement(PaperAchievement paperAchievement);

    /**
     * 修改论文成果管理
     */
    public int updatePaperAchievement(PaperAchievement paperAchievement);

    /**
     * 审核论文成果管理 (新增)
     */
    public int auditPaperAchievement(PaperAchievement paperAchievement);

    /**
     * 批量删除论文成果管理
     */
    public int deletePaperAchievementByIds(Long[] ids);

    /**
     * 删除论文成果管理信息
     */
    public int deletePaperAchievementById(Long id);
}