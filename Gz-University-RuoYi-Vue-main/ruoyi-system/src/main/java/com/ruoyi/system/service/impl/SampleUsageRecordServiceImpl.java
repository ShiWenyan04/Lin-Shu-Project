package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SampleUsageRecord;
import com.ruoyi.system.mapper.SampleUsageRecordMapper;
import com.ruoyi.system.service.ISampleUsageRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 样本使用记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-19
 */
@Service
public class SampleUsageRecordServiceImpl implements ISampleUsageRecordService 
{
    @Autowired
    private SampleUsageRecordMapper sampleUsageRecordMapper;

    /**
     * 查询样本使用记录
     * 
     * @param id 样本使用记录主键
     * @return 样本使用记录
     */
    @Override
    public SampleUsageRecord selectSampleUsageRecordById(Long id)
    {
        return sampleUsageRecordMapper.selectSampleUsageRecordById(id);
    }

    /**
     * 查询样本使用记录列表
     * 
     * @param sampleUsageRecord 样本使用记录
     * @return 样本使用记录
     */
    @Override
    public List<SampleUsageRecord> selectSampleUsageRecordList(SampleUsageRecord sampleUsageRecord)
    {
        return sampleUsageRecordMapper.selectSampleUsageRecordList(sampleUsageRecord);
    }

    /**
     * 新增样本使用记录
     * 
     * @param sampleUsageRecord 样本使用记录
     * @return 结果
     */
    @Override
    public int insertSampleUsageRecord(SampleUsageRecord sampleUsageRecord)
    {
        return sampleUsageRecordMapper.insertSampleUsageRecord(sampleUsageRecord);
    }

    /**
     * 修改样本使用记录
     * 
     * @param sampleUsageRecord 样本使用记录
     * @return 结果
     */
    @Override
    public int updateSampleUsageRecord(SampleUsageRecord sampleUsageRecord)
    {
        return sampleUsageRecordMapper.updateSampleUsageRecord(sampleUsageRecord);
    }

    /**
     * 批量删除样本使用记录
     * 
     * @param ids 需要删除的样本使用记录主键
     * @return 结果
     */
    @Override
    public int deleteSampleUsageRecordByIds(Long[] ids)
    {
        return sampleUsageRecordMapper.deleteSampleUsageRecordByIds(ids);
    }

    /**
     * 删除样本使用记录信息
     * 
     * @param id 样本使用记录主键
     * @return 结果
     */
    @Override
    public int deleteSampleUsageRecordById(Long id)
    {
        return sampleUsageRecordMapper.deleteSampleUsageRecordById(id);
    }
}
