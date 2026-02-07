package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BusiCameraPointMapper;
import com.ruoyi.system.domain.BusiCameraPoint;
import com.ruoyi.system.service.IBusiCameraPointService;

/**
 * 红外相机位点信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-12
 */
@Service
public class BusiCameraPointServiceImpl implements IBusiCameraPointService 
{
    @Autowired
    private BusiCameraPointMapper busiCameraPointMapper;

    /**
     * 查询红外相机位点信息
     * 
     * @param id 红外相机位点信息主键
     * @return 红外相机位点信息
     */
    @Override
    public BusiCameraPoint selectBusiCameraPointById(Long id)
    {
        return busiCameraPointMapper.selectBusiCameraPointById(id);
    }

    /**
     * 查询红外相机位点信息列表
     * 
     * @param busiCameraPoint 红外相机位点信息
     * @return 红外相机位点信息
     */
    @Override
    public List<BusiCameraPoint> selectBusiCameraPointList(BusiCameraPoint busiCameraPoint)
    {
        return busiCameraPointMapper.selectBusiCameraPointList(busiCameraPoint);
    }

    /**
     * 新增红外相机位点信息
     * 
     * @param busiCameraPoint 红外相机位点信息
     * @return 结果
     */
    @Override
    public int insertBusiCameraPoint(BusiCameraPoint busiCameraPoint)
    {
        busiCameraPoint.setCreateTime(DateUtils.getNowDate());
        return busiCameraPointMapper.insertBusiCameraPoint(busiCameraPoint);
    }

    /**
     * 修改红外相机位点信息
     * 
     * @param busiCameraPoint 红外相机位点信息
     * @return 结果
     */
    @Override
    public int updateBusiCameraPoint(BusiCameraPoint busiCameraPoint)
    {
        return busiCameraPointMapper.updateBusiCameraPoint(busiCameraPoint);
    }

    /**
     * 批量删除红外相机位点信息
     * 
     * @param ids 需要删除的红外相机位点信息主键
     * @return 结果
     */
    @Override
    public int deleteBusiCameraPointByIds(Long[] ids)
    {
        return busiCameraPointMapper.deleteBusiCameraPointByIds(ids);
    }

    /**
     * 删除红外相机位点信息信息
     * 
     * @param id 红外相机位点信息主键
     * @return 结果
     */
    @Override
    public int deleteBusiCameraPointById(Long id)
    {
        return busiCameraPointMapper.deleteBusiCameraPointById(id);
    }
}
