package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BusiCameraResult;

/**
 * 红外相机成果管理Service接口
 * 
 * @author ruoyi
 * @date 2026-01-14
 */
public interface IBusiCameraResultService 
{
    /**
     * 查询红外相机成果管理
     * 
     * @param id 红外相机成果管理主键
     * @return 红外相机成果管理
     */
    public BusiCameraResult selectBusiCameraResultById(Long id);

    /**
     * 查询红外相机成果管理列表
     * 
     * @param busiCameraResult 红外相机成果管理
     * @return 红外相机成果管理集合
     */
    public List<BusiCameraResult> selectBusiCameraResultList(BusiCameraResult busiCameraResult);

    /**
     * 新增红外相机成果管理
     * 
     * @param busiCameraResult 红外相机成果管理
     * @return 结果
     */
    public int insertBusiCameraResult(BusiCameraResult busiCameraResult);

    /**
     * 修改红外相机成果管理
     * 
     * @param busiCameraResult 红外相机成果管理
     * @return 结果
     */
    public int updateBusiCameraResult(BusiCameraResult busiCameraResult);

    /**
     * 批量删除红外相机成果管理
     * 
     * @param ids 需要删除的红外相机成果管理主键集合
     * @return 结果
     */
    public int deleteBusiCameraResultByIds(Long[] ids);

    /**
     * 删除红外相机成果管理信息
     * 
     * @param id 红外相机成果管理主键
     * @return 结果
     */
    public int deleteBusiCameraResultById(Long id);
    /**
     * 审核红外相机成果
     */
    public int auditBusiCameraResult(BusiCameraResult busiCameraResult);
}
