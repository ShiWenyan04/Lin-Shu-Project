package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.FieldSurvey;
import com.ruoyi.system.mapper.FieldSurveyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// === 修复点：添加这个导入 ===
import org.springframework.transaction.annotation.Transactional;
// ==========================
import com.ruoyi.system.mapper.FieldworkBorrowingMapper;
import com.ruoyi.system.domain.FieldworkBorrowing;
import com.ruoyi.system.service.IFieldworkBorrowingService;

/**
 * 外业调查设备借用Service业务层处理
 * * @author ruoyi
 * @date 2025-12-18
 */
@Service
public class FieldworkBorrowingServiceImpl implements IFieldworkBorrowingService
{
    @Autowired
    private FieldworkBorrowingMapper fieldworkBorrowingMapper;
    @Autowired
    private FieldSurveyMapper fieldSurveyMapper;

    /**
     * 查询外业调查设备借用
     * * @param id 外业调查设备借用主键
     * @return 外业调查设备借用
     */
    @Override
    public FieldworkBorrowing selectFieldworkBorrowingById(Long id)
    {
        return fieldworkBorrowingMapper.selectFieldworkBorrowingById(id);
    }

    /**
     * 查询外业调查设备借用列表
     * * @param fieldworkBorrowing 外业调查设备借用
     * @return 外业调查设备借用
     */
    @Override
    public List<FieldworkBorrowing> selectFieldworkBorrowingList(FieldworkBorrowing fieldworkBorrowing)
    {
        return fieldworkBorrowingMapper.selectFieldworkBorrowingList(fieldworkBorrowing);
    }

    /**
     * 新增外业调查设备借用
     * * @param fieldworkBorrowing 外业调查设备借用
     * @return 结果
     */
    @Override
    public int insertFieldworkBorrowing(FieldworkBorrowing fieldworkBorrowing)
    {
        fieldworkBorrowing.setCreateTime(DateUtils.getNowDate());
        return fieldworkBorrowingMapper.insertFieldworkBorrowing(fieldworkBorrowing);
    }

    /**
     * 修改外业调查设备借用
     */
    @Override
    public int updateFieldworkBorrowing(FieldworkBorrowing fieldworkBorrowing)
    {
        fieldworkBorrowing.setUpdateTime(DateUtils.getNowDate());
        return fieldworkBorrowingMapper.updateFieldworkBorrowing(fieldworkBorrowing);
    }

    /**
     * 审核逻辑 (新增方法)
     * 必须加事务，保证库存和状态同时更新成功
     */
    @Transactional
    public int auditFieldworkBorrowing(FieldworkBorrowing borrowing) {
        // 1. 如果审核结果是 "1" (通过)
        if ("1".equals(borrowing.getAuditStatus())) {
            // 获取当前库里的借用单详情（为了拿到借用数量和设备ID）
            FieldworkBorrowing oldData = fieldworkBorrowingMapper.selectFieldworkBorrowingById(borrowing.getId());

            // 查询对应的设备库存
            FieldSurvey asset = fieldSurveyMapper.selectFieldSurveyById(oldData.getEquipmentId());
            if (asset == null) {
                throw new ServiceException("关联的设备不存在！");
            }

            // 校验库存是否充足
            if (asset.getAvailableQty() < oldData.getBorrowQty()) {
                throw new ServiceException("设备库存不足，当前可用：" + asset.getAvailableQty());
            }

            // === 核心逻辑：可用减少，外借增加 ===
            asset.setAvailableQty(asset.getAvailableQty() - oldData.getBorrowQty());
            asset.setBorrowedQty(asset.getBorrowedQty() + oldData.getBorrowQty());

            // 更新设备表
            fieldSurveyMapper.updateFieldSurvey(asset);
        }

        // 2. 更新借用单状态
        borrowing.setUpdateTime(DateUtils.getNowDate());
        return fieldworkBorrowingMapper.updateFieldworkBorrowing(borrowing);
    }

    /**
     * 批量删除外业调查设备借用
     * * @param ids 需要删除的外业调查设备借用主键
     * @return 结果
     */
    @Override
    public int deleteFieldworkBorrowingByIds(Long[] ids)
    {
        return fieldworkBorrowingMapper.deleteFieldworkBorrowingByIds(ids);
    }

    /**
     * 删除外业调查设备借用信息
     * * @param id 外业调查设备借用主键
     * @return 结果
     */
    @Override
    public int deleteFieldworkBorrowingById(Long id)
    {
        return fieldworkBorrowingMapper.deleteFieldworkBorrowingById(id);
    }
}