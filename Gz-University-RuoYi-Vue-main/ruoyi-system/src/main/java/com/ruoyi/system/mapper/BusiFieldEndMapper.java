package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BusiFieldEnd;

/**
 * 外业行程结束Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-22
 */
public interface BusiFieldEndMapper 
{
    /**
     * 查询外业行程结束
     * 
     * @param endId 外业行程结束主键
     * @return 外业行程结束
     */
    public BusiFieldEnd selectBusiFieldEndByEndId(Long endId);

    /**
     * 查询外业行程结束列表
     * 
     * @param busiFieldEnd 外业行程结束
     * @return 外业行程结束集合
     */
    public List<BusiFieldEnd> selectBusiFieldEndList(BusiFieldEnd busiFieldEnd);

    /**
     * 新增外业行程结束
     * 
     * @param busiFieldEnd 外业行程结束
     * @return 结果
     */
    public int insertBusiFieldEnd(BusiFieldEnd busiFieldEnd);

    /**
     * 修改外业行程结束
     * 
     * @param busiFieldEnd 外业行程结束
     * @return 结果
     */
    public int updateBusiFieldEnd(BusiFieldEnd busiFieldEnd);

    /**
     * 删除外业行程结束
     * 
     * @param endId 外业行程结束主键
     * @return 结果
     */
    public int deleteBusiFieldEndByEndId(Long endId);

    /**
     * 批量删除外业行程结束
     * 
     * @param endIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusiFieldEndByEndIds(Long[] endIds);
}
