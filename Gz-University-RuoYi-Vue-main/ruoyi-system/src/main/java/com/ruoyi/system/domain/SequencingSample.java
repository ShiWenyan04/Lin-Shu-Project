package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 测序信息对象 sequencing_sample
 *
 * @author ruoyi
 * @date 2025-07-20
 */
public class SequencingSample extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 测序编号 */
    @Excel(name = "测序编号")
    private Integer sequencingNo;

    /** 样本编号 */
    @Excel(name = "样本编号")
    private String sampleName;

    /** 宿主 */
    @Excel(name = "宿主")
    private String host;

    /** 样本类型 */
    @Excel(name = "样本类型")
    private String sampleType;

    /** 采样地点 */
    @Excel(name = "采样地点")
    private String samplingLocation;

    /** 生境类型 */
    @Excel(name = "生境类型")
    private String habitatType;

    /** 经度 */
    @Excel(name = "经度")
    private Double longitude;

    /** 纬度 */
    @Excel(name = "纬度")
    private Double latitude;

    /** 采样时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "采样时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date samplingTime;

    /** 采样人员 */
    @Excel(name = "采样人员")
    private String samplingPerson;

    /** 测序类型 */
    @Excel(name = "测序类型")
    private String sequencingType;

    /** 测序者 */
    @Excel(name = "测序者")
    private String sequencer;

    /** 测序公司 */
    @Excel(name = "测序公司")
    private String sequencingCompany;

    /** 测序时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "测序时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sequencingDate;

    /** 存储位置 */
    @Excel(name = "存储位置")
    private String storageLocation;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setSequencingNo(Integer sequencingNo)
    {
        this.sequencingNo = sequencingNo;
    }

    public Integer getSequencingNo()
    {
        return sequencingNo;
    }

    public void setSampleName(String sampleName)
    {
        this.sampleName = sampleName;
    }

    public String getSampleName()
    {
        return sampleName;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public String getHost()
    {
        return host;
    }

    public void setSampleType(String sampleType)
    {
        this.sampleType = sampleType;
    }

    public String getSampleType()
    {
        return sampleType;
    }

    public void setSamplingLocation(String samplingLocation)
    {
        this.samplingLocation = samplingLocation;
    }

    public String getSamplingLocation()
    {
        return samplingLocation;
    }

    public void setHabitatType(String habitatType)
    {
        this.habitatType = habitatType;
    }

    public String getHabitatType()
    {
        return habitatType;
    }

    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }

    public Double getLongitude()
    {
        return longitude;
    }

    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }

    public Double getLatitude()
    {
        return latitude;
    }

    public void setSamplingTime(Date samplingTime)
    {
        this.samplingTime = samplingTime;
    }

    public Date getSamplingTime()
    {
        return samplingTime;
    }

    public void setSamplingPerson(String samplingPerson)
    {
        this.samplingPerson = samplingPerson;
    }

    public String getSamplingPerson()
    {
        return samplingPerson;
    }

    public void setSequencingType(String sequencingType)
    {
        this.sequencingType = sequencingType;
    }

    public String getSequencingType()
    {
        return sequencingType;
    }

    public void setSequencer(String sequencer)
    {
        this.sequencer = sequencer;
    }

    public String getSequencer()
    {
        return sequencer;
    }

    public void setSequencingCompany(String sequencingCompany)
    {
        this.sequencingCompany = sequencingCompany;
    }

    public String getSequencingCompany()
    {
        return sequencingCompany;
    }

    public void setSequencingDate(Date sequencingDate)
    {
        this.sequencingDate = sequencingDate;
    }

    public Date getSequencingDate()
    {
        return sequencingDate;
    }

    public void setStorageLocation(String storageLocation)
    {
        this.storageLocation = storageLocation;
    }

    public String getStorageLocation()
    {
        return storageLocation;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("sequencingNo", getSequencingNo())
                .append("sampleName", getSampleName())
                .append("host", getHost())
                .append("sampleType", getSampleType())
                .append("samplingLocation", getSamplingLocation())
                .append("habitatType", getHabitatType())
                .append("longitude", getLongitude())
                .append("latitude", getLatitude())
                .append("samplingTime", getSamplingTime())
                .append("samplingPerson", getSamplingPerson())
                .append("sequencingType", getSequencingType())
                .append("sequencer", getSequencer())
                .append("sequencingCompany", getSequencingCompany())
                .append("sequencingDate", getSequencingDate())
                .append("storageLocation", getStorageLocation())
                .toString();
    }
}