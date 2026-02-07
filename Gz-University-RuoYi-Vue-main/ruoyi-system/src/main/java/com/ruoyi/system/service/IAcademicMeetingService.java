package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.AcademicMeeting;
import java.util.List;

/**
 * 学术会议信息Service接口
 * * @author ruoyi
 * @date 2025-12-23
 */
public interface IAcademicMeetingService
{
    /**
     * 查询学术会议信息
     */
    public AcademicMeeting selectAcademicMeetingById(Long id);

    /**
     * 查询学术会议信息列表
     */
    public List<AcademicMeeting> selectAcademicMeetingList(AcademicMeeting academicMeeting);

    /**
     * 新增学术会议信息
     */
    public int insertAcademicMeeting(AcademicMeeting academicMeeting);

    /**
     * 修改学术会议信息
     */
    public int updateAcademicMeeting(AcademicMeeting academicMeeting);

    /**
     * 批量删除学术会议信息
     */
    public int deleteAcademicMeetingByIds(Long[] ids);

    /**
     * 删除学术会议信息信息
     */
    public int deleteAcademicMeetingById(Long id);

    /**
     * 学生报名
     */
    public AjaxResult studentSignup(Long meetingId);

    /**
     * 学生取消报名
     */
    public AjaxResult studentCancelSignup(Long meetingId);
}