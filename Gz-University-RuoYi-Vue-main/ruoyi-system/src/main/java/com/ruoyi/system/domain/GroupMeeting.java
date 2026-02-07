package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 大组会安排对象 group_meeting
 * 
 * @author ruoyi
 * @date 2026-01-12
 */
public class GroupMeeting extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序 */
    private Long id;

    /** 计划时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date scheduledTime;

    /** 地点 */
    @Excel(name = "地点")
    private String location;

    /** 组会负责人 */
    @Excel(name = "组会负责人")
    private String responsiblePerson;

    /** 会议安排（存储上传的文档路径或标识） */
    @Excel(name = "会议安排", readConverterExp = "存=储上传的文档路径或标识")
    private String meetingDocument;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setScheduledTime(Date scheduledTime) 
    {
        this.scheduledTime = scheduledTime;
    }

    public Date getScheduledTime() 
    {
        return scheduledTime;
    }

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }

    public void setResponsiblePerson(String responsiblePerson) 
    {
        this.responsiblePerson = responsiblePerson;
    }

    public String getResponsiblePerson() 
    {
        return responsiblePerson;
    }

    public void setMeetingDocument(String meetingDocument) 
    {
        this.meetingDocument = meetingDocument;
    }

    public String getMeetingDocument() 
    {
        return meetingDocument;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("scheduledTime", getScheduledTime())
            .append("location", getLocation())
            .append("responsiblePerson", getResponsiblePerson())
            .append("meetingDocument", getMeetingDocument())
            .toString();
    }
}
