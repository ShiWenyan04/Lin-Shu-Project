package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 红外相机安装项目对象 busi_camera_install
 * 
 * @author ruoyi
 * @date 2026-01-12
 */
public class BusiCameraInstall extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 依托项目 */
    @Excel(name = "依托项目")
    private String projectName;

    /** 地点(大区位) */
    @Excel(name = "地点(大区位)")
    private String regionName;

    /** 负责人 */
    @Excel(name = "负责人")
    private String manager;

    /** 安装时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "安装时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date installTime;

    /** 回收时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "回收时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date retrieveTime;

    /** 相机台数 */
    @Excel(name = "相机台数")
    private Long deviceCount;

    /** 点位KML文件路径 */
    @Excel(name = "点位KML文件路径")
    private String kmlFilePath;

    /** 边界KMZ/SHP路径 */
    @Excel(name = "边界KMZ/SHP路径")
    private String boundaryFile;

    /** 物种数据Excel路径 */
    @Excel(name = "物种数据Excel路径")
    private String excelFilePath;

    /** 解析后的边界坐标JSON */
    private String boundaryJson;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 点位文件名 (新增) */
    @Excel(name = "点位文件名")
    private String pointFileName;

    /** 边界文件名 (新增) */
    @Excel(name = "边界文件名")
    private String boundaryFileName;
    /** 审核状态(0=待审核,1=已通过,2=已驳回) */
    @Excel(name = "审核状态", readConverterExp = "0=待审核,1=已通过,2=已驳回")
    private String auditStatus;

    /** 审核/驳回意见 */
    @Excel(name = "审核意见")
    private String auditReason;

    // --- 新增审核字段 Getter/Setter ---
    public void setAuditStatus(String auditStatus) { this.auditStatus = auditStatus; }
    public String getAuditStatus() { return auditStatus; }

    public void setAuditReason(String auditReason) { this.auditReason = auditReason; }
    public String getAuditReason() { return auditReason; }


    // ============ 新增 Getter/Setter ============
    public void setPointFileName(String pointFileName)
    {
        this.pointFileName = pointFileName;
    }

    public String getPointFileName()
    {
        return pointFileName;
    }

    public void setBoundaryFileName(String boundaryFileName)
    {
        this.boundaryFileName = boundaryFileName;
    }

    public String getBoundaryFileName()
    {
        return boundaryFileName;
    }

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setProjectName(String projectName) 
    {
        this.projectName = projectName;
    }

    public String getProjectName() 
    {
        return projectName;
    }

    public void setRegionName(String regionName) 
    {
        this.regionName = regionName;
    }

    public String getRegionName() 
    {
        return regionName;
    }

    public void setManager(String manager) 
    {
        this.manager = manager;
    }

    public String getManager() 
    {
        return manager;
    }

    public void setInstallTime(Date installTime) 
    {
        this.installTime = installTime;
    }

    public Date getInstallTime() 
    {
        return installTime;
    }

    public void setRetrieveTime(Date retrieveTime) 
    {
        this.retrieveTime = retrieveTime;
    }

    public Date getRetrieveTime() 
    {
        return retrieveTime;
    }

    public void setDeviceCount(Long deviceCount) 
    {
        this.deviceCount = deviceCount;
    }

    public Long getDeviceCount() 
    {
        return deviceCount;
    }

    public void setKmlFilePath(String kmlFilePath) 
    {
        this.kmlFilePath = kmlFilePath;
    }

    public String getKmlFilePath() 
    {
        return kmlFilePath;
    }

    public void setBoundaryFile(String boundaryFile) 
    {
        this.boundaryFile = boundaryFile;
    }

    public String getBoundaryFile() 
    {
        return boundaryFile;
    }

    public void setExcelFilePath(String excelFilePath) 
    {
        this.excelFilePath = excelFilePath;
    }

    public String getExcelFilePath() 
    {
        return excelFilePath;
    }

    public void setBoundaryJson(String boundaryJson) 
    {
        this.boundaryJson = boundaryJson;
    }

    public String getBoundaryJson() 
    {
        return boundaryJson;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("projectName", getProjectName())
            .append("regionName", getRegionName())
            .append("manager", getManager())
            .append("installTime", getInstallTime())
            .append("retrieveTime", getRetrieveTime())
            .append("deviceCount", getDeviceCount())
            .append("kmlFilePath", getKmlFilePath())
            .append("boundaryFile", getBoundaryFile())
            .append("excelFilePath", getExcelFilePath())
            .append("boundaryJson", getBoundaryJson())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("auditStatus", getAuditStatus()) // 记得toString加一下
            .append("auditReason", getAuditReason())
            .toString();
    }
}
