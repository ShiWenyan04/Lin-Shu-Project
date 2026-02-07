package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 购买支出信息对象 biz_purchase
 * 
 * @author ruoyi
 * @date 2025-12-17
 */
public class BizPurchase extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 物品名称 */
    @Excel(name = "物品名称")
    private String itemName;

    /** 类型 */
    @Excel(name = "类型")
    private String itemType;

    /** 型号 */
    @Excel(name = "型号")
    private String model;

    /** 采购人员 */
    @Excel(name = "采购人员")
    private String purchaser;

    /** 采购公司 */
    @Excel(name = "采购公司")
    private String companyName;

    /** 数量 */
    @Excel(name = "数量")
    private Long quantity;

    /** 价格 */
    @Excel(name = "价格")
    private BigDecimal price;

    /** 用途 */
    @Excel(name = "用途")
    private String usagePurpose;

    /** 发票上传 */
    @Excel(name = "发票上传")
    private String invoiceUrl;

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

    public void setItemName(String itemName) 
    {
        this.itemName = itemName;
    }

    public String getItemName() 
    {
        return itemName;
    }

    public void setItemType(String itemType) 
    {
        this.itemType = itemType;
    }

    public String getItemType() 
    {
        return itemType;
    }

    public void setModel(String model) 
    {
        this.model = model;
    }

    public String getModel() 
    {
        return model;
    }

    public void setPurchaser(String purchaser) 
    {
        this.purchaser = purchaser;
    }

    public String getPurchaser() 
    {
        return purchaser;
    }

    public void setCompanyName(String companyName) 
    {
        this.companyName = companyName;
    }

    public String getCompanyName() 
    {
        return companyName;
    }

    public void setQuantity(Long quantity) 
    {
        this.quantity = quantity;
    }

    public Long getQuantity() 
    {
        return quantity;
    }

    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }

    public void setUsagePurpose(String usagePurpose) 
    {
        this.usagePurpose = usagePurpose;
    }

    public String getUsagePurpose() 
    {
        return usagePurpose;
    }

    public void setInvoiceUrl(String invoiceUrl) 
    {
        this.invoiceUrl = invoiceUrl;
    }

    public String getInvoiceUrl() 
    {
        return invoiceUrl;
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
            .append("itemName", getItemName())
            .append("itemType", getItemType())
            .append("model", getModel())
            .append("purchaser", getPurchaser())
            .append("companyName", getCompanyName())
            .append("quantity", getQuantity())
            .append("price", getPrice())
            .append("usagePurpose", getUsagePurpose())
            .append("invoiceUrl", getInvoiceUrl())
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
