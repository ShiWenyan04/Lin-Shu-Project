package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BusiMediaPublish;

/**
 * 自媒体发布Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-09
 */
public interface BusiMediaPublishMapper 
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
     * 删除自媒体发布
     * 
     * @param id 自媒体发布主键
     * @return 结果
     */
    public int deleteBusiMediaPublishById(Long id);

    /**
     * 批量删除自媒体发布
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusiMediaPublishByIds(Long[] ids);
}
