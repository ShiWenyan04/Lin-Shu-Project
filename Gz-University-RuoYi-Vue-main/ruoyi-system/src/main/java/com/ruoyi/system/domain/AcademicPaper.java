package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 学术论文写作对象 academic_paper
 * 
 * @author ruoyi
 * @date 2025-12-28
 */
public class AcademicPaper extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序 */
    private Long id;

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

    /** 定题时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "定题时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date topicConfirmationDate;

    /** 计划完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date plannedCompletionDate;

    /** 初稿提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "初稿提交时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date firstDraftSubmitDate;

    /** 写作状态 */
    @Excel(name = "写作状态")
    private String writingStatus;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
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

    public void setTopicConfirmationDate(Date topicConfirmationDate) 
    {
        this.topicConfirmationDate = topicConfirmationDate;
    }

    public Date getTopicConfirmationDate() 
    {
        return topicConfirmationDate;
    }

    public void setPlannedCompletionDate(Date plannedCompletionDate) 
    {
        this.plannedCompletionDate = plannedCompletionDate;
    }

    public Date getPlannedCompletionDate() 
    {
        return plannedCompletionDate;
    }

    public void setFirstDraftSubmitDate(Date firstDraftSubmitDate) 
    {
        this.firstDraftSubmitDate = firstDraftSubmitDate;
    }

    public Date getFirstDraftSubmitDate() 
    {
        return firstDraftSubmitDate;
    }

    public void setWritingStatus(String writingStatus) 
    {
        this.writingStatus = writingStatus;
    }

    public String getWritingStatus() 
    {
        return writingStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("studentName", getStudentName())
            .append("degreeType", getDegreeType())
            .append("supervisorName", getSupervisorName())
            .append("paperTitle", getPaperTitle())
            .append("topicConfirmationDate", getTopicConfirmationDate())
            .append("plannedCompletionDate", getPlannedCompletionDate())
            .append("firstDraftSubmitDate", getFirstDraftSubmitDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("writingStatus", getWritingStatus())
            .toString();
    }
}
