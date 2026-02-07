package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BusiFieldEnd;

/**
 * 外业行程结束Service接口
 * 
 * @author ruoyi
 * @date 2025-12-22
 */
public interface IBusiFieldEndService 
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
    // 2. 新方法：支持 tabType 和 userId
    public List<BusiFieldEnd> selectBusiFieldEndList(BusiFieldEnd busiFieldEnd, String tabType, Long userId);

    // 3. 审核方法
    public int auditBusiFieldEnd(Long endId, String status, String reason);

    // 4. 查询某用户可用的 Start 申请列表 (用于下拉框)
    public List<Object> selectMyAvailableStarts(Long userId);

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
     * 批量删除外业行程结束
     * 
     * @param endIds 需要删除的外业行程结束主键集合
     * @return 结果
     */
    public int deleteBusiFieldEndByEndIds(Long[] endIds);

    /**
     * 删除外业行程结束信息
     * 
     * @param endId 外业行程结束主键
     * @return 结果
     */
    public int deleteBusiFieldEndByEndId(Long endId);
}
