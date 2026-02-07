package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.ActivityTraining;

/**
 * 实训与竞赛活动Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-15
 */
public interface ActivityTrainingMapper 
{
    /**
     * 查询实训与竞赛活动
     * 
     * @param id 实训与竞赛活动主键
     * @return 实训与竞赛活动
     */
    public ActivityTraining selectActivityTrainingById(Long id);

    /**
     * 查询实训与竞赛活动列表
     * 
     * @param activityTraining 实训与竞赛活动
     * @return 实训与竞赛活动集合
     */
    public List<ActivityTraining> selectActivityTrainingList(ActivityTraining activityTraining);

    /**
     * 新增实训与竞赛活动
     * 
     * @param activityTraining 实训与竞赛活动
     * @return 结果
     */
    public int insertActivityTraining(ActivityTraining activityTraining);

    /**
     * 修改实训与竞赛活动
     * 
     * @param activityTraining 实训与竞赛活动
     * @return 结果
     */
    public int updateActivityTraining(ActivityTraining activityTraining);

    /**
     * 删除实训与竞赛活动
     * 
     * @param id 实训与竞赛活动主键
     * @return 结果
     */
    public int deleteActivityTrainingById(Long id);

    /**
     * 批量删除实训与竞赛活动
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteActivityTrainingByIds(Long[] ids);
}
