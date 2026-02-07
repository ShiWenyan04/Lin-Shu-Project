package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BusiSpeciesData;

/**
 * 物种监测数据Service接口
 * 
 * @author ruoyi
 * @date 2026-01-12
 */
public interface IBusiSpeciesDataService 
{
    /**
     * 查询物种监测数据
     * 
     * @param id 物种监测数据主键
     * @return 物种监测数据
     */
    public BusiSpeciesData selectBusiSpeciesDataById(Long id);

    /**
     * 查询物种监测数据列表
     * 
     * @param busiSpeciesData 物种监测数据
     * @return 物种监测数据集合
     */
    public List<BusiSpeciesData> selectBusiSpeciesDataList(BusiSpeciesData busiSpeciesData);

    /**
     * 新增物种监测数据
     * 
     * @param busiSpeciesData 物种监测数据
     * @return 结果
     */
    public int insertBusiSpeciesData(BusiSpeciesData busiSpeciesData);

    /**
     * 修改物种监测数据
     * 
     * @param busiSpeciesData 物种监测数据
     * @return 结果
     */
    public int updateBusiSpeciesData(BusiSpeciesData busiSpeciesData);

    /**
     * 批量删除物种监测数据
     * 
     * @param ids 需要删除的物种监测数据主键集合
     * @return 结果
     */
    public int deleteBusiSpeciesDataByIds(Long[] ids);

    /**
     * 删除物种监测数据信息
     * 
     * @param id 物种监测数据主键
     * @return 结果
     */
    public int deleteBusiSpeciesDataById(Long id);
}
