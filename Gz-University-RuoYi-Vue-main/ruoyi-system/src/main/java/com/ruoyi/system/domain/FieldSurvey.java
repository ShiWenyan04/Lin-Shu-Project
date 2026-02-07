package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 外业调查设备对象 field_survey
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
public class FieldSurvey extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String equipmentName;

    /** 型号 */
    @Excel(name = "型号")
    private String model;

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal price;

    /** 销售商 */
    @Excel(name = "销售商")
    private String vendor;

    /** 采购时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "采购时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date purchaseTime;

    /** 总数量 */
    @Excel(name = "总数量")
    private Long totalQty;

    /** 可用数量 */
    @Excel(name = "可用数量")
    private Long availableQty;

    /** 外借数量 */
    @Excel(name = "外借数量")
    private Long borrowedQty;

    /** 安装数量 */
    @Excel(name = "安装数量")
    private Long installedQty;

    /** 报废数量 */
    @Excel(name = "报废数量")
    private Long scrappedQty;

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

    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }

    public void setVendor(String vendor) 
    {
        this.vendor = vendor;
    }

    public String getVendor() 
    {
        return vendor;
    }

    public void setPurchaseTime(Date purchaseTime) 
    {
        this.purchaseTime = purchaseTime;
    }

    public Date getPurchaseTime() 
    {
        return purchaseTime;
    }

    public void setTotalQty(Long totalQty) 
    {
        this.totalQty = totalQty;
    }

    public Long getTotalQty() 
    {
        return totalQty;
    }

    public void setAvailableQty(Long availableQty) 
    {
        this.availableQty = availableQty;
    }

    public Long getAvailableQty() 
    {
        return availableQty;
    }

    public void setBorrowedQty(Long borrowedQty) 
    {
        this.borrowedQty = borrowedQty;
    }

    public Long getBorrowedQty() 
    {
        return borrowedQty;
    }

    public void setInstalledQty(Long installedQty) 
    {
        this.installedQty = installedQty;
    }

    public Long getInstalledQty() 
    {
        return installedQty;
    }

    public void setScrappedQty(Long scrappedQty) 
    {
        this.scrappedQty = scrappedQty;
    }

    public Long getScrappedQty() 
    {
        return scrappedQty;
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
            .append("equipmentName", getEquipmentName())
            .append("model", getModel())
            .append("price", getPrice())
            .append("vendor", getVendor())
            .append("purchaseTime", getPurchaseTime())
            .append("totalQty", getTotalQty())
            .append("availableQty", getAvailableQty())
            .append("borrowedQty", getBorrowedQty())
            .append("installedQty", getInstalledQty())
            .append("scrappedQty", getScrappedQty())
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
