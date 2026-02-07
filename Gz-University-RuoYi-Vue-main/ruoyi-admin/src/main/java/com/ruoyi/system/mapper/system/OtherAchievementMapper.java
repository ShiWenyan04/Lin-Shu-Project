package com.ruoyi.system.mapper.system;

import java.util.List;
import com.ruoyi.system.domain.OtherAchievement;

/**
 * 其他成果管理Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
public interface OtherAchievementMapper 
{
    /**
     * 查询其他成果管理
     * 
     * @param id 其他成果管理主键
     * @return 其他成果管理
     */
    public OtherAchievement selectOtherAchievementById(Long id);

    /**
     * 查询其他成果管理列表
     * 
     * @param otherAchievement 其他成果管理
     * @return 其他成果管理集合
     */
    public List<OtherAchievement> selectOtherAchievementList(OtherAchievement otherAchievement);

    /**
     * 新增其他成果管理
     * 
     * @param otherAchievement 其他成果管理
     * @return 结果
     */
    public int insertOtherAchievement(OtherAchievement otherAchievement);

    /**
     * 修改其他成果管理
     * 
     * @param otherAchievement 其他成果管理
     * @return 结果
     */
    public int updateOtherAchievement(OtherAchievement otherAchievement);

    /**
     * 删除其他成果管理
     * 
     * @param id 其他成果管理主键
     * @return 结果
     */
    public int deleteOtherAchievementById(Long id);

    /**
     * 批量删除其他成果管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOtherAchievementByIds(Long[] ids);
}
