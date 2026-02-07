package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BusiCameraInstall;

/**
 * 红外相机安装项目Service接口
 * 
 * @author ruoyi
 * @date 2026-01-12
 */
public interface IBusiCameraInstallService 
{
    /**
     * 查询红外相机安装项目
     * 
     * @param id 红外相机安装项目主键
     * @return 红外相机安装项目
     */
    public BusiCameraInstall selectBusiCameraInstallById(Long id);

    /**
     * 查询红外相机安装项目列表
     * 
     * @param busiCameraInstall 红外相机安装项目
     * @return 红外相机安装项目集合
     */
    public List<BusiCameraInstall> selectBusiCameraInstallList(BusiCameraInstall busiCameraInstall);

    /**
     * 新增红外相机安装项目
     * 
     * @param busiCameraInstall 红外相机安装项目
     * @return 结果
     */
    public int insertBusiCameraInstall(BusiCameraInstall busiCameraInstall);

    /**
     * 修改红外相机安装项目
     * 
     * @param busiCameraInstall 红外相机安装项目
     * @return 结果
     */
    public int updateBusiCameraInstall(BusiCameraInstall busiCameraInstall);

    /**
     * 批量删除红外相机安装项目
     * 
     * @param ids 需要删除的红外相机安装项目主键集合
     * @return 结果
     */
    public int deleteBusiCameraInstallByIds(Long[] ids);

    /**
     * 删除红外相机安装项目信息
     * 
     * @param id 红外相机安装项目主键
     * @return 结果
     */
    public int deleteBusiCameraInstallById(Long id);

    /**
     * 【新增】审核红外相机安装项目
     * * @param busiCameraInstall 包含 id, auditStatus, auditReason
     * @return 结果
     */
    public int auditBusiCameraInstall(BusiCameraInstall busiCameraInstall);
}
