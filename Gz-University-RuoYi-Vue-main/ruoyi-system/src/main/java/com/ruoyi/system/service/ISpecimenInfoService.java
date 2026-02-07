package com.ruoyi.system.service;

import com.ruoyi.system.domain.SpecimenInfo;

import java.util.List;

/**
 * 标本信息Service接口
 * 
 * @author ruoyi
 * @date 2025-07-19
 */
public interface ISpecimenInfoService 
{
    /**
     * 查询标本信息
     * 
     * @param id 标本信息主键
     * @return 标本信息
     */
    public SpecimenInfo selectSpecimenInfoById(Long id);

    /**
     * 查询标本信息列表
     * 
     * @param specimenInfo 标本信息
     * @return 标本信息集合
     */
    public List<SpecimenInfo> selectSpecimenInfoList(SpecimenInfo specimenInfo);

    /**
     * 新增标本信息
     * 
     * @param specimenInfo 标本信息
     * @return 结果
     */
    public int insertSpecimenInfo(SpecimenInfo specimenInfo);

    /**
     * 修改标本信息
     * 
     * @param specimenInfo 标本信息
     * @return 结果
     */
    public int updateSpecimenInfo(SpecimenInfo specimenInfo);

    /**
     * 批量删除标本信息
     * 
     * @param ids 需要删除的标本信息主键集合
     * @return 结果
     */
    public int deleteSpecimenInfoByIds(Long[] ids);

    /**
     * 删除标本信息信息
     * 
     * @param id 标本信息主键
     * @return 结果
     */
    public int deleteSpecimenInfoById(Long id);
}
