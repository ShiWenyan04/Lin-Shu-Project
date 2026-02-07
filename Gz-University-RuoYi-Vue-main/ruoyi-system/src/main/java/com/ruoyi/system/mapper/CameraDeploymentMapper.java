package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.CameraDeployment;

/**
 * 红外相机布设管理Mapper接口
 * 
 * @author ruoyi
 * @date 2025-07-21
 */
public interface CameraDeploymentMapper 
{
    /**
     * 查询红外相机布设管理
     * 
     * @param id 红外相机布设管理主键
     * @return 红外相机布设管理
     */
    public CameraDeployment selectCameraDeploymentById(Long id);

    /**
     * 查询红外相机布设管理列表
     * 
     * @param cameraDeployment 红外相机布设管理
     * @return 红外相机布设管理集合
     */
    public List<CameraDeployment> selectCameraDeploymentList(CameraDeployment cameraDeployment);

    /**
     * 新增红外相机布设管理
     * 
     * @param cameraDeployment 红外相机布设管理
     * @return 结果
     */
    public int insertCameraDeployment(CameraDeployment cameraDeployment);

    /**
     * 修改红外相机布设管理
     * 
     * @param cameraDeployment 红外相机布设管理
     * @return 结果
     */
    public int updateCameraDeployment(CameraDeployment cameraDeployment);

    /**
     * 删除红外相机布设管理
     * 
     * @param id 红外相机布设管理主键
     * @return 结果
     */
    public int deleteCameraDeploymentById(Long id);

    /**
     * 批量删除红外相机布设管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCameraDeploymentByIds(Long[] ids);
}
