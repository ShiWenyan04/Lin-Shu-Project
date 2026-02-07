package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ActivityTrainingMapper;
import com.ruoyi.system.domain.ActivityTraining;
import com.ruoyi.system.service.IActivityTrainingService;

/**
 * 实训与竞赛活动Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-15
 */
@Service
public class ActivityTrainingServiceImpl implements IActivityTrainingService 
{
    @Autowired
    private ActivityTrainingMapper activityTrainingMapper;

    /**
     * 查询实训与竞赛活动
     * 
     * @param id 实训与竞赛活动主键
     * @return 实训与竞赛活动
     */
    @Override
    public ActivityTraining selectActivityTrainingById(Long id)
    {
        return activityTrainingMapper.selectActivityTrainingById(id);
    }

    /**
     * 查询实训与竞赛活动列表
     * 
     * @param activityTraining 实训与竞赛活动
     * @return 实训与竞赛活动
     */
    @Override
    public List<ActivityTraining> selectActivityTrainingList(ActivityTraining activityTraining)
    {
        return activityTrainingMapper.selectActivityTrainingList(activityTraining);
    }

    /**
     * 新增实训与竞赛活动
     * 
     * @param activityTraining 实训与竞赛活动
     * @return 结果
     */
    @Override
    public int insertActivityTraining(ActivityTraining activityTraining)
    {
        activityTraining.setCreateTime(DateUtils.getNowDate());
        return activityTrainingMapper.insertActivityTraining(activityTraining);
    }

    /**
     * 修改实训与竞赛活动
     * 
     * @param activityTraining 实训与竞赛活动
     * @return 结果
     */
    @Override
    public int updateActivityTraining(ActivityTraining activityTraining)
    {
        activityTraining.setUpdateTime(DateUtils.getNowDate());
        return activityTrainingMapper.updateActivityTraining(activityTraining);
    }

    /**
     * 批量删除实训与竞赛活动
     * 
     * @param ids 需要删除的实训与竞赛活动主键
     * @return 结果
     */
    @Override
    public int deleteActivityTrainingByIds(Long[] ids)
    {
        return activityTrainingMapper.deleteActivityTrainingByIds(ids);
    }

    /**
     * 删除实训与竞赛活动信息
     * 
     * @param id 实训与竞赛活动主键
     * @return 结果
     */
    @Override
    public int deleteActivityTrainingById(Long id)
    {
        return activityTrainingMapper.deleteActivityTrainingById(id);
    }
}
