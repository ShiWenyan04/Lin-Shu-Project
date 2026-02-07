package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BusiMediaPublish;

/**
 * 自媒体发布Service接口
 * 
 * @author ruoyi
 * @date 2026-01-09
 */
public interface IBusiMediaPublishService 
{
    /**
     * 查询自媒体发布
     * 
     * @param id 自媒体发布主键
     * @return 自媒体发布
     */
    public BusiMediaPublish selectBusiMediaPublishById(Long id);

    /**
     * 查询自媒体发布列表
     * 
     * @param busiMediaPublish 自媒体发布
     * @return 自媒体发布集合
     */
    public List<BusiMediaPublish> selectBusiMediaPublishList(BusiMediaPublish busiMediaPublish);

    /**
     * 新增自媒体发布
     * 
     * @param busiMediaPublish 自媒体发布
     * @return 结果
     */
    public int insertBusiMediaPublish(BusiMediaPublish busiMediaPublish);

    /**
     * 修改自媒体发布
     * 
     * @param busiMediaPublish 自媒体发布
     * @return 结果
     */
    public int updateBusiMediaPublish(BusiMediaPublish busiMediaPublish);

    /**
     * 批量删除自媒体发布
     * 
     * @param ids 需要删除的自媒体发布主键集合
     * @return 结果
     */
    public int deleteBusiMediaPublishByIds(Long[] ids);

    /**
     * 删除自媒体发布信息
     * 
     * @param id 自媒体发布主键
     * @return 结果
     */
    public int deleteBusiMediaPublishById(Long id);
}
