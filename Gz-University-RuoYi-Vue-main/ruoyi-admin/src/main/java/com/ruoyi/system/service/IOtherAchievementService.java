package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.OtherAchievement;

/**
 * 其他成果管理Service接口
 * * @author ruoyi
 * @date 2025-12-18
 */
public interface IOtherAchievementService
{
    /**
     * 查询其他成果管理
     */
    public OtherAchievement selectOtherAchievementById(Long id);

    /**
     * 查询其他成果管理列表
     */
    public List<OtherAchievement> selectOtherAchievementList(OtherAchievement otherAchievement);

    /**
     * 新增其他成果管理
     */
    public int insertOtherAchievement(OtherAchievement otherAchievement);

    /**
     * 修改其他成果管理
     */
    public int updateOtherAchievement(OtherAchievement otherAchievement);

    /**
     * 审核其他成果 (新增)
     */
    public int auditOtherAchievement(OtherAchievement otherAchievement);

    /**
     * 批量删除其他成果管理
     */
    public int deleteOtherAchievementByIds(Long[] ids);

    /**
     * 删除其他成果管理信息
     */
    public int deleteOtherAchievementById(Long id);
}