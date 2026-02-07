package com.ruoyi.system.service;

import com.ruoyi.system.domain.SequencingSample;

import java.util.List;

/**
 * 测序信息Service接口
 * 
 * @author ruoyi
 * @date 2025-07-20
 */
public interface ISequencingSampleService 
{
    /**
     * 查询测序信息
     * 
     * @param id 测序信息主键
     * @return 测序信息
     */
    public SequencingSample selectSequencingSampleById(Long id);

    /**
     * 查询测序信息列表
     * 
     * @param sequencingSample 测序信息
     * @return 测序信息集合
     */
    public List<SequencingSample> selectSequencingSampleList(SequencingSample sequencingSample);

    /**
     * 新增测序信息
     * 
     * @param sequencingSample 测序信息
     * @return 结果
     */
    public int insertSequencingSample(SequencingSample sequencingSample);

    /**
     * 修改测序信息
     * 
     * @param sequencingSample 测序信息
     * @return 结果
     */
    public int updateSequencingSample(SequencingSample sequencingSample);

    /**
     * 批量删除测序信息
     * 
     * @param ids 需要删除的测序信息主键集合
     * @return 结果
     */
    public int deleteSequencingSampleByIds(Long[] ids);

    /**
     * 删除测序信息信息
     * 
     * @param id 测序信息主键
     * @return 结果
     */
    public int deleteSequencingSampleById(Long id);
    /**
     * 导入测序信息信息
     */
    String importSequencingSample(List<SequencingSample> sampleList, Boolean isUpdateSupport);
}
