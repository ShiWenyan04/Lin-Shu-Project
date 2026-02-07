package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.CameraReturn;

/**
 * 红外相机归还Service接口
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
public interface ICameraReturnService 
{
    /**
     * 查询红外相机归还
     * 
     * @param id 红外相机归还主键
     * @return 红外相机归还
     */
    public CameraReturn selectCameraReturnById(Long id);

    /**
     * 查询红外相机归还列表
     * 
     * @param cameraReturn 红外相机归还
     * @return 红外相机归还集合
     */
    public List<CameraReturn> selectCameraReturnList(CameraReturn cameraReturn);

    /**
     * 新增红外相机归还
     * 
     * @param cameraReturn 红外相机归还
     * @return 结果
     */
    public int insertCameraReturn(CameraReturn cameraReturn);

    /**
     * 修改红外相机归还
     * 
     * @param cameraReturn 红外相机归还
     * @return 结果
     */
    public int updateCameraReturn(CameraReturn cameraReturn);

    /**
     * 批量删除红外相机归还
     * 
     * @param ids 需要删除的红外相机归还主键集合
     * @return 结果
     */
    public int deleteCameraReturnByIds(Long[] ids);

    /**
     * 删除红外相机归还信息
     * 
     * @param id 红外相机归还主键
     * @return 结果
     */
    public int deleteCameraReturnById(Long id);

    /**
            * 审核红外相机归还与运维 (新增)
     * * @param cameraReturn 红外相机归还与运维
     * @return 结果
     */
    public int auditCameraReturn(CameraReturn cameraReturn);
}
