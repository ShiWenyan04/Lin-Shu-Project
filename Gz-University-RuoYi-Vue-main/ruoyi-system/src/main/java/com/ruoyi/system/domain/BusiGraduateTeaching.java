package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 研究生助教安排对象 busi_graduate_teaching
 * 
 * @author ruoyi
 * @date 2026-01-08
 */
public class BusiGraduateTeaching extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 姓名 */
    @Excel(name = "姓名")
    private String studentName;

    /** 年级 */
    @Excel(name = "年级")
    private String grade;

    /** 课程名称 */
    @Excel(name = "课程名称")
    private String courseName;

    /** 授课时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "授课时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date teachingTime;

    /** 授课班级 */
    @Excel(name = "授课班级")
    private String teachingClass;

    /** 教学周数 */
    @Excel(name = "教学周数")
    private Long teachingWeeks;

    /** 授课内容 */
    @Excel(name = "授课内容")
    private String teachingContent;

    /** 教案PPT */
    @Excel(name = "教案PPT")
    private String pptFile;

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

    public void setStudentName(String studentName) 
    {
        this.studentName = studentName;
    }

    public String getStudentName() 
    {
        return studentName;
    }

    public void setGrade(String grade) 
    {
        this.grade = grade;
    }

    public String getGrade() 
    {
        return grade;
    }

    public void setCourseName(String courseName) 
    {
        this.courseName = courseName;
    }

    public String getCourseName() 
    {
        return courseName;
    }

    public void setTeachingTime(Date teachingTime) 
    {
        this.teachingTime = teachingTime;
    }

    public Date getTeachingTime() 
    {
        return teachingTime;
    }

    public void setTeachingClass(String teachingClass) 
    {
        this.teachingClass = teachingClass;
    }

    public String getTeachingClass() 
    {
        return teachingClass;
    }

    public void setTeachingWeeks(Long teachingWeeks) 
    {
        this.teachingWeeks = teachingWeeks;
    }

    public Long getTeachingWeeks() 
    {
        return teachingWeeks;
    }

    public void setTeachingContent(String teachingContent) 
    {
        this.teachingContent = teachingContent;
    }

    public String getTeachingContent() 
    {
        return teachingContent;
    }

    public void setPptFile(String pptFile) 
    {
        this.pptFile = pptFile;
    }

    public String getPptFile() 
    {
        return pptFile;
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
            .append("studentName", getStudentName())
            .append("grade", getGrade())
            .append("courseName", getCourseName())
            .append("teachingTime", getTeachingTime())
            .append("teachingClass", getTeachingClass())
            .append("teachingWeeks", getTeachingWeeks())
            .append("teachingContent", getTeachingContent())
            .append("pptFile", getPptFile())
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
