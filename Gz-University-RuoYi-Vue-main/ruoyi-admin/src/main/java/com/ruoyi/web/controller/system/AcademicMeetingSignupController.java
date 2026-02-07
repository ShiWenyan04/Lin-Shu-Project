package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.AcademicMeetingSignup;
import com.ruoyi.system.service.IAcademicMeetingSignupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 学术会议报名Controller
 * @author ruoyi
 * @date 2025-12-24
 */
@RestController
@RequestMapping("/system/meeting/signup")
public class AcademicMeetingSignupController extends BaseController {

    @Autowired
    private IAcademicMeetingSignupService signupService;

    /**
     * 查询会议报名列表
     * 修改点：只有 老师、本模块负责人、管理员 可以查看名单
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:checkSignup') || @ss.hasRole('teacher') || @ss.hasRole('academicmeeting_manager')")
    @GetMapping("/list/{meetingId}")
    public TableDataInfo getSignupList(@PathVariable Long meetingId) {
        List<AcademicMeetingSignup> list = signupService.getSignupListByMeetingId(meetingId);
        return getDataTable(list);
    }

    /**
     * 学生点击报名
     */
    @PreAuthorize("@ss.hasRole('student') || @ss.hasRole('academicmeeting_manager') || @ss.hasRole('admin')")
    @PostMapping("/student/{meetingId}")
    public AjaxResult studentSignup(@PathVariable Long meetingId) {
        return signupService.studentSignup(meetingId);
    }

    /**
     * 学生/负责人 点击取消报名
     */
    @PreAuthorize("@ss.hasRole('student') || @ss.hasRole('academicmeeting_manager') || @ss.hasRole('admin')")
    @PostMapping("/cancel/{meetingId}")
    public AjaxResult cancelSignup(@PathVariable Long meetingId) {
        return signupService.studentCancelSignup(meetingId);
    }
}