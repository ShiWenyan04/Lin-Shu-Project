package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 红外相机位点信息对象 busi_camera_point
 * 
 * @author ruoyi
 * @date 2026-01-12
 */
public class BusiCameraPoint extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 位点ID */
    private Long id;

    /** 归属项目ID(关联install表) */
    @Excel(name = "归属项目ID(关联install表)")
    private Long installId;

    /** 位点编号(如H21-1) */
    @Excel(name = "位点编号(如H21-1)")
    private String siteCode;

    /** 经度 */
    @Excel(name = "经度")
    private BigDecimal longitude;

    /** 纬度 */
    @Excel(name = "纬度")
    private BigDecimal latitude;

    /** 海拔 */
    @Excel(name = "海拔")
    private BigDecimal altitude;

    private String speciesNames;

    public String getSpeciesNames() {
        return speciesNames;
    }

    public void setSpeciesNames(String speciesNames) {
        this.speciesNames = speciesNames;
    }

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

    public void setSiteCode(String siteCode) 
    {
        this.siteCode = siteCode;
    }

    public String getSiteCode() 
    {
        return siteCode;
    }

    public void setLongitude(BigDecimal longitude) 
    {
        this.longitude = longitude;
    }

    public BigDecimal getLongitude() 
    {
        return longitude;
    }

    public void setLatitude(BigDecimal latitude) 
    {
        this.latitude = latitude;
    }

    public BigDecimal getLatitude() 
    {
        return latitude;
    }

    public void setAltitude(BigDecimal altitude) 
    {
        this.altitude = altitude;
    }

    public BigDecimal getAltitude() 
    {
        return altitude;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("installId", getInstallId())
            .append("siteCode", getSiteCode())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("altitude", getAltitude())
            .append("createTime", getCreateTime())
            .toString();
    }
}
