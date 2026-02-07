package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.FieldworkBorrowing;

/**
 * 外业调查设备借用Service接口
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
public interface IFieldworkBorrowingService 
{
    /**
     * 查询外业调查设备借用
     * 
     * @param id 外业调查设备借用主键
     * @return 外业调查设备借用
     */
    public FieldworkBorrowing selectFieldworkBorrowingById(Long id);

    /**
     * 查询外业调查设备借用列表
     * 
     * @param fieldworkBorrowing 外业调查设备借用
     * @return 外业调查设备借用集合
     */
    public List<FieldworkBorrowing> selectFieldworkBorrowingList(FieldworkBorrowing fieldworkBorrowing);

    /**
     * 新增外业调查设备借用
     * 
     * @param fieldworkBorrowing 外业调查设备借用
     * @return 结果
     */
    public int insertFieldworkBorrowing(FieldworkBorrowing fieldworkBorrowing);

    /**
     * 修改外业调查设备借用
     * 
     * @param fieldworkBorrowing 外业调查设备借用
     * @return 结果
     */
    public int updateFieldworkBorrowing(FieldworkBorrowing fieldworkBorrowing);

    /**
     * 批量删除外业调查设备借用
     * 
     * @param ids 需要删除的外业调查设备借用主键集合
     * @return 结果
     */
    public int deleteFieldworkBorrowingByIds(Long[] ids);

    /**
     * 删除外业调查设备借用信息
     * 
     * @param id 外业调查设备借用主键
     * @return 结果
     */
    public int deleteFieldworkBorrowingById(Long id);
    public int auditFieldworkBorrowing(FieldworkBorrowing fieldworkBorrowing);
}
