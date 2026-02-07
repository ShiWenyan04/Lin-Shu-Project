package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 外业启动申请对象 busi_field_start
 * 
 * @author ruoyi
 * @date 2025-12-17
 */
public class BusiFieldStart extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long startId;

    /** 关联项目ID */
    @Excel(name = "关联项目ID")
    private Long projectId;

    /** 外业负责人 */
    @Excel(name = "外业负责人")
    private String leaderName;

    /** 外业事由 */
    @Excel(name = "外业事由")
    private String reason;

    /** 目的地 */
    @Excel(name = "目的地")
    private String destination;

    /** 出发时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出发时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 计划天数 */
    @Excel(name = "计划天数")
    private Long planDays;

    /** 本组参加人员 */
    @Excel(name = "本组参加人员")
    private String membersInner;

    /** 组外参加人员 */
    @Excel(name = "组外参加人员")
    private String membersOuter;

    /** 是否购买保险 */
    @Excel(name = "是否购买保险")
    private String isInsurance;

    /** 租车公司 */
    @Excel(name = "租车公司")
    private String carCompany;

    /** 调查方案 */
    @Excel(name = "调查方案")
    private String planFile;

    /** 审核状态 */
    @Excel(name = "审核状态")
    private String auditStatus;

    /** 驳回原因 */
    @Excel(name = "驳回原因")
    private String auditReason;

    /** 项目名称 (展示用，非数据库字段) */
    private String projectName;

    /** 内部人员姓名串 (展示用，非数据库字段) */
    private String membersInnerNames;

    // 在类里面增加这个字段
    /** 是否已填报结束 (虚拟字段，sql查询得出) */
    private Integer endCount;

    public Integer getEndCount() {
        return endCount;
    }

    public void setEndCount(Integer endCount) {
        this.endCount = endCount;
    }

    // 必须手动添加 get 和 set 方法，否则报错 "找不到符号"

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getMembersInnerNames() {
        return membersInnerNames;
    }

    public void setMembersInnerNames(String membersInnerNames) {
        this.membersInnerNames = membersInnerNames;
    }

    public void setStartId(Long startId) 
    {
        this.startId = startId;
    }

    public Long getStartId() 
    {
        return startId;
    }

    public void setProjectId(Long projectId) 
    {
        this.projectId = projectId;
    }

    public Long getProjectId() 
    {
        return projectId;
    }

    public void setLeaderName(String leaderName) 
    {
        this.leaderName = leaderName;
    }

    public String getLeaderName() 
    {
        return leaderName;
    }

    public void setReason(String reason) 
    {
        this.reason = reason;
    }

    public String getReason() 
    {
        return reason;
    }

    public void setDestination(String destination) 
    {
        this.destination = destination;
    }

    public String getDestination() 
    {
        return destination;
    }

    public void setStartDate(Date startDate) 
    {
        this.startDate = startDate;
    }

    public Date getStartDate() 
    {
        return startDate;
    }

    public void setPlanDays(Long planDays) 
    {
        this.planDays = planDays;
    }

    public Long getPlanDays() 
    {
        return planDays;
    }

    public void setMembersInner(String membersInner) 
    {
        this.membersInner = membersInner;
    }

    public String getMembersInner() 
    {
        return membersInner;
    }

    public void setMembersOuter(String membersOuter) 
    {
        this.membersOuter = membersOuter;
    }

    public String getMembersOuter() 
    {
        return membersOuter;
    }

    public void setIsInsurance(String isInsurance) 
    {
        this.isInsurance = isInsurance;
    }

    public String getIsInsurance() 
    {
        return isInsurance;
    }

    public void setCarCompany(String carCompany) 
    {
        this.carCompany = carCompany;
    }

    public String getCarCompany() 
    {
        return carCompany;
    }

    public void setPlanFile(String planFile) 
    {
        this.planFile = planFile;
    }

    public String getPlanFile() 
    {
        return planFile;
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
            .append("startId", getStartId())
            .append("projectId", getProjectId())
            .append("leaderName", getLeaderName())
            .append("reason", getReason())
            .append("destination", getDestination())
            .append("startDate", getStartDate())
            .append("planDays", getPlanDays())
            .append("membersInner", getMembersInner())
            .append("membersOuter", getMembersOuter())
            .append("isInsurance", getIsInsurance())
            .append("carCompany", getCarCompany())
            .append("planFile", getPlanFile())
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
