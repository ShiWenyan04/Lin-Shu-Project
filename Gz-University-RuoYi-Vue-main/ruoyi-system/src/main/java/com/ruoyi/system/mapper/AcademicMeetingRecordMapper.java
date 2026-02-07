package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.AcademicMeetingRecord;

/**
 * 参会记录Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-23
 */
public interface AcademicMeetingRecordMapper 
{
    /**
     * 查询参会记录
     * 
     * @param id 参会记录主键
     * @return 参会记录
     */
    public AcademicMeetingRecord selectAcademicMeetingRecordById(Long id);

    /**
     * 查询参会记录列表
     * 
     * @param academicMeetingRecord 参会记录
     * @return 参会记录集合
     */
    public List<AcademicMeetingRecord> selectAcademicMeetingRecordList(AcademicMeetingRecord academicMeetingRecord);

    /**
     * 新增参会记录
     * 
     * @param academicMeetingRecord 参会记录
     * @return 结果
     */
    public int insertAcademicMeetingRecord(AcademicMeetingRecord academicMeetingRecord);

    /**
     * 修改参会记录
     * 
     * @param academicMeetingRecord 参会记录
     * @return 结果
     */
    public int updateAcademicMeetingRecord(AcademicMeetingRecord academicMeetingRecord);

    /**
     * 删除参会记录
     * 
     * @param id 参会记录主键
     * @return 结果
     */
    public int deleteAcademicMeetingRecordById(Long id);

    /**
     * 批量删除参会记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAcademicMeetingRecordByIds(Long[] ids);
    /**
     * 审核参会记录（通过/驳回）
     *
     * @param params 参数Map，包含id, auditStatus, auditReason
     * @return 结果
     */
    public int auditAcademicMeetingRecord(Map<String, Object> params);


}
