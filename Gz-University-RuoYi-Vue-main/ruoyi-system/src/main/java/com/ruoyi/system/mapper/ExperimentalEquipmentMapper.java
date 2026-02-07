package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.ExperimentalEquipment;

/**
 * 实验设备信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-16
 */
public interface ExperimentalEquipmentMapper 
{
    /**
     * 查询实验设备信息
     * 
     * @param id 实验设备信息主键
     * @return 实验设备信息
     */
    public ExperimentalEquipment selectExperimentalEquipmentById(Long id);

    /**
     * 查询实验设备信息列表
     * 
     * @param experimentalEquipment 实验设备信息
     * @return 实验设备信息集合
     */
    public List<ExperimentalEquipment> selectExperimentalEquipmentList(ExperimentalEquipment experimentalEquipment);

    /**
     * 新增实验设备信息
     * 
     * @param experimentalEquipment 实验设备信息
     * @return 结果
     */
    public int insertExperimentalEquipment(ExperimentalEquipment experimentalEquipment);

    /**
     * 修改实验设备信息
     * 
     * @param experimentalEquipment 实验设备信息
     * @return 结果
     */
    public int updateExperimentalEquipment(ExperimentalEquipment experimentalEquipment);

    /**
     * 删除实验设备信息
     * 
     * @param id 实验设备信息主键
     * @return 结果
     */
    public int deleteExperimentalEquipmentById(Long id);

    /**
     * 批量删除实验设备信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteExperimentalEquipmentByIds(Long[] ids);
}
