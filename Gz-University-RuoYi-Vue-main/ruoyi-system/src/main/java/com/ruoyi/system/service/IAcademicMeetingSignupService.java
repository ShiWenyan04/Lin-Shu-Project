package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.AcademicMeetingSignup;
import java.util.List;

/**
 * 学术会议报名Service接口
 * @author ruoyi
 * @date 2025-12-24
 */
public interface IAcademicMeetingSignupService {
    /**
     * 新增会议报名 (管理员手动添加或其他通用用途)
     */
    AjaxResult addSignup(AcademicMeetingSignup signup);

    /**
     * 根据会议ID查询报名列表
     */
    List<AcademicMeetingSignup> getSignupListByMeetingId(Long meetingId);

    /**
     * 学生自助报名
     */
    AjaxResult studentSignup(Long meetingId);

    /**
     * 学生取消报名
     */
    AjaxResult studentCancelSignup(Long meetingId);
}