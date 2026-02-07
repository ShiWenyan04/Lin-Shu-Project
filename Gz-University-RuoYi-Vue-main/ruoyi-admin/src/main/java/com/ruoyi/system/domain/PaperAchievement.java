package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 论文成果管理对象 paper_achievement
 *
 * @author ruoyi
 * @date 2025-12-20
 */
public class PaperAchievement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 序号 */
    @Excel(name = "序号")
    private String serialNumber;

    /** 第一作者（含共同一作） */
    @Excel(name = "第一作者")
    private String firstAuthor;

    /** 学位类别（本科、硕士、博士） */
    @Excel(name = "学位类别")
    private String degreeType;

    /** 年级 */
    @Excel(name = "年级")
    private String grade;

    /** 指导人 */
    @Excel(name = "指导人")
    private String supervisor;

    /** 其他作者 */
    private String otherAuthors;

    /** 题目 */
    @Excel(name = "题目")
    private String title;

    /** 期刊 */
    @Excel(name = "期刊")
    private String journal;

    /** 中科院分区（非必填） */
    @Excel(name = "中科院分区")
    private String casPartition;

    /** TOP（0=否，1=是） */
    @Excel(name = "TOP", readConverterExp = "0=否,1=是")
    private String isTop;

    /** 自然指数（0=否，1=是） */
    @Excel(name = "自然指数", readConverterExp = "0=否,1=是")
    private String isNaturalIndex;

    /** 影响因子 */
    @Excel(name = "影响因子")
    private BigDecimal impactFactor;

    /** DOI */
    @Excel(name = "DOI")
    private String doi;

    /** 投稿时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "投稿时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date submissionDate;

    /** 接收时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "接收时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date acceptanceDate;

    /** 发表时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发表时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date publicationDate;

    /** 摘要 */
    private String paperAbstract;

    /** 方向类别 */
    @Excel(name = "方向类别")
    private String directionCategory;

    /** APA引用 */
    @Excel(name = "APA引用")
    private String apaCitation;

    /** 版面费 */
    @Excel(name = "版面费")
    private BigDecimal pageFee;

    /** 收录证明文件路径 */
    private String inclusionProof;

    /** 奖励发放（0=否，1=是） */
    @Excel(name = "奖励发放", readConverterExp = "0=否,1=是")
    private String rewardStatus;

    /** 奖励额度 */
    @Excel(name = "奖励额度")
    private BigDecimal rewardAmount;

    /** 奖励时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "奖励时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date rewardDate;

    /** 审核状态（0=待审核，1=审核通过，2=审核不通过） */
    @Excel(name = "审核状态", readConverterExp = "0=待审核,1=审核通过,2=审核不通过")
    private String auditStatus;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setSerialNumber(String serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    public String getSerialNumber()
    {
        return serialNumber;
    }

    public void setFirstAuthor(String firstAuthor)
    {
        this.firstAuthor = firstAuthor;
    }

    public String getFirstAuthor()
    {
        return firstAuthor;
    }

    public void setDegreeType(String degreeType)
    {
        this.degreeType = degreeType;
    }

    public String getDegreeType()
    {
        return degreeType;
    }

    public void setGrade(String grade)
    {
        this.grade = grade;
    }

    public String getGrade()
    {
        return grade;
    }

    public void setSupervisor(String supervisor)
    {
        this.supervisor = supervisor;
    }

    public String getSupervisor()
    {
        return supervisor;
    }

    public void setOtherAuthors(String otherAuthors)
    {
        this.otherAuthors = otherAuthors;
    }

    public String getOtherAuthors()
    {
        return otherAuthors;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }

    public void setJournal(String journal)
    {
        this.journal = journal;
    }

    public String getJournal()
    {
        return journal;
    }

    public void setCasPartition(String casPartition)
    {
        this.casPartition = casPartition;
    }

    public String getCasPartition()
    {
        return casPartition;
    }

    public void setIsTop(String isTop)
    {
        this.isTop = isTop;
    }

    public String getIsTop()
    {
        return isTop;
    }

    public void setIsNaturalIndex(String isNaturalIndex)
    {
        this.isNaturalIndex = isNaturalIndex;
    }

    public String getIsNaturalIndex()
    {
        return isNaturalIndex;
    }

    public void setImpactFactor(BigDecimal impactFactor)
    {
        this.impactFactor = impactFactor;
    }

    public BigDecimal getImpactFactor()
    {
        return impactFactor;
    }

    public void setDoi(String doi)
    {
        this.doi = doi;
    }

    public String getDoi()
    {
        return doi;
    }

    public void setSubmissionDate(Date submissionDate)
    {
        this.submissionDate = submissionDate;
    }

    public Date getSubmissionDate()
    {
        return submissionDate;
    }

    public void setAcceptanceDate(Date acceptanceDate)
    {
        this.acceptanceDate = acceptanceDate;
    }

    public Date getAcceptanceDate()
    {
        return acceptanceDate;
    }

    public void setPublicationDate(Date publicationDate)
    {
        this.publicationDate = publicationDate;
    }

    public Date getPublicationDate()
    {
        return publicationDate;
    }

    public void setPaperAbstract(String paperAbstract)
    {
        this.paperAbstract = paperAbstract;
    }

    public String getPaperAbstract()
    {
        return paperAbstract;
    }

    public void setDirectionCategory(String directionCategory)
    {
        this.directionCategory = directionCategory;
    }

    public String getDirectionCategory()
    {
        return directionCategory;
    }

    public void setApaCitation(String apaCitation)
    {
        this.apaCitation = apaCitation;
    }

    public String getApaCitation()
    {
        return apaCitation;
    }

    public void setPageFee(BigDecimal pageFee)
    {
        this.pageFee = pageFee;
    }

    public BigDecimal getPageFee()
    {
        return pageFee;
    }

    public void setInclusionProof(String inclusionProof)
    {
        this.inclusionProof = inclusionProof;
    }

    public String getInclusionProof()
    {
        return inclusionProof;
    }

    public void setRewardStatus(String rewardStatus)
    {
        this.rewardStatus = rewardStatus;
    }

    public String getRewardStatus()
    {
        return rewardStatus;
    }

    public void setRewardAmount(BigDecimal rewardAmount)
    {
        this.rewardAmount = rewardAmount;
    }

    public BigDecimal getRewardAmount()
    {
        return rewardAmount;
    }

    public void setRewardDate(Date rewardDate)
    {
        this.rewardDate = rewardDate;
    }

    public Date getRewardDate()
    {
        return rewardDate;
    }

    public void setAuditStatus(String auditStatus)
    {
        this.auditStatus = auditStatus;
    }

    public String getAuditStatus()
    {
        return auditStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("serialNumber", getSerialNumber())
                .append("firstAuthor", getFirstAuthor())
                .append("degreeType", getDegreeType())
                .append("grade", getGrade())
                .append("supervisor", getSupervisor())
                .append("otherAuthors", getOtherAuthors())
                .append("title", getTitle())
                .append("journal", getJournal())
                .append("casPartition", getCasPartition())
                .append("isTop", getIsTop())
                .append("isNaturalIndex", getIsNaturalIndex())
                .append("impactFactor", getImpactFactor())
                .append("doi", getDoi())
                .append("submissionDate", getSubmissionDate())
                .append("acceptanceDate", getAcceptanceDate())
                .append("publicationDate", getPublicationDate())
                .append("paperAbstract", getPaperAbstract())
                .append("directionCategory", getDirectionCategory())
                .append("apaCitation", getApaCitation())
                .append("pageFee", getPageFee())
                .append("inclusionProof", getInclusionProof())
                .append("rewardStatus", getRewardStatus())
                .append("rewardAmount", getRewardAmount())
                .append("rewardDate", getRewardDate())
                .append("auditStatus", getAuditStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}