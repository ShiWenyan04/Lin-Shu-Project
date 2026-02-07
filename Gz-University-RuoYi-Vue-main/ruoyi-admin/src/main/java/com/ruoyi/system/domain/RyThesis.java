package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 学位论文管理对象 ry_thesis
 *
 * @author ruoyi
 * @date 2025-12-13
 */
public class RyThesis extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    private Long id;

    /** 姓名 */
    @Excel(name = "姓名")
    private String studentName;

    /** 学位类别（本科、硕士、博士） */
    @Excel(name = "学位类别", readConverterExp = "本=科、硕士、博士")
    private String degreeType;

    /** 毕业年份 */
    @Excel(name = "毕业年份")
    private String graduationYear;

    /** 校外导师 */
    @Excel(name = "校外导师")
    private String externalSupervisor;

    /** 论文题目 */
    @Excel(name = "论文题目")
    private String thesisTitle;

    /** 研究对象 */
    @Excel(name = "研究对象")
    private String researchSubject;

    /** 研究地点 */
    @Excel(name = "研究地点")
    private String researchLocation;

    /** 研究方向 */
    @Excel(name = "研究方向")
    private String researchField;

    /** 论文发表情况 */
    @Excel(name = "论文发表情况")
    private String publicationInfo;

    /** 审核状态（0=待审核，1=审核通过，2=审核不通过） */
    @Excel(name = "审核状态", readConverterExp = "0=待审核,1=审核通过,2=审核不通过")
    private String auditStatus;

    /** 审核意见 */
    @Excel(name = "审核意见")
    private String auditOpinion;

    /** 审核人ID */
    private Long auditorId;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date auditTime;

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

    public void setGraduationYear(String graduationYear)
    {
        this.graduationYear = graduationYear;
    }

    public String getGraduationYear()
    {
        return graduationYear;
    }

    public void setExternalSupervisor(String externalSupervisor)
    {
        this.externalSupervisor = externalSupervisor;
    }

    public String getExternalSupervisor()
    {
        return externalSupervisor;
    }

    public void setThesisTitle(String thesisTitle)
    {
        this.thesisTitle = thesisTitle;
    }

    public String getThesisTitle()
    {
        return thesisTitle;
    }

    public void setResearchSubject(String researchSubject)
    {
        this.researchSubject = researchSubject;
    }

    public String getResearchSubject()
    {
        return researchSubject;
    }

    public void setResearchLocation(String researchLocation)
    {
        this.researchLocation = researchLocation;
    }

    public String getResearchLocation()
    {
        return researchLocation;
    }

    public void setResearchField(String researchField)
    {
        this.researchField = researchField;
    }

    public String getResearchField()
    {
        return researchField;
    }

    public void setPublicationInfo(String publicationInfo)
    {
        this.publicationInfo = publicationInfo;
    }

    public String getPublicationInfo()
    {
        return publicationInfo;
    }

    public void setAuditStatus(String auditStatus)
    {
        this.auditStatus = auditStatus;
    }

    public String getAuditStatus()
    {
        return auditStatus;
    }

    public void setAuditOpinion(String auditOpinion)
    {
        this.auditOpinion = auditOpinion;
    }

    public String getAuditOpinion()
    {
        return auditOpinion;
    }

    public void setAuditorId(Long auditorId)
    {
        this.auditorId = auditorId;
    }

    public Long getAuditorId()
    {
        return auditorId;
    }

    public void setAuditTime(Date auditTime)
    {
        this.auditTime = auditTime;
    }

    public Date getAuditTime()
    {
        return auditTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("studentName", getStudentName())
                .append("degreeType", getDegreeType())
                .append("graduationYear", getGraduationYear())
                .append("externalSupervisor", getExternalSupervisor())
                .append("thesisTitle", getThesisTitle())
                .append("researchSubject", getResearchSubject())
                .append("researchLocation", getResearchLocation())
                .append("researchField", getResearchField())
                .append("publicationInfo", getPublicationInfo())
                .append("auditStatus", getAuditStatus())
                .append("auditOpinion", getAuditOpinion())
                .append("auditorId", getAuditorId())
                .append("auditTime", getAuditTime())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}