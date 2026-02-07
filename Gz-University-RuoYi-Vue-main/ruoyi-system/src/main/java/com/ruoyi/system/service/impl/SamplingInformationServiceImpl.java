package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.SampleUsageRecord;
import com.ruoyi.system.domain.SamplingInformation;
import com.ruoyi.system.mapper.SamplingInformationMapper;
import com.ruoyi.system.service.ISampleUsageRecordService;
import com.ruoyi.system.service.ISamplingInformationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 采样信息Service业务层处理
 *
 * @author ruoyi
 * @date 2025-07-19
 */
@Service
public class SamplingInformationServiceImpl implements ISamplingInformationService
{
    private static final Logger log = LoggerFactory.getLogger(SamplingInformationServiceImpl.class);

    @Autowired
    private SamplingInformationMapper samplingInformationMapper;

    // 如果需要使用 sampleUsageRecordService，请取消注释并添加 @Autowired
    @Autowired
    private ISampleUsageRecordService sampleUsageRecordService;

    /**
     * 查询采样信息
     *
     * @param id 采样信息主键
     * @return 采样信息
     */
    @Override
    public SamplingInformation selectSamplingInformationById(Long id)
    {
        return samplingInformationMapper.selectSamplingInformationById(id);
    }

    /**
     * 查询采样信息列表
     *
     * @param samplingInformation 采样信息
     * @return 采样信息
     */
    @Override
    public List<SamplingInformation> selectSamplingInformationList(SamplingInformation samplingInformation)
    {
        return samplingInformationMapper.selectSamplingInformationList(samplingInformation);
    }

    /**
     * 新增采样信息
     *
     * @param samplingInformation 采样信息
     * @return 结果
     */
    @Transactional
    @Override
    public int insertSamplingInformation(SamplingInformation samplingInformation)
    {
        // 移除了 insertSampleUsageRecord 调用，因为关联逻辑需要重新设计
        return samplingInformationMapper.insertSamplingInformation(samplingInformation);
    }

    /**
     * 修改采样信息
     *
     * @param samplingInformation 采样信息
     * @return 结果
     */
    @Transactional
    @Override
    public int updateSamplingInformation(SamplingInformation samplingInformation) {
        // 移除了 updatedTime 设置，使用基类的 updateTime
        return samplingInformationMapper.updateSamplingInformation(samplingInformation);
    }

    /**
     * 批量删除采样信息
     *
     * @param ids 需要删除的采样信息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteSamplingInformationByIds(Long[] ids)
    {
        // 先删除关联的使用记录
        samplingInformationMapper.deleteSampleUsageRecordBySampleIds(ids);
        // 再删除采样信息
        return samplingInformationMapper.deleteSamplingInformationByIds(ids);
    }

    @Override
    public boolean checkSampleNameUnique(SamplingInformation samplingInformation) {
        Long id = samplingInformation.getId();
        String sampleName = samplingInformation.getSampleName();

        if (StringUtils.isBlank(sampleName)) {
            return false;
        }

        SamplingInformation query = new SamplingInformation();
        query.setSampleName(sampleName);
        List<SamplingInformation> list = samplingInformationMapper.selectSamplingInformationList(query);

        if (list.isEmpty()) {
            return true; // 没有重复，唯一
        }

        // 编辑时排除自身
        if (id != null) {
            return list.stream().allMatch(item -> item.getId().equals(id));
        }

        return false; // 新增时有重复，不唯一
    }

    /**
     * 删除采样信息信息
     *
     * @param id 采样信息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteSamplingInformationById(Long id)
    {
        // 先删除关联的使用记录
        samplingInformationMapper.deleteSampleUsageRecordBySampleId(id);
        // 再删除采样信息
        return samplingInformationMapper.deleteSamplingInformationById(id);
    }

    /**
     * 导入采样信息数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importSamplingInformation(List<SamplingInformation> samplingInformationList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(samplingInformationList) || samplingInformationList.isEmpty()) {
            throw new ServiceException("导入采样信息数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        int updateNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        // 记录更新的样本编号，用于错误信息
        List<String> updatedSamples = new ArrayList<>();

        for (SamplingInformation samplingInformation : samplingInformationList) {
            try {
                // 验证样本编号是否唯一
                SamplingInformation checkInfo = new SamplingInformation();
                checkInfo.setSampleName(samplingInformation.getSampleName());
                List<SamplingInformation> existList = samplingInformationMapper.selectSamplingInformationList(checkInfo);

                if (!existList.isEmpty()) {
                    // 如果存在重复记录，默认进行更新（不再询问用户）
                    SamplingInformation existInfo = existList.get(0);
                    samplingInformation.setId(existInfo.getId());
                    samplingInformation.setUpdateBy(operName);
                    samplingInformation.setUpdateTime(new Date());
                    samplingInformationMapper.updateSamplingInformation(samplingInformation);
                    updateNum++;
                    updatedSamples.add(samplingInformation.getSampleName());
                } else {
                    // 新增记录
                    samplingInformation.setCreateBy(operName);
                    samplingInformation.setCreateTime(new Date());
                    samplingInformationMapper.insertSamplingInformation(samplingInformation);
                    successNum++;
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、样本 " + samplingInformation.getSampleName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }

        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            // 只显示汇总信息，不显示每条详细信息
            successMsg.append("恭喜您，数据已全部导入成功！共 " + (successNum + updateNum) + " 条，其中新增 " + successNum + " 条，更新 " + updateNum + " 条");

            // 如果有更新的样本，显示更新的样本编号
            if (!updatedSamples.isEmpty()) {
                successMsg.append("更新的样本编号：").append(String.join("、", updatedSamples));
            }
        }

        return successMsg.toString();
    }
    /**
     * 根据ID获取样本名称
     */
    @Override
    public String getSampleNameById(Long id) {
        SamplingInformation info = samplingInformationMapper.selectSamplingInformationById(id);
        return info != null ? info.getSampleName() : null;
    }

    /**
     * 根据ID列表获取样本名称列表
     */
    @Override
    public List<String> getSampleNamesByIds(Long[] ids) {
        List<String> sampleNames = new ArrayList<>();
        for (Long id : ids) {
            String sampleName = getSampleNameById(id);
            if (sampleName != null) {
                sampleNames.add(sampleName);
            }
        }
        return sampleNames;
    }
    /**
     * 新增样本使用记录信息
     * 注意：由于删除了 batchName 字段，此方法需要重新设计
     * 暂时保留但注释掉，待业务逻辑重新设计
     */
    /*
    public void insertSampleUsageRecord(SamplingInformation samplingInformation)
    {
        List<SampleUsageRecord> sampleUsageRecordList = samplingInformation.getSampleUsageRecordList();
        Long id = samplingInformation.getId();
        if (StringUtils.isNotNull(sampleUsageRecordList))
        {
            List<SampleUsageRecord> list = new ArrayList<SampleUsageRecord>();
            for (SampleUsageRecord sampleUsageRecord : sampleUsageRecordList)
            {
                sampleUsageRecord.setSampleId(id);
                // 注意：batchName 字段已删除，需要重新设计
                // sampleUsageRecord.setBatchName(samplingInformation.getBatchName());
                list.add(sampleUsageRecord);
            }
            if (list.size() > 0)
            {
                samplingInformationMapper.batchSampleUsageRecord(list);
            }
        }
    }
    */
}