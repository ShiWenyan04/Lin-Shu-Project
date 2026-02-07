package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 开题与学位论文对象 thesis_proposal
 * 
 * @author ruoyi
 * @date 2025-12-28
 */
public class ThesisProposal extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序 */
    private Long id;

    /** 学生姓名 */
    @Excel(name = "学生姓名")
    private String studentName;

    /** 类别（硕士/博士） */
    @Excel(name = "类别", readConverterExp = "硕=士/博士")
    private String degreeType;

    /** 专业（野保、林业、生态学、环境工程） */
    @Excel(name = "专业", readConverterExp = "野=保、林业、生态学、环境工程")
    private String major;

    /** 入学年级 */
    @Excel(name = "入学年级")
    private String enrollmentYear;

    /** 开题题目 */
    @Excel(name = "开题题目")
    private String thesisTitle;

    /** 方向类别 */
    @Excel(name = "方向类别")
    private String researchDirection;

    /** 计划开题日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划开题日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date plannedProposalDate;

    /** 开题答辩日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开题答辩日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actualProposalDate;

    /** 中期检查日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "中期检查日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date midtermCheckDate;

    /** 中期答辩日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "中期答辩日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date midtermDefenseDate;

    /** 开题报告（文档） */
    @Excel(name = "开题报告", readConverterExp = "文=档")
    private String proposalReportUrl;

    /** 状态（0进行中，1已完成） */
    @Excel(name = "状态", readConverterExp = "0=进行中，1已完成")
    private Integer status;

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

    public void setMajor(String major) 
    {
        this.major = major;
    }

    public String getMajor() 
    {
        return major;
    }

    public void setEnrollmentYear(String enrollmentYear) 
    {
        this.enrollmentYear = enrollmentYear;
    }

    public String getEnrollmentYear() 
    {
        return enrollmentYear;
    }

    public void setThesisTitle(String thesisTitle) 
    {
        this.thesisTitle = thesisTitle;
    }

    public String getThesisTitle() 
    {
        return thesisTitle;
    }

    public void setResearchDirection(String researchDirection) 
    {
        this.researchDirection = researchDirection;
    }

    public String getResearchDirection() 
    {
        return researchDirection;
    }

    public void setPlannedProposalDate(Date plannedProposalDate) 
    {
        this.plannedProposalDate = plannedProposalDate;
    }

    public Date getPlannedProposalDate() 
    {
        return plannedProposalDate;
    }

    public void setActualProposalDate(Date actualProposalDate) 
    {
        this.actualProposalDate = actualProposalDate;
    }

    public Date getActualProposalDate() 
    {
        return actualProposalDate;
    }

    public void setMidtermCheckDate(Date midtermCheckDate) 
    {
        this.midtermCheckDate = midtermCheckDate;
    }

    public Date getMidtermCheckDate() 
    {
        return midtermCheckDate;
    }

    public void setMidtermDefenseDate(Date midtermDefenseDate) 
    {
        this.midtermDefenseDate = midtermDefenseDate;
    }

    public Date getMidtermDefenseDate() 
    {
        return midtermDefenseDate;
    }

    public void setProposalReportUrl(String proposalReportUrl) 
    {
        this.proposalReportUrl = proposalReportUrl;
    }

    public String getProposalReportUrl() 
    {
        return proposalReportUrl;
    }

    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("studentName", getStudentName())
            .append("degreeType", getDegreeType())
            .append("major", getMajor())
            .append("enrollmentYear", getEnrollmentYear())
            .append("thesisTitle", getThesisTitle())
            .append("researchDirection", getResearchDirection())
            .append("plannedProposalDate", getPlannedProposalDate())
            .append("actualProposalDate", getActualProposalDate())
            .append("midtermCheckDate", getMidtermCheckDate())
            .append("midtermDefenseDate", getMidtermDefenseDate())
            .append("proposalReportUrl", getProposalReportUrl())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("status", getStatus())
            .toString();
    }
}
