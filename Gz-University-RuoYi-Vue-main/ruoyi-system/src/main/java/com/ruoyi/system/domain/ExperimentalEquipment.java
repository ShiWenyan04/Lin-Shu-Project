package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 实验设备信息对象 experimental_equipment
 * 
 * @author ruoyi
 * @date 2025-12-16
 */
public class ExperimentalEquipment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 型号 */
    @Excel(name = "型号")
    private String model;

    /** 资产编号 */
    @Excel(name = "资产编号")
    private String assetNo;

    /** 存放位置 */
    @Excel(name = "存放位置")
    private String location;

    /** 购买时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "购买时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date purchaseTime;

    /** 设备状态 */
    @Excel(name = "设备状态")
    private String equipmentStatus;

    /** 审核状态 */
    @Excel(name = "审核状态")
    private String auditStatus;

    /** 审核驳回原因 */
    @Excel(name = "审核驳回原因")
    private String auditReason;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setDeviceName(String deviceName) 
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName() 
    {
        return deviceName;
    }

    public void setModel(String model) 
    {
        this.model = model;
    }

    public String getModel() 
    {
        return model;
    }

    public void setAssetNo(String assetNo) 
    {
        this.assetNo = assetNo;
    }

    public String getAssetNo() 
    {
        return assetNo;
    }

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }

    public void setPurchaseTime(Date purchaseTime) 
    {
        this.purchaseTime = purchaseTime;
    }

    public Date getPurchaseTime() 
    {
        return purchaseTime;
    }

    public void setEquipmentStatus(String equipmentStatus) 
    {
        this.equipmentStatus = equipmentStatus;
    }

    public String getEquipmentStatus() 
    {
        return equipmentStatus;
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
            .append("deviceName", getDeviceName())
            .append("model", getModel())
            .append("assetNo", getAssetNo())
            .append("location", getLocation())
            .append("purchaseTime", getPurchaseTime())
            .append("equipmentStatus", getEquipmentStatus())
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
