package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 红外相机布设管理对象 camera_deployment
 *
 * @author ruoyi
 * @date 2025-07-21
 */
public class CameraDeployment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 序号 */
    @Excel(name = "序号")
    private Long sequenceNo;

    /** 批次名称 */
    @Excel(name = "批次名称")
    private String batchName;

    /** 布设区域 */
    @Excel(name = "布设区域")
    private String area;

    /** 布设开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "布设开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deploymentStart;

    /** 布设结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "布设结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deploymentEnd;

    /** 布设相机数量 */
    @Excel(name = "布设相机数量")
    private Long quantity;

    /** 负责人 */
    @Excel(name = "负责人")
    private String leader;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createdBy;

    /** 上传时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "上传时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    /** 备注 */
    @Excel(name = "备注")
    private String remarks;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String filePath;

    /** 文件大小 */
    @Excel(name = "文件大小")
    private BigDecimal fileSize;

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

    public void setBatchName(String batchName)
    {
        this.batchName = batchName;
    }

    public String getBatchName()
    {
        return batchName;
    }

    public void setArea(String area)
    {
        this.area = area;
    }

    public String getArea()
    {
        return area;
    }

    public void setDeploymentStart(Date deploymentStart)
    {
        this.deploymentStart = deploymentStart;
    }

    public Date getDeploymentStart()
    {
        return deploymentStart;
    }

    public void setDeploymentEnd(Date deploymentEnd)
    {
        this.deploymentEnd = deploymentEnd;
    }

    public Date getDeploymentEnd()
    {
        return deploymentEnd;
    }

    public void setQuantity(Long quantity)
    {
        this.quantity = quantity;
    }

    public Long getQuantity()
    {
        return quantity;
    }

    public void setLeader(String leader)
    {
        this.leader = leader;
    }

    public String getLeader()
    {
        return leader;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedTime(Date createdTime)
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime()
    {
        return createdTime;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public void setFileSize(BigDecimal fileSize)
    {
        this.fileSize = fileSize;
    }

    public BigDecimal getFileSize()
    {
        return fileSize;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("sequenceNo", getSequenceNo())
            .append("batchName", getBatchName())
            .append("area", getArea())
            .append("deploymentStart", getDeploymentStart())
            .append("deploymentEnd", getDeploymentEnd())
            .append("quantity", getQuantity())
            .append("leader", getLeader())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("remarks", getRemarks())
            .append("filePath", getFilePath())
            .append("fileSize", getFileSize())
            .toString();
    }
}
