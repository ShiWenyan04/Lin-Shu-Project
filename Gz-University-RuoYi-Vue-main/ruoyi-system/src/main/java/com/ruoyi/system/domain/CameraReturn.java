package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 红外相机归还对象 camera_return
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
public class CameraReturn extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 关联借用单ID */
    @Excel(name = "关联借用单ID")
    private Long borrowId;

    /** 关联设备ID */
    @Excel(name = "关联设备ID")
    private Long equipmentId;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String equipmentName;

    /** 型号 */
    @Excel(name = "型号")
    private String model;

    /** 关联项目ID */
    @Excel(name = "关联项目ID")
    private Long projectId;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 归还人员 */
    @Excel(name = "归还人员")
    private String returner;

    /** 归还时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "归还时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date returnTime;

    /** 归还数量 */
    @Excel(name = "归还数量")
    private Long returnQty;

    /** 安装数量 */
    @Excel(name = "安装数量")
    private Long installAddQty;

    /** 报废数量 */
    @Excel(name = "报废数量")
    private Long scrappedQty;

    /** 被盗数量 */
    @Excel(name = "被盗数量")
    private Long stolenQty;

    /** 回收数量 */
    @Excel(name = "回收数量")
    private Long recoveredQty;

    /** 地点 */
    @Excel(name = "地点")
    private String location;

    /** 导师姓名 */
    @Excel(name = "导师姓名")
    private String tutorName;

    /** 审核状态 */
    @Excel(name = "审核状态")
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

    public void setBorrowId(Long borrowId) 
    {
        this.borrowId = borrowId;
    }

    public Long getBorrowId() 
    {
        return borrowId;
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

    public void setReturner(String returner) 
    {
        this.returner = returner;
    }

    public String getReturner() 
    {
        return returner;
    }

    public void setReturnTime(Date returnTime) 
    {
        this.returnTime = returnTime;
    }

    public Date getReturnTime() 
    {
        return returnTime;
    }

    public void setReturnQty(Long returnQty) 
    {
        this.returnQty = returnQty;
    }

    public Long getReturnQty() 
    {
        return returnQty;
    }

    public void setInstallAddQty(Long installAddQty) 
    {
        this.installAddQty = installAddQty;
    }

    public Long getInstallAddQty() 
    {
        return installAddQty;
    }

    public void setScrappedQty(Long scrappedQty) 
    {
        this.scrappedQty = scrappedQty;
    }

    public Long getScrappedQty() 
    {
        return scrappedQty;
    }

    public void setStolenQty(Long stolenQty) 
    {
        this.stolenQty = stolenQty;
    }

    public Long getStolenQty() 
    {
        return stolenQty;
    }

    public void setRecoveredQty(Long recoveredQty) 
    {
        this.recoveredQty = recoveredQty;
    }

    public Long getRecoveredQty() 
    {
        return recoveredQty;
    }

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
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
            .append("borrowId", getBorrowId())
            .append("equipmentId", getEquipmentId())
            .append("equipmentName", getEquipmentName())
            .append("model", getModel())
            .append("projectId", getProjectId())
            .append("projectName", getProjectName())
            .append("returner", getReturner())
            .append("returnTime", getReturnTime())
            .append("returnQty", getReturnQty())
            .append("installAddQty", getInstallAddQty())
            .append("scrappedQty", getScrappedQty())
            .append("stolenQty", getStolenQty())
            .append("recoveredQty", getRecoveredQty())
            .append("location", getLocation())
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
