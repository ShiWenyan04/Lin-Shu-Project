package com.ruoyi.system.service;

import com.ruoyi.system.domain.SamplingInformation;

import java.util.List;

/**
 * 采样信息Service接口
 *
 * @author ruoyi
 * @date 2025-07-19
 */
public interface ISamplingInformationService
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
     * 批量删除采样信息
     *
     * @param ids 需要删除的采样信息主键集合
     * @return 结果
     */
    public int deleteSamplingInformationByIds(Long[] ids);
    /**
     * 检查样本编号是否唯一
     *
     * @param samplingInformation 采样信息
     * @return 结果
     */
    boolean checkSampleNameUnique(SamplingInformation samplingInformation);

    /**
     * 导入采样信息数据
     *
     * @param samplingInformationList 采样信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importSamplingInformation(List<SamplingInformation> samplingInformationList, Boolean isUpdateSupport, String operName);
    /**
     * 根据ID获取样本名称
     *
     * @param id 样本ID
     * @return 样本名称
     */
    String getSampleNameById(Long id);

    /**
     * 根据ID列表获取样本名称列表
     *
     * @param ids 样本ID列表
     * @return 样本名称列表
     */
    List<String> getSampleNamesByIds(Long[] ids);

    /**
     * 删除采样信息信息
     *
     * @param id 采样信息主键
     * @return 结果
     */
    public int deleteSamplingInformationById(Long id);
}