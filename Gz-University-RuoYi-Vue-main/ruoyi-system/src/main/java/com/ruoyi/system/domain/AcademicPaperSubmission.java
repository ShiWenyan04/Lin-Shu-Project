package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 学术论文投稿对象 academic_paper_submission
 * 
 * @author ruoyi
 * @date 2025-12-28
 */
public class AcademicPaperSubmission extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序 */
    private Long id;

    /** 关联写作表ID */
    @Excel(name = "关联写作表ID")
    private Long paperId;

    /** 姓名 */
    @Excel(name = "姓名")
    private String studentName;

    /** 学位类别（硕士/博士） */
    @Excel(name = "学位类别", readConverterExp = "硕=士/博士")
    private String degreeType;

    /** 指导人 */
    @Excel(name = "指导人")
    private String supervisorName;

    /** 定题名称 */
    @Excel(name = "定题名称")
    private String paperTitle;

    /** 投稿时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "投稿时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date submissionDate;

    /** 投稿期刊 */
    @Excel(name = "投稿期刊")
    private String journalName;

    /** 接收时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "接收时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date receiveDate;

    /** 拒稿时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "拒稿时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date rejectionDate;

    /** 拒稿再投时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "拒稿再投时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date resubmissionDate;

    /** 发表时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发表时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date publicationDate;

    /** 投稿状态 */
    @Excel(name = "投稿状态")
    private String submissionStatus;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setPaperId(Long paperId) 
    {
        this.paperId = paperId;
    }

    public Long getPaperId() 
    {
        return paperId;
    }

    public void setStudentName(String studentName) 
    {
        this.studentName = studentName;
    }

    public String getStudentName() 
    {
        return studentName;
    }

    public void setDegreeType(String degreeType) 
    {
        this.degreeType = degreeType;
    }

    public String getDegreeType() 
    {
        return degreeType;
    }

    public void setSupervisorName(String supervisorName) 
    {
        this.supervisorName = supervisorName;
    }

    public String getSupervisorName() 
    {
        return supervisorName;
    }

    public void setPaperTitle(String paperTitle) 
    {
        this.paperTitle = paperTitle;
    }

    public String getPaperTitle() 
    {
        return paperTitle;
    }

    public void setSubmissionDate(Date submissionDate) 
    {
        this.submissionDate = submissionDate;
    }

    public Date getSubmissionDate() 
    {
        return submissionDate;
    }

    public void setJournalName(String journalName) 
    {
        this.journalName = journalName;
    }

    public String getJournalName() 
    {
        return journalName;
    }

    public void setReceiveDate(Date receiveDate) 
    {
        this.receiveDate = receiveDate;
    }

    public Date getReceiveDate() 
    {
        return receiveDate;
    }

    public void setRejectionDate(Date rejectionDate) 
    {
        this.rejectionDate = rejectionDate;
    }

    public Date getRejectionDate() 
    {
        return rejectionDate;
    }

    public void setResubmissionDate(Date resubmissionDate) 
    {
        this.resubmissionDate = resubmissionDate;
    }

    public Date getResubmissionDate() 
    {
        return resubmissionDate;
    }

    public void setPublicationDate(Date publicationDate) 
    {
        this.publicationDate = publicationDate;
    }

    public Date getPublicationDate() 
    {
        return publicationDate;
    }

    public void setSubmissionStatus(String submissionStatus) 
    {
        this.submissionStatus = submissionStatus;
    }

    public String getSubmissionStatus() 
    {
        return submissionStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("paperId", getPaperId())
            .append("studentName", getStudentName())
            .append("degreeType", getDegreeType())
            .append("supervisorName", getSupervisorName())
            .append("paperTitle", getPaperTitle())
            .append("submissionDate", getSubmissionDate())
            .append("journalName", getJournalName())
            .append("receiveDate", getReceiveDate())
            .append("rejectionDate", getRejectionDate())
            .append("resubmissionDate", getResubmissionDate())
            .append("publicationDate", getPublicationDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("submissionStatus", getSubmissionStatus())
            .toString();
    }
}
