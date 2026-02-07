package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 标本信息对象 specimen_info
 * 
 * @author ruoyi
 * @date 2025-07-19
 */
public class SpecimenInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 序号 */
    @Excel(name = "序号")
    private Long sequenceNo;

    /** 标本类别 */
    @Excel(name = "标本类别")
    private String specimenCategory;

    /** 标本名称 */
    @Excel(name = "标本名称")
    private String specimenName;

    /** 标本数量 */
    @Excel(name = "标本数量")
    private Long quantity;

    /** 制作日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "制作日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date productionDate;

    /** 制作人 */
    @Excel(name = "制作人")
    private String producer;

    /** 状态 */
    @Excel(name = "状态 ")
    private String status;

    /** 来源 */
    @Excel(name = "来源 ")
    private String source;

    /** 采集地 */
    @Excel(name = "采集地 ")
    private String collectionPlace;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setSequenceNo(Long sequenceNo) 
    {
        this.sequenceNo = sequenceNo;
    }

    public Long getSequenceNo() 
    {
        return sequenceNo;
    }

    public void setSpecimenCategory(String specimenCategory) 
    {
        this.specimenCategory = specimenCategory;
    }

    public String getSpecimenCategory() 
    {
        return specimenCategory;
    }

    public void setSpecimenName(String specimenName) 
    {
        this.specimenName = specimenName;
    }

    public String getSpecimenName() 
    {
        return specimenName;
    }

    public void setQuantity(Long quantity) 
    {
        this.quantity = quantity;
    }

    public Long getQuantity() 
    {
        return quantity;
    }

    public void setProductionDate(Date productionDate) 
    {
        this.productionDate = productionDate;
    }

    public Date getProductionDate() 
    {
        return productionDate;
    }

    public void setProducer(String producer) 
    {
        this.producer = producer;
    }

    public String getProducer() 
    {
        return producer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCollectionPlace() {
        return collectionPlace;
    }

    public void setCollectionPlace(String collectionPlace) {
        this.collectionPlace = collectionPlace;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("sequenceNo", getSequenceNo())
            .append("specimenCategory", getSpecimenCategory())
            .append("specimenName", getSpecimenName())
            .append("quantity", getQuantity())
            .append("productionDate", getProductionDate())
            .append("producer", getProducer())
            .append("status", getStatus())
            .append("source", getSource())
            .append("collectionPlace", getCollectionPlace())
            .toString();
    }
}
