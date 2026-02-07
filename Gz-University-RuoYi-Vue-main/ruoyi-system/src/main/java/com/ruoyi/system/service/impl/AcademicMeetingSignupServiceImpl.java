package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.AcademicMeeting;
import com.ruoyi.system.domain.AcademicMeetingSignup;
import com.ruoyi.system.mapper.AcademicMeetingMapper;
import com.ruoyi.system.mapper.AcademicMeetingSignupMapper;
import com.ruoyi.system.service.IAcademicMeetingSignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 学术会议报名Service实现
 * @author ruoyi
 * @date 2025-12-24
 */
@Service
public class AcademicMeetingSignupServiceImpl implements IAcademicMeetingSignupService {

    @Autowired
    private AcademicMeetingSignupMapper signupMapper;

    @Autowired
    private AcademicMeetingMapper meetingMapper;

    /**
     * 通用新增报名（如果保留原有逻辑的话）
     */
    @Override
    @Transactional
    public AjaxResult addSignup(AcademicMeetingSignup signup) {
        // 1. 业务校验略...
        signupMapper.insertSignup(signup);
        // 刷新主表名单
        refreshMeetingParticipants(signup.getMeetingId());
        return AjaxResult.success("报名成功");
    }

    /**
     * 查询会议报名列表
     */
    @Override
    public List<AcademicMeetingSignup> getSignupListByMeetingId(Long meetingId) {
        return signupMapper.selectSignupListByMeetingId(meetingId);
    }

    /**
     * 学生自助报名
     */
    @Override
    @Transactional
    public AjaxResult studentSignup(Long meetingId) {
        // 1. 获取当前登录用户信息
        Long userId = SecurityUtils.getUserId();
        String nickName = SecurityUtils.getLoginUser().getUser().getNickName();
        String phonenumber = SecurityUtils.getLoginUser().getUser().getPhonenumber();

        // 2. 校验会议是否存在及时间
        AcademicMeeting meeting = meetingMapper.selectAcademicMeetingById(meetingId);
        if (meeting == null) {
            return AjaxResult.error("会议不存在");
        }
        Date now = new Date();
        if (meeting.getSignUpStart() != null && now.before(meeting.getSignUpStart())) {
            return AjaxResult.error("报名尚未开始");
        }
        if (meeting.getSignUpEnd() != null && now.after(meeting.getSignUpEnd())) {
            return AjaxResult.error("报名已结束");
        }

        // 3. 校验是否重复报名
        int count = signupMapper.checkUserSignedUp(meetingId, userId);
        if (count > 0) {
            return AjaxResult.error("您已报名该会议，无需重复提交");
        }

        // 4. 插入报名记录
        AcademicMeetingSignup signup = new AcademicMeetingSignup();
        signup.setMeetingId(meetingId);
        signup.setUserId(userId);
        signup.setName(nickName); // 自动填入名字
        signup.setPhone(phonenumber); // 自动填入手机
        signup.setSignupTime(new Date());

        signupMapper.insertSignup(signup);

        // 5. 同步更新主表的 participants 字段
        refreshMeetingParticipants(meetingId);

        return AjaxResult.success("报名成功");
    }

    /**
     * 学生取消报名
     */
    @Override
    @Transactional
    public AjaxResult studentCancelSignup(Long meetingId) {
        Long userId = SecurityUtils.getUserId();

        // 1. 删除记录
        int rows = signupMapper.deleteSignupByMeetingAndUser(meetingId, userId);
        if (rows == 0) {
            return AjaxResult.error("您尚未报名或已取消");
        }

        // 2. 同步更新主表的 participants 字段
        refreshMeetingParticipants(meetingId);

        return AjaxResult.success("已取消报名");
    }

    /**
     * 辅助方法：刷新主表参会人员字符串
     */
    private void refreshMeetingParticipants(Long meetingId) {
        // 获取所有报名人姓名
        List<String> names = signupMapper.selectNamesByMeetingId(meetingId);

        // 拼接字符串，例如 "张三,李四"
        String participantsStr = StringUtils.join(names, ",");

        // 更新主表
        AcademicMeeting meeting = new AcademicMeeting();
        meeting.setId(meetingId);
        meeting.setParticipants(participantsStr);
        // 如果主表有报名人数统计字段，也可以在这里更新
        // meeting.setSignUpCount(names.size());

        meetingMapper.updateAcademicMeeting(meeting);
    }
}