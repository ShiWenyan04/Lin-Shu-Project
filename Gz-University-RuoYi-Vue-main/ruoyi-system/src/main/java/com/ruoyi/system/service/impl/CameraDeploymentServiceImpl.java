package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.CameraDeploymentMapper;
import com.ruoyi.system.domain.CameraDeployment;
import com.ruoyi.system.service.ICameraDeploymentService;

/**
 * 红外相机布设管理Service业务层处理
 *
 * @author ruoyi
 * @date 2025-07-21
 */
@Service
public class CameraDeploymentServiceImpl implements ICameraDeploymentService
{
    @Autowired
    private CameraDeploymentMapper cameraDeploymentMapper;

    /**
     * 查询红外相机布设管理
     *
     * @param id 红外相机布设管理主键
     * @return 红外相机布设管理
     */
    @Override
    public CameraDeployment selectCameraDeploymentById(Long id)
    {
        return cameraDeploymentMapper.selectCameraDeploymentById(id);
    }

    /**
     * 查询红外相机布设管理列表
     *
     * @param cameraDeployment 红外相机布设管理
     * @return 红外相机布设管理
     */
    @Override
    public List<CameraDeployment> selectCameraDeploymentList(CameraDeployment cameraDeployment)
    {
        // 直接调用Mapper，参数处理在Controller中完成
        return cameraDeploymentMapper.selectCameraDeploymentList(cameraDeployment);
    }

    /**
     * 新增红外相机布设管理
     *
     * @param cameraDeployment 红外相机布设管理
     * @return 结果
     */
    @Override
    public int insertCameraDeployment(CameraDeployment cameraDeployment)
    {
        cameraDeployment.setCreatedTime(DateUtils.getNowDate());
        cameraDeployment.setCreatedBy(SecurityUtils.getUsername());
        return cameraDeploymentMapper.insertCameraDeployment(cameraDeployment);
    }

    /**
     * 修改红外相机布设管理
     *
     * @param cameraDeployment 红外相机布设管理
     * @return 结果
     */
    @Override
    public int updateCameraDeployment(CameraDeployment cameraDeployment)
    {
        return cameraDeploymentMapper.updateCameraDeployment(cameraDeployment);
    }

    /**
     * 批量删除红外相机布设管理
     *
     * @param ids 需要删除的红外相机布设管理主键
     * @return 结果
     */
    @Override
    public int deleteCameraDeploymentByIds(Long[] ids)
    {
        return cameraDeploymentMapper.deleteCameraDeploymentByIds(ids);
    }

    /**
     * 删除红外相机布设管理信息
     *
     * @param id 红外相机布设管理主键
     * @return 结果
     */
    @Override
    public int deleteCameraDeploymentById(Long id)
    {
        return cameraDeploymentMapper.deleteCameraDeploymentById(id);
    }
}