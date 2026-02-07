package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 物种监测数据对象 busi_species_data
 * 
 * @author ruoyi
 * @date 2026-01-12
 */
public class BusiSpeciesData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long id;

    /** 归属项目ID */
    @Excel(name = "归属项目ID")
    private Long installId;

    /** 关联的具体位点ID(可选) */
    @Excel(name = "关联的具体位点ID(可选)")
    private Long pointId;

    /** Excel中的原始编号(如H21) */
    @Excel(name = "Excel中的原始编号(如H21)")
    private String siteCodeRef;

    /** 物种名称(如豹猫) */
    @Excel(name = "物种名称(如豹猫)")
    private String speciesName;

    /** 对象类别(如兽类) */
    @Excel(name = "对象类别(如兽类)")
    private String className;

    /** 拍摄次数(预留) */
    @Excel(name = "拍摄次数(预留)")
    private Long captureCount;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setInstallId(Long installId) 
    {
        this.installId = installId;
    }

    public Long getInstallId() 
    {
        return installId;
    }

    public void setPointId(Long pointId) 
    {
        this.pointId = pointId;
    }

    public Long getPointId() 
    {
        return pointId;
    }

    public void setSiteCodeRef(String siteCodeRef) 
    {
        this.siteCodeRef = siteCodeRef;
    }

    public String getSiteCodeRef() 
    {
        return siteCodeRef;
    }

    public void setSpeciesName(String speciesName) 
    {
        this.speciesName = speciesName;
    }

    public String getSpeciesName() 
    {
        return speciesName;
    }

    public void setClassName(String className) 
    {
        this.className = className;
    }

    public String getClassName() 
    {
        return className;
    }

    public void setCaptureCount(Long captureCount) 
    {
        this.captureCount = captureCount;
    }

    public Long getCaptureCount() 
    {
        return captureCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("installId", getInstallId())
            .append("pointId", getPointId())
            .append("siteCodeRef", getSiteCodeRef())
            .append("speciesName", getSpeciesName())
            .append("className", getClassName())
            .append("captureCount", getCaptureCount())
            .append("createTime", getCreateTime())
            .toString();
    }
}
