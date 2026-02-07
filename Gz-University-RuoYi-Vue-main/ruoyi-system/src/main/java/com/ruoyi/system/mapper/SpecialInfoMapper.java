package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SpecialInfo;

/**
 * 特种设备考证信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-16
 */
public interface SpecialInfoMapper 
{
    /**
     * 查询特种设备考证信息
     * 
     * @param id 特种设备考证信息主键
     * @return 特种设备考证信息
     */
    public SpecialInfo selectSpecialInfoById(Long id);

    /**
     * 查询特种设备考证信息列表
     * 
     * @param specialInfo 特种设备考证信息
     * @return 特种设备考证信息集合
     */
    public List<SpecialInfo> selectSpecialInfoList(SpecialInfo specialInfo);

    /**
     * 新增特种设备考证信息
     * 
     * @param specialInfo 特种设备考证信息
     * @return 结果
     */
    public int insertSpecialInfo(SpecialInfo specialInfo);

    /**
     * 修改特种设备考证信息
     * 
     * @param specialInfo 特种设备考证信息
     * @return 结果
     */
    public int updateSpecialInfo(SpecialInfo specialInfo);

    /**
     * 删除特种设备考证信息
     * 
     * @param id 特种设备考证信息主键
     * @return 结果
     */
    public int deleteSpecialInfoById(Long id);

    /**
     * 批量删除特种设备考证信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSpecialInfoByIds(Long[] ids);
}
