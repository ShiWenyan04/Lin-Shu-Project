package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ExperimentalEquipmentMapper;
import com.ruoyi.system.domain.ExperimentalEquipment;
import com.ruoyi.system.service.IExperimentalEquipmentService;

/**
 * 实验设备信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-16
 */
@Service
public class ExperimentalEquipmentServiceImpl implements IExperimentalEquipmentService 
{
    @Autowired
    private ExperimentalEquipmentMapper experimentalEquipmentMapper;

    /**
     * 查询实验设备信息
     * 
     * @param id 实验设备信息主键
     * @return 实验设备信息
     */
    @Override
    public ExperimentalEquipment selectExperimentalEquipmentById(Long id)
    {
        return experimentalEquipmentMapper.selectExperimentalEquipmentById(id);
    }

    /**
     * 查询实验设备信息列表
     * 
     * @param experimentalEquipment 实验设备信息
     * @return 实验设备信息
     */
    @Override
    public List<ExperimentalEquipment> selectExperimentalEquipmentList(ExperimentalEquipment experimentalEquipment)
    {
        return experimentalEquipmentMapper.selectExperimentalEquipmentList(experimentalEquipment);
    }

    /**
     * 新增实验设备信息
     * 
     * @param experimentalEquipment 实验设备信息
     * @return 结果
     */
    @Override
    public int insertExperimentalEquipment(ExperimentalEquipment experimentalEquipment)
    {
        experimentalEquipment.setCreateTime(DateUtils.getNowDate());
        return experimentalEquipmentMapper.insertExperimentalEquipment(experimentalEquipment);
    }

    /**
     * 修改实验设备信息
     * 
     * @param experimentalEquipment 实验设备信息
     * @return 结果
     */
    @Override
    public int updateExperimentalEquipment(ExperimentalEquipment experimentalEquipment)
    {
        experimentalEquipment.setUpdateTime(DateUtils.getNowDate());
        return experimentalEquipmentMapper.updateExperimentalEquipment(experimentalEquipment);
    }

    /**
     * 批量删除实验设备信息
     * 
     * @param ids 需要删除的实验设备信息主键
     * @return 结果
     */
    @Override
    public int deleteExperimentalEquipmentByIds(Long[] ids)
    {
        return experimentalEquipmentMapper.deleteExperimentalEquipmentByIds(ids);
    }

    /**
     * 删除实验设备信息信息
     * 
     * @param id 实验设备信息主键
     * @return 结果
     */
    @Override
    public int deleteExperimentalEquipmentById(Long id)
    {
        return experimentalEquipmentMapper.deleteExperimentalEquipmentById(id);
    }
}
