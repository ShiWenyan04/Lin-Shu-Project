package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 参会记录对象 academic_meeting_record
 * 
 * @author ruoyi
 * @date 2025-12-23
 */
public class AcademicMeetingRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序 */
    private Long id;

    /** 会议名称 */
    @Excel(name = "会议名称")
    private String meetingName;

    /** 举办单位 */
    @Excel(name = "举办单位")
    private String hostUnit;

    /** 参会人员 */
    @Excel(name = "参会人员")
    private String participants;

    /** 出发日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出发日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date departureDate;

    /** 返校日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "返校日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date returnDate;

    /** 汇报人 */
    @Excel(name = "汇报人")
    private String reporter;

    /** 汇报题目 */
    @Excel(name = "汇报题目")
    private String reportTopic;

    /** 所属会场 */
    @Excel(name = "所属会场")
    private String venue;

    /** 会议手册（文件） */
    @Excel(name = "会议手册", readConverterExp = "文=件")
    private String meetingManual;

    /** 精选照片 */
    @Excel(name = "精选照片")
    private String photos;

    /** 返校汇报 */
    @Excel(name = "返校汇报")
    private Integer isReport;

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

    public void setParticipants(String participants) 
    {
        this.participants = participants;
    }

    public String getParticipants() 
    {
        return participants;
    }

    public void setDepartureDate(Date departureDate) 
    {
        this.departureDate = departureDate;
    }

    public Date getDepartureDate() 
    {
        return departureDate;
    }

    public void setReturnDate(Date returnDate) 
    {
        this.returnDate = returnDate;
    }

    public Date getReturnDate() 
    {
        return returnDate;
    }

    public void setReporter(String reporter) 
    {
        this.reporter = reporter;
    }

    public String getReporter() 
    {
        return reporter;
    }

    public void setReportTopic(String reportTopic) 
    {
        this.reportTopic = reportTopic;
    }

    public String getReportTopic() 
    {
        return reportTopic;
    }

    public void setVenue(String venue) 
    {
        this.venue = venue;
    }

    public String getVenue() 
    {
        return venue;
    }

    public void setMeetingManual(String meetingManual) 
    {
        this.meetingManual = meetingManual;
    }

    public String getMeetingManual() 
    {
        return meetingManual;
    }

    public void setPhotos(String photos) 
    {
        this.photos = photos;
    }

    public String getPhotos() 
    {
        return photos;
    }

    public void setIsReport(Integer isReport) 
    {
        this.isReport = isReport;
    }

    public Integer getIsReport() 
    {
        return isReport;
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
            .append("meetingName", getMeetingName())
            .append("hostUnit", getHostUnit())
            .append("participants", getParticipants())
            .append("departureDate", getDepartureDate())
            .append("returnDate", getReturnDate())
            .append("reporter", getReporter())
            .append("reportTopic", getReportTopic())
            .append("venue", getVenue())
            .append("meetingManual", getMeetingManual())
            .append("photos", getPhotos())
            .append("isReport", getIsReport())
            .append("auditStatus", getAuditStatus())
            .append("auditReason", getAuditReason())
            .toString();
    }
}
