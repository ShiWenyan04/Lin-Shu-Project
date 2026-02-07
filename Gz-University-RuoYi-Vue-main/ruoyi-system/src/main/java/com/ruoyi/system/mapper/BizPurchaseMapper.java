package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizPurchase;

/**
 * 购买支出信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-17
 */
public interface BizPurchaseMapper 
{
    /**
     * 查询购买支出信息
     * 
     * @param id 购买支出信息主键
     * @return 购买支出信息
     */
    public BizPurchase selectBizPurchaseById(Long id);

    /**
     * 查询购买支出信息列表
     * 
     * @param bizPurchase 购买支出信息
     * @return 购买支出信息集合
     */
    public List<BizPurchase> selectBizPurchaseList(BizPurchase bizPurchase);

    /**
     * 新增购买支出信息
     * 
     * @param bizPurchase 购买支出信息
     * @return 结果
     */
    public int insertBizPurchase(BizPurchase bizPurchase);

    /**
     * 修改购买支出信息
     * 
     * @param bizPurchase 购买支出信息
     * @return 结果
     */
    public int updateBizPurchase(BizPurchase bizPurchase);

    /**
     * 删除购买支出信息
     * 
     * @param id 购买支出信息主键
     * @return 结果
     */
    public int deleteBizPurchaseById(Long id);

    /**
     * 批量删除购买支出信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizPurchaseByIds(Long[] ids);
}
