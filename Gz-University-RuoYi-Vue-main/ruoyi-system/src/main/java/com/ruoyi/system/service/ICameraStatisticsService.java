package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.CameraStatistics;

/**
 * 红外相机数据Service接口
 * 
 * @author ruoyi
 * @date 2025-07-26
 */
public interface ICameraStatisticsService 
{
    /**
     * 查询红外相机数据
     * 
     * @param id 红外相机数据主键
     * @return 红外相机数据
     */
    public CameraStatistics selectCameraStatisticsById(Long id);

    /**
     * 查询红外相机数据列表
     * 
     * @param cameraStatistics 红外相机数据
     * @return 红外相机数据集合
     */
    public List<CameraStatistics> selectCameraStatisticsList(CameraStatistics cameraStatistics);

    /**
     * 新增红外相机数据
     * 
     * @param cameraStatistics 红外相机数据
     * @return 结果
     */
    public int insertCameraStatistics(CameraStatistics cameraStatistics);

    /**
     * 修改红外相机数据
     * 
     * @param cameraStatistics 红外相机数据
     * @return 结果
     */
    public int updateCameraStatistics(CameraStatistics cameraStatistics);

    /**
     * 批量删除红外相机数据
     * 
     * @param ids 需要删除的红外相机数据主键集合
     * @return 结果
     */
    public int deleteCameraStatisticsByIds(Long[] ids);

    /**
     * 删除红外相机数据信息
     * 
     * @param id 红外相机数据主键
     * @return 结果
     */
    public int deleteCameraStatisticsById(Long id);
    /**
     * 导入红外相机数据
     *
     * @param statisticsList 红外相机数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否进行更新
     * @return 结果
     */
    String importCameraStatistics(List<CameraStatistics> statisticsList, boolean updateSupport);
}
