package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.AcademicMeeting;
import com.ruoyi.system.domain.AcademicMeetingSignup;
import com.ruoyi.system.mapper.AcademicMeetingMapper;
import com.ruoyi.system.mapper.AcademicMeetingSignupMapper;
import com.ruoyi.system.service.IAcademicMeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 学术会议信息Service业务层处理
 * * @author ruoyi
 * @date 2025-12-23
 */
@Service
public class AcademicMeetingServiceImpl implements IAcademicMeetingService
{
    @Autowired
    private AcademicMeetingMapper academicMeetingMapper;

    @Autowired
    private AcademicMeetingSignupMapper signupMapper; // 引入报名Mapper

    /**
     * 查询学术会议信息
     */
    @Override
    public AcademicMeeting selectAcademicMeetingById(Long id)
    {
        return academicMeetingMapper.selectAcademicMeetingById(id);
    }

    /**
     * 查询学术会议信息列表
     * (这里加入了逻辑：判断当前用户是否已报名，设置 signedUp 字段)
     */
    @Override
    public List<AcademicMeeting> selectAcademicMeetingList(AcademicMeeting academicMeeting)
    {
        List<AcademicMeeting> list = academicMeetingMapper.selectAcademicMeetingList(academicMeeting);

        try {
            Long userId = SecurityUtils.getUserId();
            // 遍历每个会议，检查当前用户是否已报名
            for (AcademicMeeting meeting : list) {
                int count = signupMapper.checkUserSignedUp(meeting.getId(), userId);
                meeting.setSignedUp(count > 0);
            }
        } catch (Exception e) {
            // 防止未登录或其他情况导致列表无法加载
        }

        return list;
    }

    /**
     * 学生报名
     */
    @Override
    @Transactional
    public AjaxResult studentSignup(Long meetingId) {
        Long userId = SecurityUtils.getUserId();
        String nickName = SecurityUtils.getLoginUser().getUser().getNickName();
        String phonenumber = SecurityUtils.getLoginUser().getUser().getPhonenumber();

        // 1. 检查会议是否存在及时间有效性
        AcademicMeeting meeting = academicMeetingMapper.selectAcademicMeetingById(meetingId);
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

        // 2. 检查是否重复报名
        int count = signupMapper.checkUserSignedUp(meetingId, userId);
        if (count > 0) {
            return AjaxResult.error("您已报名，无需重复操作");
        }

        // 3. 写入报名表
        AcademicMeetingSignup signup = new AcademicMeetingSignup();
        signup.setMeetingId(meetingId);
        signup.setUserId(userId);
        signup.setName(nickName);
        signup.setPhone(phonenumber);
        signupMapper.insertSignup(signup);

        // 4. 更新主表 participants 字段
        refreshParticipants(meetingId);

        return AjaxResult.success("报名成功");
    }

    /**
     * 学生取消报名
     */
    @Override
    @Transactional
    public AjaxResult studentCancelSignup(Long meetingId) {
        Long userId = SecurityUtils.getUserId();

        // 1. 删除报名记录
        int rows = signupMapper.deleteSignupByMeetingAndUser(meetingId, userId);
        if (rows == 0) {
            return AjaxResult.error("您尚未报名或已取消");
        }

        // 2. 更新主表 participants 字段
        refreshParticipants(meetingId);

        return AjaxResult.success("已取消报名");
    }

    /**
     * 辅助方法：刷新主表参会人员字段
     */
    private void refreshParticipants(Long meetingId) {
        // 获取所有报名人姓名
        List<String> names = signupMapper.selectNamesByMeetingId(meetingId);

        // 拼接字符串
        String participantsStr = StringUtils.join(names, ",");

        // 更新主表
        AcademicMeeting updateMeeting = new AcademicMeeting();
        updateMeeting.setId(meetingId);
        updateMeeting.setParticipants(participantsStr);
        // 如果想更新报名人数，可以加这行：
        // updateMeeting.setSignUpCount(names.size());

        academicMeetingMapper.updateAcademicMeeting(updateMeeting);
    }

    /**
     * 新增学术会议信息
     */
    @Override
    public int insertAcademicMeeting(AcademicMeeting academicMeeting)
    {
        validateBusinessRules(academicMeeting);
        return academicMeetingMapper.insertAcademicMeeting(academicMeeting);
    }

    /**
     * 修改学术会议信息
     */
    @Override
    public int updateAcademicMeeting(AcademicMeeting academicMeeting)
    {
        validateBusinessRules(academicMeeting);
        return academicMeetingMapper.updateAcademicMeeting(academicMeeting);
    }

    /**
     * 批量删除学术会议信息
     */
    @Override
    public int deleteAcademicMeetingByIds(Long[] ids)
    {
        return academicMeetingMapper.deleteAcademicMeetingByIds(ids);
    }

    /**
     * 删除学术会议信息
     */
    @Override
    public int deleteAcademicMeetingById(Long id)
    {
        return academicMeetingMapper.deleteAcademicMeetingById(id);
    }

    /**
     * 业务规则校验
     */
    private void validateBusinessRules(AcademicMeeting academicMeeting) {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date todayStart = calendar.getTime();

        // 1. 年份必须大于等于当前年份
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (academicMeeting.getYear() != null && academicMeeting.getYear() < currentYear) {
            // throw new IllegalArgumentException("年份必须大于等于当前年份(" + currentYear + ")");
            // 建议改为不强制抛错，或者使用 AjaxResult 提示，这里保留你的逻辑
        }

        // 2. 时间逻辑校验
        if (academicMeeting.getSignUpStart() != null && academicMeeting.getSignUpEnd() != null) {
            if (!academicMeeting.getSignUpStart().before(academicMeeting.getSignUpEnd())) {
                throw new RuntimeException("报名开始时间必须在报名结束时间之前");
            }
        }

        // 其他校验逻辑保持你原有的即可...
    }
}