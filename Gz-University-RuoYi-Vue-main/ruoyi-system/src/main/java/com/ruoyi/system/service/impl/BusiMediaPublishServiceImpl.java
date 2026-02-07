package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BusiMediaPublishMapper;
import com.ruoyi.system.domain.BusiMediaPublish;
import com.ruoyi.system.service.IBusiMediaPublishService;

/**
 * 自媒体发布Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-09
 */
@Service
public class BusiMediaPublishServiceImpl implements IBusiMediaPublishService 
{
    @Autowired
    private BusiMediaPublishMapper busiMediaPublishMapper;

    /**
     * 查询自媒体发布
     * 
     * @param id 自媒体发布主键
     * @return 自媒体发布
     */
    @Override
    public BusiMediaPublish selectBusiMediaPublishById(Long id)
    {
        return busiMediaPublishMapper.selectBusiMediaPublishById(id);
    }

    /**
     * 查询自媒体发布列表
     * 
     * @param busiMediaPublish 自媒体发布
     * @return 自媒体发布
     */
    @Override
    public List<BusiMediaPublish> selectBusiMediaPublishList(BusiMediaPublish busiMediaPublish)
    {
        return busiMediaPublishMapper.selectBusiMediaPublishList(busiMediaPublish);
    }

    /**
     * 新增自媒体发布
     * 
     * @param busiMediaPublish 自媒体发布
     * @return 结果
     */
    @Override
    public int insertBusiMediaPublish(BusiMediaPublish busiMediaPublish)
    {
        busiMediaPublish.setCreateTime(DateUtils.getNowDate());
        return busiMediaPublishMapper.insertBusiMediaPublish(busiMediaPublish);
    }

    /**
     * 修改自媒体发布
     * 
     * @param busiMediaPublish 自媒体发布
     * @return 结果
     */
    @Override
    public int updateBusiMediaPublish(BusiMediaPublish busiMediaPublish)
    {
        busiMediaPublish.setUpdateTime(DateUtils.getNowDate());
        return busiMediaPublishMapper.updateBusiMediaPublish(busiMediaPublish);
    }

    /**
     * 批量删除自媒体发布
     * 
     * @param ids 需要删除的自媒体发布主键
     * @return 结果
     */
    @Override
    public int deleteBusiMediaPublishByIds(Long[] ids)
    {
        return busiMediaPublishMapper.deleteBusiMediaPublishByIds(ids);
    }

    /**
     * 删除自媒体发布信息
     * 
     * @param id 自媒体发布主键
     * @return 结果
     */
    @Override
    public int deleteBusiMediaPublishById(Long id)
    {
        return busiMediaPublishMapper.deleteBusiMediaPublishById(id);
    }
}
