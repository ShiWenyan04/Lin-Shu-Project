package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 本科论文指导对象 busi_assistant_guidance
 * 
 * @author ruoyi
 * @date 2026-01-08
 */
public class BusiAssistantGuidance extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 年级 */
    @Excel(name = "年级")
    private String grade;

    /** 班级 */
    @Excel(name = "班级")
    private String className;

    /** 姓名 */
    @Excel(name = "姓名")
    private String studentName;

    /** 学号 */
    @Excel(name = "学号")
    private String studentId;

    /** 指导人 */
    @Excel(name = "指导人")
    private String supervisor;

    /** 论文题目 */
    @Excel(name = "论文题目")
    private String thesisTitle;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 开题时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开题时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date proposalDate;

    /** 答辩时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "答辩时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date defenseDate;

    /** 答辩结果 */
    @Excel(name = "答辩结果")
    private String defenseResult;

    /** 指导费用 */
    @Excel(name = "指导费用")
    private String hasFee;

    /** 指导记录及评价附件 */
    @Excel(name = "指导记录及评价附件")
    private String evaluationFile;

    /** 审核状态 */
    @Excel(name = "审核状态")
    private String auditStatus;

    /** 审核意见 */
    @Excel(name = "审核意见")
    private String auditReason;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setGrade(String grade) 
    {
        this.grade = grade;
    }

    public String getGrade() 
    {
        return grade;
    }

    public void setClassName(String className) 
    {
        this.className = className;
    }

    public String getClassName() 
    {
        return className;
    }

    public void setStudentName(String studentName) 
    {
        this.studentName = studentName;
    }

    public String getStudentName() 
    {
        return studentName;
    }

    public void setStudentId(String studentId) 
    {
        this.studentId = studentId;
    }

    public String getStudentId() 
    {
        return studentId;
    }

    public void setSupervisor(String supervisor) 
    {
        this.supervisor = supervisor;
    }

    public String getSupervisor() 
    {
        return supervisor;
    }

    public void setThesisTitle(String thesisTitle) 
    {
        this.thesisTitle = thesisTitle;
    }

    public String getThesisTitle() 
    {
        return thesisTitle;
    }

    public void setProjectName(String projectName) 
    {
        this.projectName = projectName;
    }

    public String getProjectName() 
    {
        return projectName;
    }

    public void setProposalDate(Date proposalDate) 
    {
        this.proposalDate = proposalDate;
    }

    public Date getProposalDate() 
    {
        return proposalDate;
    }

    public void setDefenseDate(Date defenseDate) 
    {
        this.defenseDate = defenseDate;
    }

    public Date getDefenseDate() 
    {
        return defenseDate;
    }

    public void setDefenseResult(String defenseResult) 
    {
        this.defenseResult = defenseResult;
    }

    public String getDefenseResult() 
    {
        return defenseResult;
    }

    public void setHasFee(String hasFee) 
    {
        this.hasFee = hasFee;
    }

    public String getHasFee() 
    {
        return hasFee;
    }

    public void setEvaluationFile(String evaluationFile) 
    {
        this.evaluationFile = evaluationFile;
    }

    public String getEvaluationFile() 
    {
        return evaluationFile;
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
            .append("grade", getGrade())
            .append("className", getClassName())
            .append("studentName", getStudentName())
            .append("studentId", getStudentId())
            .append("supervisor", getSupervisor())
            .append("thesisTitle", getThesisTitle())
            .append("projectName", getProjectName())
            .append("proposalDate", getProposalDate())
            .append("defenseDate", getDefenseDate())
            .append("defenseResult", getDefenseResult())
            .append("hasFee", getHasFee())
            .append("evaluationFile", getEvaluationFile())
            .append("auditStatus", getAuditStatus())
            .append("auditReason", getAuditReason())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
