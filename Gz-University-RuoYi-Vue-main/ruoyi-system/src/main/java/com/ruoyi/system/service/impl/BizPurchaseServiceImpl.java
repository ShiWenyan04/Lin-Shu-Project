package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BizPurchaseMapper;
import com.ruoyi.system.domain.BizPurchase;
import com.ruoyi.system.service.IBizPurchaseService;

/**
 * 购买支出信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-17
 */
@Service
public class BizPurchaseServiceImpl implements IBizPurchaseService 
{
    @Autowired
    private BizPurchaseMapper bizPurchaseMapper;

    /**
     * 查询购买支出信息
     * 
     * @param id 购买支出信息主键
     * @return 购买支出信息
     */
    @Override
    public BizPurchase selectBizPurchaseById(Long id)
    {
        return bizPurchaseMapper.selectBizPurchaseById(id);
    }

    /**
     * 查询购买支出信息列表
     * 
     * @param bizPurchase 购买支出信息
     * @return 购买支出信息
     */
    @Override
    public List<BizPurchase> selectBizPurchaseList(BizPurchase bizPurchase)
    {
        return bizPurchaseMapper.selectBizPurchaseList(bizPurchase);
    }

    /**
     * 新增购买支出信息
     * 
     * @param bizPurchase 购买支出信息
     * @return 结果
     */
    @Override
    public int insertBizPurchase(BizPurchase bizPurchase)
    {
        bizPurchase.setCreateTime(DateUtils.getNowDate());
        return bizPurchaseMapper.insertBizPurchase(bizPurchase);
    }

    /**
     * 修改购买支出信息
     * 
     * @param bizPurchase 购买支出信息
     * @return 结果
     */
    @Override
    public int updateBizPurchase(BizPurchase bizPurchase)
    {
        bizPurchase.setUpdateTime(DateUtils.getNowDate());
        return bizPurchaseMapper.updateBizPurchase(bizPurchase);
    }

    /**
     * 批量删除购买支出信息
     * 
     * @param ids 需要删除的购买支出信息主键
     * @return 结果
     */
    @Override
    public int deleteBizPurchaseByIds(Long[] ids)
    {
        return bizPurchaseMapper.deleteBizPurchaseByIds(ids);
    }

    /**
     * 删除购买支出信息信息
     * 
     * @param id 购买支出信息主键
     * @return 结果
     */
    @Override
    public int deleteBizPurchaseById(Long id)
    {
        return bizPurchaseMapper.deleteBizPurchaseById(id);
    }
}
