package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 学术会议信息对象 academic_meeting
 * * @author ruoyi
 * @date 2025-12-23
 */
public class AcademicMeeting extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序 */
    @Excel(name = "序")
    private Long id;

    /** 年份 */
    @Excel(name = "年份")
    private Long year;

    /** 会议名称 */
    @Excel(name = "会议名称")
    private String meetingName;

    /** 举办单位 */
    @Excel(name = "举办单位")
    private String hostUnit;

    /** 召开时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "召开时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date holdTime;

    /** 地点 */
    @Excel(name = "地点")
    private String location;

    /** 会议通知 */
    @Excel(name = "会议通知")
    private String meetingNotice;

    /** 报名开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报名开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date signUpStart;

    /** 报名结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报名结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date signUpEnd;

    /** 注册费用 */
    @Excel(name = "注册费用")
    private BigDecimal registerFee;

    /** 参会人员 */
    @Excel(name = "参会人员")
    private String participants;

    /** 汇报人 */
    @Excel(name = "汇报人")
    private String reporter;

    /** 报名人数 */
    @Excel(name = "报名人数")
    private Integer signUpCount;

    // ================= 新增字段 =================
    /** 当前登录用户是否已报名 (非数据库字段) */
    private boolean signedUp;

    public boolean isSignedUp() {
        return signedUp;
    }

    public void setSignedUp(boolean signedUp) {
        this.signedUp = signedUp;
    }
    // ===========================================

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setYear(Long year)
    {
        this.year = year;
    }

    public Long getYear()
    {
        return year;
    }

    public void setMeetingName(String meetingName)
    {
        this.meetingName = meetingName;
    }

    public String getMeetingName()
    {
        return meetingName;
    }

    public void setHostUnit(String hostUnit)
    {
        this.hostUnit = hostUnit;
    }

    public String getHostUnit()
    {
        return hostUnit;
    }

    public void setHoldTime(Date holdTime)
    {
        this.holdTime = holdTime;
    }

    public Date getHoldTime()
    {
        return holdTime;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getLocation()
    {
        return location;
    }

    public void setMeetingNotice(String meetingNotice)
    {
        this.meetingNotice = meetingNotice;
    }

    public String getMeetingNotice()
    {
        return meetingNotice;
    }

    public void setSignUpStart(Date signUpStart)
    {
        this.signUpStart = signUpStart;
    }

    public Date getSignUpStart()
    {
        return signUpStart;
    }

    public void setSignUpEnd(Date signUpEnd)
    {
        this.signUpEnd = signUpEnd;
    }

    public Date getSignUpEnd()
    {
        return signUpEnd;
    }

    public void setRegisterFee(BigDecimal registerFee)
    {
        this.registerFee = registerFee;
    }

    public BigDecimal getRegisterFee()
    {
        return registerFee;
    }

    public void setParticipants(String participants)
    {
        this.participants = participants;
    }

    public String getParticipants()
    {
        return participants;
    }

    public void setReporter(String reporter)
    {
        this.reporter = reporter;
    }

    public String getReporter()
    {
        return reporter;
    }

    public Integer getSignUpCount() {
        return signUpCount;
    }

    public void setSignUpCount(Integer signUpCount) {
        this.signUpCount = signUpCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("year", getYear())
                .append("meetingName", getMeetingName())
                .append("hostUnit", getHostUnit())
                .append("holdTime", getHoldTime())
                .append("location", getLocation())
                .append("meetingNotice", getMeetingNotice())
                .append("signUpStart", getSignUpStart())
                .append("signUpEnd", getSignUpEnd())
                .append("registerFee", getRegisterFee())
                .append("participants", getParticipants())
                .append("reporter", getReporter())
                .append("signUpCount", getSignUpCount())
                .append("signedUp", isSignedUp())
                .toString();
    }
}