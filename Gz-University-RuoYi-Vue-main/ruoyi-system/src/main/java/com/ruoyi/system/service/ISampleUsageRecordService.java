package com.ruoyi.system.service;

import com.ruoyi.system.domain.SampleUsageRecord;

import java.util.List;

/**
 * 样本使用记录Service接口
 * 
 * @author ruoyi
 * @date 2025-07-19
 */
public interface ISampleUsageRecordService 
{
    /**
     * 查询样本使用记录
     * 
     * @param id 样本使用记录主键
     * @return 样本使用记录
     */
    public SampleUsageRecord selectSampleUsageRecordById(Long id);

    /**
     * 查询样本使用记录列表
     * 
     * @param sampleUsageRecord 样本使用记录
     * @return 样本使用记录集合
     */
    public List<SampleUsageRecord> selectSampleUsageRecordList(SampleUsageRecord sampleUsageRecord);

    /**
     * 新增样本使用记录
     * 
     * @param sampleUsageRecord 样本使用记录
     * @return 结果
     */
    public int insertSampleUsageRecord(SampleUsageRecord sampleUsageRecord);

    /**
     * 修改样本使用记录
     * 
     * @param sampleUsageRecord 样本使用记录
     * @return 结果
     */
    public int updateSampleUsageRecord(SampleUsageRecord sampleUsageRecord);

    /**
     * 批量删除样本使用记录
     * 
     * @param ids 需要删除的样本使用记录主键集合
     * @return 结果
     */
    public int deleteSampleUsageRecordByIds(Long[] ids);

    /**
     * 删除样本使用记录信息
     * 
     * @param id 样本使用记录主键
     * @return 结果
     */
    public int deleteSampleUsageRecordById(Long id);
}
