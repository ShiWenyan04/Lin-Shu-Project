package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SpecialInfoMapper;
import com.ruoyi.system.domain.SpecialInfo;
import com.ruoyi.system.service.ISpecialInfoService;

/**
 * 特种设备考证信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-16
 */
@Service
public class SpecialInfoServiceImpl implements ISpecialInfoService 
{
    @Autowired
    private SpecialInfoMapper specialInfoMapper;

    /**
     * 查询特种设备考证信息
     * 
     * @param id 特种设备考证信息主键
     * @return 特种设备考证信息
     */
    @Override
    public SpecialInfo selectSpecialInfoById(Long id)
    {
        return specialInfoMapper.selectSpecialInfoById(id);
    }

    /**
     * 查询特种设备考证信息列表
     * 
     * @param specialInfo 特种设备考证信息
     * @return 特种设备考证信息
     */
    @Override
    public List<SpecialInfo> selectSpecialInfoList(SpecialInfo specialInfo)
    {
        return specialInfoMapper.selectSpecialInfoList(specialInfo);
    }

    /**
     * 新增特种设备考证信息
     * 
     * @param specialInfo 特种设备考证信息
     * @return 结果
     */
    @Override
    public int insertSpecialInfo(SpecialInfo specialInfo)
    {
        return specialInfoMapper.insertSpecialInfo(specialInfo);
    }

    /**
     * 修改特种设备考证信息
     * 
     * @param specialInfo 特种设备考证信息
     * @return 结果
     */
    @Override
    public int updateSpecialInfo(SpecialInfo specialInfo)
    {
        return specialInfoMapper.updateSpecialInfo(specialInfo);
    }

    /**
     * 批量删除特种设备考证信息
     * 
     * @param ids 需要删除的特种设备考证信息主键
     * @return 结果
     */
    @Override
    public int deleteSpecialInfoByIds(Long[] ids)
    {
        return specialInfoMapper.deleteSpecialInfoByIds(ids);
    }

    /**
     * 删除特种设备考证信息信息
     * 
     * @param id 特种设备考证信息主键
     * @return 结果
     */
    @Override
    public int deleteSpecialInfoById(Long id)
    {
        return specialInfoMapper.deleteSpecialInfoById(id);
    }
}
