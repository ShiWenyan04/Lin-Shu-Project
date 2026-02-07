package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BusiCameraPoint;

/**
 * 红外相机位点信息Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-12
 */
public interface BusiCameraPointMapper 
{
    /**
     * 查询红外相机位点信息
     * 
     * @param id 红外相机位点信息主键
     * @return 红外相机位点信息
     */
    public BusiCameraPoint selectBusiCameraPointById(Long id);

    /**
     * 查询红外相机位点信息列表
     * 
     * @param busiCameraPoint 红外相机位点信息
     * @return 红外相机位点信息集合
     */
    public List<BusiCameraPoint> selectBusiCameraPointList(BusiCameraPoint busiCameraPoint);

    /**
     * 新增红外相机位点信息
     * 
     * @param busiCameraPoint 红外相机位点信息
     * @return 结果
     */
    public int insertBusiCameraPoint(BusiCameraPoint busiCameraPoint);

    /**
     * 修改红外相机位点信息
     * 
     * @param busiCameraPoint 红外相机位点信息
     * @return 结果
     */
    public int updateBusiCameraPoint(BusiCameraPoint busiCameraPoint);

    /**
     * 删除红外相机位点信息
     * 
     * @param id 红外相机位点信息主键
     * @return 结果
     */
    public int deleteBusiCameraPointById(Long id);

    /**
     * 批量删除红外相机位点信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusiCameraPointByIds(Long[] ids);
}
