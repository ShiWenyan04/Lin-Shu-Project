package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Map;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils; // 必须引入，用于获取当前用户
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.AcademicMeetingRecordMapper;
import com.ruoyi.system.domain.AcademicMeetingRecord;
import com.ruoyi.system.service.IAcademicMeetingRecordService;

/**
 * 参会记录Service业务层处理
 *
 * @author ruoyi
 * @date 2025-12-23
 */
@Service
public class AcademicMeetingRecordServiceImpl implements IAcademicMeetingRecordService
{
    @Autowired
    private AcademicMeetingRecordMapper academicMeetingRecordMapper;

    /**
     * 审核参会记录
     */
    @Override
    public int auditAcademicMeetingRecord(Map<String, Object> params) {
        // 调用Mapper中定义的 auditAcademicMeetingRecord 方法
        return academicMeetingRecordMapper.auditAcademicMeetingRecord(params);
    }

    /**
     * 查询参会记录
     */
    @Override
    public AcademicMeetingRecord selectAcademicMeetingRecordById(Long id)
    {
        return academicMeetingRecordMapper.selectAcademicMeetingRecordById(id);
    }

    /**
     * 查询参会记录列表
     */
    @Override
    public List<AcademicMeetingRecord> selectAcademicMeetingRecordList(AcademicMeetingRecord academicMeetingRecord)
    {
        return academicMeetingRecordMapper.selectAcademicMeetingRecordList(academicMeetingRecord);
    }

    /**
     * 新增参会记录
     * * 【重点修改】：自动填入创建人和创建时间
     */
    @Override
    public int insertAcademicMeetingRecord(AcademicMeetingRecord academicMeetingRecord)
    {
        // 1. 获取当前登录用户名
        String username = SecurityUtils.getUsername();
        academicMeetingRecord.setCreateBy(username);

        // 2. 设置创建时间
        academicMeetingRecord.setCreateTime(DateUtils.getNowDate());

        // 3. 默认审核状态为 0 (待审核)，防止前端不传
        if (academicMeetingRecord.getAuditStatus() == null) {
            academicMeetingRecord.setAuditStatus("0");
        }

        return academicMeetingRecordMapper.insertAcademicMeetingRecord(academicMeetingRecord);
    }

    /**
     * 修改参会记录
     */
    @Override
    public int updateAcademicMeetingRecord(AcademicMeetingRecord academicMeetingRecord)
    {
        String username = SecurityUtils.getUsername();
        academicMeetingRecord.setUpdateBy(username);
        academicMeetingRecord.setUpdateTime(DateUtils.getNowDate());

        // 【修改点】：权限判断
        // 只有非（老师/参会记录负责人/管理员）修改时，才重置为 '0'
        if (!SecurityUtils.hasRole("teacher")
                && !SecurityUtils.hasRole("meetingrecord_manager")
                && !SecurityUtils.hasRole("admin")) {
            academicMeetingRecord.setAuditStatus("0");
        }

        // 如果是重置状态，通常也清空驳回原因；如果保留状态，则不清理
        if ("0".equals(academicMeetingRecord.getAuditStatus())) {
            academicMeetingRecord.setAuditReason("");
        }

        return academicMeetingRecordMapper.updateAcademicMeetingRecord(academicMeetingRecord);
    }

    /**
     * 批量删除参会记录
     */
    @Override
    public int deleteAcademicMeetingRecordByIds(Long[] ids)
    {
        return academicMeetingRecordMapper.deleteAcademicMeetingRecordByIds(ids);
    }

    /**
     * 删除参会记录信息
     */
    @Override
    public int deleteAcademicMeetingRecordById(Long id)
    {
        return academicMeetingRecordMapper.deleteAcademicMeetingRecordById(id);
    }
}