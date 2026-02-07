package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 采样信息对象 sampling_information
 *
 * @author ruoyi
 * @date 2025-07-19
 */
public class SamplingInformation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 序号 */
    @Excel(name = "序号")
    private Long sequenceNo;

    /** 样本编号 */
    @NotBlank(message = "样本编号不能为空")
    @Excel(name = "样本编号")
    private String sampleName;

    /** 宿主 */
    @NotBlank(message = "宿主不能为空")
    @Excel(name = "宿主")
    private String host;

    /** 样本类型 */
    @NotBlank(message = "样本类型不能为空")
    @Excel(name = "样本类型")
    private String sampleType;

    /** 采样时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "采样时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date samplingTime;

    /** 采样地点 */
    @Excel(name = "采样地点")
    private String location;

    /** 生境类型 */
    @Excel(name = "生境类型")
    private String habitatType;

    /** 坐标经度 */
    @Excel(name = "坐标经度")
    private BigDecimal longitude;

    /** 坐标纬度 */
    @Excel(name = "坐标纬度")
    private BigDecimal latitude;

    /** 采样人员 */
    @NotBlank(message = "采样人员不能为空")
    @Excel(name = "采样人员")
    private String samplingPerson;

    /** 保存位置 */
    @NotBlank(message = "保存位置不能为空")
    @Excel(name = "保存位置")
    private String storageLocation;

    /** 样本使用记录信息 */
    private List<SampleUsageRecord> sampleUsageRecordList;

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

    public void setSamplingTime(Date samplingTime)
    {
        this.samplingTime = samplingTime;
    }

    public Date getSamplingTime()
    {
        return samplingTime;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getLocation()
    {
        return location;
    }

    public void setHabitatType(String habitatType)
    {
        this.habitatType = habitatType;
    }

    public String getHabitatType()
    {
        return habitatType;
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

    public void setSamplingPerson(String samplingPerson)
    {
        this.samplingPerson = samplingPerson;
    }

    public String getSamplingPerson()
    {
        return samplingPerson;
    }

    public void setStorageLocation(String storageLocation)
    {
        this.storageLocation = storageLocation;
    }

    public String getStorageLocation()
    {
        return storageLocation;
    }

    public List<SampleUsageRecord> getSampleUsageRecordList()
    {
        return sampleUsageRecordList;
    }

    public void setSampleUsageRecordList(List<SampleUsageRecord> sampleUsageRecordList)
    {
        this.sampleUsageRecordList = sampleUsageRecordList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("sequenceNo", getSequenceNo())
                .append("sampleName", getSampleName())
                .append("host", getHost())
                .append("sampleType", getSampleType())
                .append("samplingTime", getSamplingTime())
                .append("location", getLocation())
                .append("habitatType", getHabitatType())
                .append("longitude", getLongitude())
                .append("latitude", getLatitude())
                .append("samplingPerson", getSamplingPerson())
                .append("storageLocation", getStorageLocation())
                .append("sampleUsageRecordList", getSampleUsageRecordList())
                .toString();
    }

    public static class SampleUsageDTO {
        /** 样本ID */
        private Long sampleId;

        /** 使用数量 */
        private Integer usedQuantity;

        /** 备注 */
        private String remarks;

        public Long getSampleId() {
            return sampleId;
        }

        public void setSampleId(Long sampleId) {
            this.sampleId = sampleId;
        }

        public Integer getUsedQuantity() {
            return usedQuantity;
        }

        public void setUsedQuantity(Integer usedQuantity) {
            this.usedQuantity = usedQuantity;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        @Override
        public String toString() {
            return "SampleUsageDTO{" +
                    "sampleId=" + sampleId +
                    ", usedQuantity=" + usedQuantity +
                    ", remarks='" + remarks + '\'' +
                    '}';
        }
    }
}