package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 红外相机成果管理对象 busi_camera_result
 * 
 * @author ruoyi
 * @date 2026-01-14
 */
public class BusiCameraResult extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 成果ID */
    private Long id;

    /** 地点 */
    @Excel(name = "地点")
    private String regionName;

    /** 监测开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "监测开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date periodStart;

    /** 监测结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "监测结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date periodEnd;

    /** 关联安装项目ID集合 */
    @Excel(name = "关联安装项目ID集合")
    private String relatedIds;

    /** 监测方案(文件路径) */
    private String monitorPlanFile;

    /** 中期报告(文件路径) */
    private String interimRptFile;

    /** 结题报告(文件路径) */
    private String finalRptFile;

    /** 汇报PPT(文件路径) */
    private String pptFile;

    /** 撰写人 */
    @Excel(name = "撰写人")
    private String writerName;

    /** 审核状态(0待审核 1已通过 2已驳回) */
    @Excel(name = "审核状态(0待审核 1已通过 2已驳回)")
    private String auditStatus;

    /** 驳回原因 */
    @Excel(name = "驳回原因")
    private String auditReason;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setRegionName(String regionName) 
    {
        this.regionName = regionName;
    }

    public String getRegionName() 
    {
        return regionName;
    }

    public void setPeriodStart(Date periodStart) 
    {
        this.periodStart = periodStart;
    }

    public Date getPeriodStart() 
    {
        return periodStart;
    }

    public void setPeriodEnd(Date periodEnd) 
    {
        this.periodEnd = periodEnd;
    }

    public Date getPeriodEnd() 
    {
        return periodEnd;
    }

    public void setRelatedIds(String relatedIds) 
    {
        this.relatedIds = relatedIds;
    }

    public String getRelatedIds() 
    {
        return relatedIds;
    }

    public void setMonitorPlanFile(String monitorPlanFile) 
    {
        this.monitorPlanFile = monitorPlanFile;
    }

    public String getMonitorPlanFile() 
    {
        return monitorPlanFile;
    }

    public void setInterimRptFile(String interimRptFile) 
    {
        this.interimRptFile = interimRptFile;
    }

    public String getInterimRptFile() 
    {
        return interimRptFile;
    }

    public void setFinalRptFile(String finalRptFile) 
    {
        this.finalRptFile = finalRptFile;
    }

    public String getFinalRptFile() 
    {
        return finalRptFile;
    }

    public void setPptFile(String pptFile) 
    {
        this.pptFile = pptFile;
    }

    public String getPptFile() 
    {
        return pptFile;
    }

    public void setWriterName(String writerName) 
    {
        this.writerName = writerName;
    }

    public String getWriterName() 
    {
        return writerName;
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
            .append("regionName", getRegionName())
            .append("periodStart", getPeriodStart())
            .append("periodEnd", getPeriodEnd())
            .append("relatedIds", getRelatedIds())
            .append("monitorPlanFile", getMonitorPlanFile())
            .append("interimRptFile", getInterimRptFile())
            .append("finalRptFile", getFinalRptFile())
            .append("pptFile", getPptFile())
            .append("writerName", getWriterName())
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
