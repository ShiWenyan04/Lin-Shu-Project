package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 其他成果管理对象 other_achievement
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
public class OtherAchievement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    @Excel(name = "序号")
    private Long id;

    /** 成果类型（1=专利，2=竞赛，3=英语六级，4=其他荣誉） */
    @Excel(name = "成果类型", readConverterExp = "1==专利，2=竞赛，3=英语六级，4=其他荣誉")
    private String achievementType;

    /** 成果名称 */
    @Excel(name = "成果名称")
    private String name;

    /** 主完成人 */
    @Excel(name = "主完成人")
    private String mainPerson;

    /** 共同完成人（多人用逗号分隔） */
    @Excel(name = "共同完成人", readConverterExp = "多=人用逗号分隔")
    private String coPerson;

    /** 颁发单位 */
    @Excel(name = "颁发单位")
    private String issueUnit;

    /** 获奖等次 */
    @Excel(name = "获奖等次")
    private String awardLevel;

    /** 获得时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "获得时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date obtainTime;

    /** 奖励发放（0=否，1=是） */
    @Excel(name = "奖励发放", readConverterExp = "0==否，1=是")
    private String rewardStatus;

    /** 奖励额度（元） */
    @Excel(name = "奖励额度", readConverterExp = "元=")
    private BigDecimal rewardAmount;

    /** 奖励时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "奖励时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date rewardTime;

    /** 审核状态（0=待审核，1=审核通过，2=审核不通过） */
    @Excel(name = "审核状态", readConverterExp = "0==待审核，1=审核通过，2=审核不通过")
    private String auditStatus;

    /** 审核意见 */
    @Excel(name = "审核意见")
    private String auditOpinion;

    /** 审核人ID */
    private Long auditorId;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date auditTime;

    /** 删除标志（0=存在，1=删除） */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setAchievementType(String achievementType) 
    {
        this.achievementType = achievementType;
    }

    public String getAchievementType() 
    {
        return achievementType;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setMainPerson(String mainPerson) 
    {
        this.mainPerson = mainPerson;
    }

    public String getMainPerson() 
    {
        return mainPerson;
    }

    public void setCoPerson(String coPerson) 
    {
        this.coPerson = coPerson;
    }

    public String getCoPerson() 
    {
        return coPerson;
    }

    public void setIssueUnit(String issueUnit) 
    {
        this.issueUnit = issueUnit;
    }

    public String getIssueUnit() 
    {
        return issueUnit;
    }

    public void setAwardLevel(String awardLevel) 
    {
        this.awardLevel = awardLevel;
    }

    public String getAwardLevel() 
    {
        return awardLevel;
    }

    public void setObtainTime(Date obtainTime) 
    {
        this.obtainTime = obtainTime;
    }

    public Date getObtainTime() 
    {
        return obtainTime;
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

    public void setRewardTime(Date rewardTime) 
    {
        this.rewardTime = rewardTime;
    }

    public Date getRewardTime() 
    {
        return rewardTime;
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

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("achievementType", getAchievementType())
            .append("name", getName())
            .append("mainPerson", getMainPerson())
            .append("coPerson", getCoPerson())
            .append("issueUnit", getIssueUnit())
            .append("awardLevel", getAwardLevel())
            .append("obtainTime", getObtainTime())
            .append("rewardStatus", getRewardStatus())
            .append("rewardAmount", getRewardAmount())
            .append("rewardTime", getRewardTime())
            .append("auditStatus", getAuditStatus())
            .append("auditOpinion", getAuditOpinion())
            .append("auditorId", getAuditorId())
            .append("auditTime", getAuditTime())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
