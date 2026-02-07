package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SpecimenInfo;
import com.ruoyi.system.mapper.SpecimenInfoMapper;
import com.ruoyi.system.service.ISpecimenInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 标本信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-19
 */
@Service
public class SpecimenInfoServiceImpl implements ISpecimenInfoService 
{
    @Autowired
    private SpecimenInfoMapper specimenInfoMapper;

    /**
     * 查询标本信息
     * 
     * @param id 标本信息主键
     * @return 标本信息
     */
    @Override
    public SpecimenInfo selectSpecimenInfoById(Long id)
    {
        return specimenInfoMapper.selectSpecimenInfoById(id);
    }

    /**
     * 查询标本信息列表
     * 
     * @param specimenInfo 标本信息
     * @return 标本信息
     */
    @Override
    public List<SpecimenInfo> selectSpecimenInfoList(SpecimenInfo specimenInfo)
    {
        return specimenInfoMapper.selectSpecimenInfoList(specimenInfo);
    }

    /**
     * 新增标本信息
     * 
     * @param specimenInfo 标本信息
     * @return 结果
     */
    @Override
    public int insertSpecimenInfo(SpecimenInfo specimenInfo)
    {
        return specimenInfoMapper.insertSpecimenInfo(specimenInfo);
    }

    /**
     * 修改标本信息
     * 
     * @param specimenInfo 标本信息
     * @return 结果
     */
    @Override
    public int updateSpecimenInfo(SpecimenInfo specimenInfo)
    {
        return specimenInfoMapper.updateSpecimenInfo(specimenInfo);
    }

    /**
     * 批量删除标本信息
     * 
     * @param ids 需要删除的标本信息主键
     * @return 结果
     */
    @Override
    public int deleteSpecimenInfoByIds(Long[] ids)
    {
        return specimenInfoMapper.deleteSpecimenInfoByIds(ids);
    }

    /**
     * 删除标本信息信息
     * 
     * @param id 标本信息主键
     * @return 结果
     */
    @Override
    public int deleteSpecimenInfoById(Long id)
    {
        return specimenInfoMapper.deleteSpecimenInfoById(id);
    }
}
