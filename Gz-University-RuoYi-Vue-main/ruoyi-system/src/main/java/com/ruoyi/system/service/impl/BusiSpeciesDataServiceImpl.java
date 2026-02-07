package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BusiSpeciesDataMapper;
import com.ruoyi.system.domain.BusiSpeciesData;
import com.ruoyi.system.service.IBusiSpeciesDataService;

/**
 * 物种监测数据Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-12
 */
@Service
public class BusiSpeciesDataServiceImpl implements IBusiSpeciesDataService 
{
    @Autowired
    private BusiSpeciesDataMapper busiSpeciesDataMapper;

    /**
     * 查询物种监测数据
     * 
     * @param id 物种监测数据主键
     * @return 物种监测数据
     */
    @Override
    public BusiSpeciesData selectBusiSpeciesDataById(Long id)
    {
        return busiSpeciesDataMapper.selectBusiSpeciesDataById(id);
    }

    /**
     * 查询物种监测数据列表
     * 
     * @param busiSpeciesData 物种监测数据
     * @return 物种监测数据
     */
    @Override
    public List<BusiSpeciesData> selectBusiSpeciesDataList(BusiSpeciesData busiSpeciesData)
    {
        return busiSpeciesDataMapper.selectBusiSpeciesDataList(busiSpeciesData);
    }

    /**
     * 新增物种监测数据
     * 
     * @param busiSpeciesData 物种监测数据
     * @return 结果
     */
    @Override
    public int insertBusiSpeciesData(BusiSpeciesData busiSpeciesData)
    {
        busiSpeciesData.setCreateTime(DateUtils.getNowDate());
        return busiSpeciesDataMapper.insertBusiSpeciesData(busiSpeciesData);
    }

    /**
     * 修改物种监测数据
     * 
     * @param busiSpeciesData 物种监测数据
     * @return 结果
     */
    @Override
    public int updateBusiSpeciesData(BusiSpeciesData busiSpeciesData)
    {
        return busiSpeciesDataMapper.updateBusiSpeciesData(busiSpeciesData);
    }

    /**
     * 批量删除物种监测数据
     * 
     * @param ids 需要删除的物种监测数据主键
     * @return 结果
     */
    @Override
    public int deleteBusiSpeciesDataByIds(Long[] ids)
    {
        return busiSpeciesDataMapper.deleteBusiSpeciesDataByIds(ids);
    }

    /**
     * 删除物种监测数据信息
     * 
     * @param id 物种监测数据主键
     * @return 结果
     */
    @Override
    public int deleteBusiSpeciesDataById(Long id)
    {
        return busiSpeciesDataMapper.deleteBusiSpeciesDataById(id);
    }
}
