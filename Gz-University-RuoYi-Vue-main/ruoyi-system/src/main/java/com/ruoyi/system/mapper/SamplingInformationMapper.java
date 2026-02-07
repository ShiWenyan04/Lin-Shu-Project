package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SampleUsageRecord;
import com.ruoyi.system.domain.SamplingInformation;

import java.util.List;

/**
 * 采样信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-07-19
 */
public interface SamplingInformationMapper 
{
    /**
     * 查询采样信息
     * 
     * @param id 采样信息主键
     * @return 采样信息
     */
    public SamplingInformation selectSamplingInformationById(Long id);

    /**
     * 查询采样信息列表
     * 
     * @param samplingInformation 采样信息
     * @return 采样信息集合
     */
    public List<SamplingInformation> selectSamplingInformationList(SamplingInformation samplingInformation);

    /**
     * 新增采样信息
     * 
     * @param samplingInformation 采样信息
     * @return 结果
     */
    public int insertSamplingInformation(SamplingInformation samplingInformation);

    /**
     * 修改采样信息
     * 
     * @param samplingInformation 采样信息
     * @return 结果
     */
    public int updateSamplingInformation(SamplingInformation samplingInformation);

    /**
     * 删除采样信息
     * 
     * @param id 采样信息主键
     * @return 结果
     */
    public int deleteSamplingInformationById(Long id);

    /**
     * 批量删除采样信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSamplingInformationByIds(Long[] ids);

    /**
     * 批量删除样本使用记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSampleUsageRecordBySampleIds(Long[] ids);
    
    /**
     * 批量新增样本使用记录
     * 
     * @param sampleUsageRecordList 样本使用记录列表
     * @return 结果
     */
    public int batchSampleUsageRecord(List<SampleUsageRecord> sampleUsageRecordList);
    

    /**
     * 通过采样信息主键删除样本使用记录信息
     * 
     * @param id 采样信息ID
     * @return 结果
     */
    public int deleteSampleUsageRecordBySampleId(Long id);
}
