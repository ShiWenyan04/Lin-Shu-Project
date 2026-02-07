package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 外业行程结束对象 busi_field_end
 * 
 * @author ruoyi
 * @date 2025-12-22
 */
public class BusiFieldEnd extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long endId;

    /** 关联外业启动ID */
    @Excel(name = "关联外业启动ID")
    private Long startId;

    /** 外业负责人 */
    @Excel(name = "外业负责人")
    private String leaderName;

    /** 外业小结附件 */
    @Excel(name = "外业小结附件")
    private String summaryFile;

    /** 任务完成情况 */
    @Excel(name = "任务完成情况")
    private String taskResult;

    /** 实际出发时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际出发时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actStartDate;

    /** 实际结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actEndDate;

    /** 外业总天数 */
    @Excel(name = "外业总天数")
    private Long actDays;

    /** 有无报销明细 */
    @Excel(name = "有无报销明细")
    private String isReimburse;

    /** 发票是否提交 */
    @Excel(name = "发票是否提交")
    private String isInvoice;

    /** 审核状态 */
    @Excel(name = "审核状态")
    private String auditStatus;

    /** 审核意见 */
    @Excel(name = "审核意见")
    private String auditReason;

    // 在 BusiFieldEnd 类中增加
    /** 是否已归档 (虚拟字段) */
    private Integer archiveCount;

    public Integer getArchiveCount() {
        return archiveCount;
    }

    public void setArchiveCount(Integer archiveCount) {
        this.archiveCount = archiveCount;
    }

    /** 项目名称 (展示用，非数据库字段) */
    private String projectName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setEndId(Long endId) 
    {
        this.endId = endId;
    }

    public Long getEndId() 
    {
        return endId;
    }

    public void setStartId(Long startId) 
    {
        this.startId = startId;
    }

    public Long getStartId() 
    {
        return startId;
    }

    public void setLeaderName(String leaderName) 
    {
        this.leaderName = leaderName;
    }

    public String getLeaderName() 
    {
        return leaderName;
    }

    public void setSummaryFile(String summaryFile) 
    {
        this.summaryFile = summaryFile;
    }

    public String getSummaryFile() 
    {
        return summaryFile;
    }

    public void setTaskResult(String taskResult) 
    {
        this.taskResult = taskResult;
    }

    public String getTaskResult() 
    {
        return taskResult;
    }

    public void setActStartDate(Date actStartDate) 
    {
        this.actStartDate = actStartDate;
    }

    public Date getActStartDate() 
    {
        return actStartDate;
    }

    public void setActEndDate(Date actEndDate) 
    {
        this.actEndDate = actEndDate;
    }

    public Date getActEndDate() 
    {
        return actEndDate;
    }

    public void setActDays(Long actDays) 
    {
        this.actDays = actDays;
    }

    public Long getActDays() 
    {
        return actDays;
    }

    public void setIsReimburse(String isReimburse) 
    {
        this.isReimburse = isReimburse;
    }

    public String getIsReimburse() 
    {
        return isReimburse;
    }

    public void setIsInvoice(String isInvoice) 
    {
        this.isInvoice = isInvoice;
    }

    public String getIsInvoice() 
    {
        return isInvoice;
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
            .append("endId", getEndId())
            .append("startId", getStartId())
            .append("leaderName", getLeaderName())
            .append("summaryFile", getSummaryFile())
            .append("taskResult", getTaskResult())
            .append("actStartDate", getActStartDate())
            .append("actEndDate", getActEndDate())
            .append("actDays", getActDays())
            .append("isReimburse", getIsReimburse())
            .append("isInvoice", getIsInvoice())
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
