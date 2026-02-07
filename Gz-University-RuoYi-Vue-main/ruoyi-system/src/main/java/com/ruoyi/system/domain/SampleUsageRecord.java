package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 样本使用记录对象 sample_usage_record
 */
public class SampleUsageRecord
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 样本ID（关联采样信息表） */
    private Long sampleId;

    /** 样本编号 */
    @Excel(name = "样本编号")
    private String sampleCode;  // 改为驼峰命名

    /** 宿主 */
    @Excel(name = "宿主")
    private String host;

    /** 样本类型 */
    @Excel(name = "样本类型")
    private String sampleType;

    /** 采样地点 */
    @Excel(name = "采样地点")
    private String samplingLocation;  // 改为驼峰命名

    /** 生境类型 */
    @Excel(name = "生境类型")
    private String habitatType;

    /** 经度 */
    @Excel(name = "经度")
    private BigDecimal longitude;

    /** 纬度 */
    @Excel(name = "纬度")
    private BigDecimal latitude;

    /** 采样时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "采样时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date samplingTime;

    /** 采样人员 */
    @Excel(name = "采样人员")
    private String sampler;

    /** 保存位置 */
    @Excel(name = "保存位置")
    private String storageLocation;

    /** 使用人员 */
    @Excel(name = "使用人员")
    private String usedBy;

    /** 用样时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "用样时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date usedTime;

    /** 用途 */
    @Excel(name = "用途")
    private String purpose;

    // getter和setter方法
    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setSampleId(Long sampleId)
    {
        this.sampleId = sampleId;
    }

    public Long getSampleId()
    {
        return sampleId;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getSamplingLocation() {
        return samplingLocation;
    }

    public void setSamplingLocation(String samplingLocation) {
        this.samplingLocation = samplingLocation;
    }

    public String getHabitatType() {
        return habitatType;
    }

    public void setHabitatType(String habitatType) {
        this.habitatType = habitatType;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public Date getSamplingTime() {
        return samplingTime;
    }

    public void setSamplingTime(Date samplingTime) {
        this.samplingTime = samplingTime;
    }

    public String getSampler() {
        return sampler;
    }

    public void setSampler(String sampler) {
        this.sampler = sampler;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public void setUsedBy(String usedBy)
    {
        this.usedBy = usedBy;
    }

    public String getUsedBy()
    {
        return usedBy;
    }

    public void setUsedTime(Date usedTime)
    {
        this.usedTime = usedTime;
    }

    public Date getUsedTime()
    {
        return usedTime;
    }

    public void setPurpose(String purpose)
    {
        this.purpose = purpose;
    }

    public String getPurpose()
    {
        return purpose;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("sampleId", getSampleId())
                .append("sampleCode", getSampleCode())
                .append("host", getHost())
                .append("sampleType", getSampleType())
                .append("samplingLocation", getSamplingLocation())
                .append("habitatType", getHabitatType())
                .append("longitude", getLongitude())
                .append("latitude", getLatitude())
                .append("samplingTime", getSamplingTime())
                .append("sampler", getSampler())
                .append("storageLocation", getStorageLocation())
                .append("usedBy", getUsedBy())
                .append("usedTime", getUsedTime())
                .append("purpose", getPurpose())
                .toString();
    }
}