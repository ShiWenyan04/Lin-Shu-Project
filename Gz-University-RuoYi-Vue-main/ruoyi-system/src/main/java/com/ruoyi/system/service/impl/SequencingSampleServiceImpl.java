package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.SequencingSample;
import com.ruoyi.system.mapper.SequencingSampleMapper;
import com.ruoyi.system.service.ISequencingSampleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 测序信息Service业务层处理
 *
 * @author ruoyi
 * @date 2025-07-20
 */
@Service
public class SequencingSampleServiceImpl implements ISequencingSampleService
{
    @Autowired
    private SequencingSampleMapper sequencingSampleMapper;

    private static final Logger log = LoggerFactory.getLogger(SequencingSampleServiceImpl.class);

    /**
     * 查询测序信息
     *
     * @param id 测序信息主键
     * @return 测序信息
     */
    @Override
    public SequencingSample selectSequencingSampleById(Long id)
    {
        return sequencingSampleMapper.selectSequencingSampleById(id);
    }

    /**
     * 查询测序信息列表
     *
     * @param sequencingSample 测序信息
     * @return 测序信息
     */
    @Override
    public List<SequencingSample> selectSequencingSampleList(SequencingSample sequencingSample)
    {
        return sequencingSampleMapper.selectSequencingSampleList(sequencingSample);
    }

    /**
     * 新增测序信息
     *
     * @param sequencingSample 测序信息
     * @return 结果
     */
    @Override
    public int insertSequencingSample(SequencingSample sequencingSample)
    {
        // 可以在这里添加业务逻辑校验
        validateSequencingSample(sequencingSample);
        return sequencingSampleMapper.insertSequencingSample(sequencingSample);
    }

    /**
     * 修改测序信息
     *
     * @param sequencingSample 测序信息
     * @return 结果
     */
    @Override
    public int updateSequencingSample(SequencingSample sequencingSample)
    {
        // 可以在这里添加业务逻辑校验
        validateSequencingSample(sequencingSample);
        return sequencingSampleMapper.updateSequencingSample(sequencingSample);
    }

    /**
     * 批量删除测序信息
     *
     * @param ids 需要删除的测序信息主键
     * @return 结果
     */
    @Override
    public int deleteSequencingSampleByIds(Long[] ids)
    {
        return sequencingSampleMapper.deleteSequencingSampleByIds(ids);
    }

    /**
     * 删除测序信息信息
     *
     * @param id 测序信息主键
     * @return 结果
     */
    @Override
    public int deleteSequencingSampleById(Long id)
    {
        return sequencingSampleMapper.deleteSequencingSampleById(id);
    }

    /**
     * 导入测序信息
     */
    @Override
    public String importSequencingSample(List<SequencingSample> sampleList, Boolean isUpdateSupport) {
        if (sampleList == null || sampleList.isEmpty()) {
            throw new ServiceException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        int updateNum = 0; // 新增：专门记录更新成功的条数
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (SequencingSample sample : sampleList) {
            try {
                // 校验必填字段
                validateRequiredFields(sample);

                // 检查是否允许更新已有数据
                SequencingSample exist = sequencingSampleMapper.selectSequencingSampleBySampleName(sample.getSampleName());
                if (exist == null) {
                    sequencingSampleMapper.insertSequencingSample(sample);
                    successNum++;
                    // 移除了单条成功导入的消息拼接
                } else if (isUpdateSupport) {
                    sample.setId(exist.getId());
                    sequencingSampleMapper.updateSequencingSample(sample);
                    updateNum++; // 使用新的计数器
                    successMsg.append("<br/>").append(updateNum).append("、样本 ").append(sample.getSampleName()).append(" 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、样本 ").append(sample.getSampleName()).append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、样本 " + sample.getSampleName() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }

        if (failureNum > 0) {
            failureMsg.insert(0, "导入失败！共 " + failureNum + " 条数据格式不正确：");
            throw new ServiceException(failureMsg.toString());
        } else {
            // 修改成功提示，只显示成功和更新的总条数，不显示具体哪条成功
            if (updateNum > 0) {
                successMsg.insert(0, "成功导入 " + successNum + " 条新数据，并更新 " + updateNum + " 条现有数据。");
            } else {
                successMsg.insert(0, "成功导入 " + successNum + " 条数据。");
            }
        }
        return successMsg.toString();
    }

    /**
     * 校验测序信息必填字段
     */
    private void validateRequiredFields(SequencingSample sample) {
        if (sample.getSequencingNo() == null) {
            throw new ServiceException("测序编号不能为空");
        }
        if (sample.getSampleName() == null || sample.getSampleName().trim().isEmpty()) {
            throw new ServiceException("样本编号不能为空");
        }
        if (sample.getHost() == null || sample.getHost().trim().isEmpty()) {
            throw new ServiceException("宿主不能为空");
        }
        if (sample.getSampleType() == null || sample.getSampleType().trim().isEmpty()) {
            throw new ServiceException("样本类型不能为空");
        }
        if (sample.getSequencingType() == null || sample.getSequencingType().trim().isEmpty()) {
            throw new ServiceException("测序类型不能为空");
        }
        if (sample.getSequencer() == null || sample.getSequencer().trim().isEmpty()) {
            throw new ServiceException("测序者不能为空");
        }
        if (sample.getSequencingCompany() == null || sample.getSequencingCompany().trim().isEmpty()) {
            throw new ServiceException("测序公司不能为空");
        }
        if (sample.getSequencingDate() == null) {
            throw new ServiceException("测序时间不能为空");
        }
        if (sample.getStorageLocation() == null || sample.getStorageLocation().trim().isEmpty()) {
            throw new ServiceException("存储位置不能为空");
        }
    }

    /**
     * 校验测序信息业务逻辑
     */
    private void validateSequencingSample(SequencingSample sample) {
        validateRequiredFields(sample);
        // 这里可以添加其他业务逻辑校验
        // 例如：样本名称唯一性校验（除了当前记录外）
        // 经纬度格式校验等
    }
}