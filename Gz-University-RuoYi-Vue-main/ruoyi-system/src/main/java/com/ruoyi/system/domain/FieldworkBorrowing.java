package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 外业调查设备借用对象 fieldwork_borrowing
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
public class FieldworkBorrowing extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 关联设备ID */
    @Excel(name = "关联设备ID")
    private Long equipmentId;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String equipmentName;

    /** 型号 */
    @Excel(name = "型号")
    private String model;

    /** 借用人员 */
    @Excel(name = "借用人员")
    private String borrower;

    /** 借用数量 */
    @Excel(name = "借用数量")
    private Long borrowQty;

    /** 借用时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "借用时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date borrowTime;

    /** 地点 */
    @Excel(name = "地点")
    private String location;

    /** 关联项目ID */
    @Excel(name = "关联项目ID")
    private Long projectId;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 导师姓名 */
    @Excel(name = "导师姓名")
    private String tutorName;

    /** 审核状态 */
    @Excel(name = "审核状态")
    private String auditStatus;

    /** 驳回原因 */
    @Excel(name = "驳回原因")
    private String auditReason;

    /** * 统计字段：历史已归还/核销总数
     * (非数据库表字段，由SQL计算得出)
     */
    private Integer returnCount;

    public void setReturnCount(Integer returnCount)
    {
        this.returnCount = returnCount;
    }

    public Integer getReturnCount()
    {
        return returnCount;
    }

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setEquipmentId(Long equipmentId) 
    {
        this.equipmentId = equipmentId;
    }

    public Long getEquipmentId() 
    {
        return equipmentId;
    }

    public void setEquipmentName(String equipmentName) 
    {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentName() 
    {
        return equipmentName;
    }

    public void setModel(String model) 
    {
        this.model = model;
    }

    public String getModel() 
    {
        return model;
    }

    public void setBorrower(String borrower) 
    {
        this.borrower = borrower;
    }

    public String getBorrower() 
    {
        return borrower;
    }

    public void setBorrowQty(Long borrowQty) 
    {
        this.borrowQty = borrowQty;
    }

    public Long getBorrowQty() 
    {
        return borrowQty;
    }

    public void setBorrowTime(Date borrowTime) 
    {
        this.borrowTime = borrowTime;
    }

    public Date getBorrowTime() 
    {
        return borrowTime;
    }

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }

    public void setProjectId(Long projectId) 
    {
        this.projectId = projectId;
    }

    public Long getProjectId() 
    {
        return projectId;
    }

    public void setProjectName(String projectName) 
    {
        this.projectName = projectName;
    }

    public String getProjectName() 
    {
        return projectName;
    }

    public void setTutorName(String tutorName) 
    {
        this.tutorName = tutorName;
    }

    public String getTutorName() 
    {
        return tutorName;
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
            .append("equipmentId", getEquipmentId())
            .append("equipmentName", getEquipmentName())
            .append("model", getModel())
            .append("borrower", getBorrower())
            .append("borrowQty", getBorrowQty())
            .append("borrowTime", getBorrowTime())
            .append("location", getLocation())
            .append("projectId", getProjectId())
            .append("projectName", getProjectName())
            .append("tutorName", getTutorName())
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
