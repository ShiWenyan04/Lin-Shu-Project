package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 大组会实施记录页对象 group_meeting_record
 * 
 * @author ruoyi
 * @date 2026-01-10
 */
public class GroupMeetingRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Excel(name = "主键ID")
    private Long id;

    /** 会议时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "会议时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date meetingTime;

    /** 会议地点 */
    @Excel(name = "会议地点")
    private String meetingPlace;

    /** 主持人 */
    @Excel(name = "主持人")
    private String hostPerson;

    /** 参加人员 */
    @Excel(name = "参加人员")
    private String participants;

    /** 缺席人员 */
    @Excel(name = "缺席人员")
    private String absentees;

    /** 缺席原因 */
    @Excel(name = "缺席原因")
    private String absentReason;

    /** 图片上传地址 */
    @Excel(name = "图片上传地址")
    private String imageUrls;

    /** 文件上传地址 */
    @Excel(name = "文件上传地址")
    private String fileUrls;

    /** 审核状态(0-待审核 1-通过 2-驳回) */
    @Excel(name = "审核状态(0-待审核 1-通过 2-驳回)")
    private String auditStatus;

    /** 驳回原因 */
    @Excel(name = "驳回原因")
    private String auditReason;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setMeetingTime(Date meetingTime) 
    {
        this.meetingTime = meetingTime;
    }

    public Date getMeetingTime() 
    {
        return meetingTime;
    }

    public void setMeetingPlace(String meetingPlace) 
    {
        this.meetingPlace = meetingPlace;
    }

    public String getMeetingPlace() 
    {
        return meetingPlace;
    }

    public void setHostPerson(String hostPerson) 
    {
        this.hostPerson = hostPerson;
    }

    public String getHostPerson() 
    {
        return hostPerson;
    }

    public void setParticipants(String participants) 
    {
        this.participants = participants;
    }

    public String getParticipants() 
    {
        return participants;
    }

    public void setAbsentees(String absentees) 
    {
        this.absentees = absentees;
    }

    public String getAbsentees() 
    {
        return absentees;
    }

    public void setAbsentReason(String absentReason) 
    {
        this.absentReason = absentReason;
    }

    public String getAbsentReason() 
    {
        return absentReason;
    }

    public void setImageUrls(String imageUrls) 
    {
        this.imageUrls = imageUrls;
    }

    public String getImageUrls() 
    {
        return imageUrls;
    }

    public void setFileUrls(String fileUrls) 
    {
        this.fileUrls = fileUrls;
    }

    public String getFileUrls() 
    {
        return fileUrls;
    }

    public void setAuditStatus(String auditStatus) 
    {
        this.auditStatus = auditStatus;
    }

    public String getAuditStatus() 
    {
        return auditStatus;
    }

    public void setAuditReason(String auditReason) 
    {
        this.auditReason = auditReason;
    }

    public String getAuditReason() 
    {
        return auditReason;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("meetingTime", getMeetingTime())
            .append("meetingPlace", getMeetingPlace())
            .append("hostPerson", getHostPerson())
            .append("participants", getParticipants())
            .append("absentees", getAbsentees())
            .append("absentReason", getAbsentReason())
            .append("imageUrls", getImageUrls())
            .append("fileUrls", getFileUrls())
            .append("auditStatus", getAuditStatus())
            .append("auditReason", getAuditReason())
            .toString();
    }
}
