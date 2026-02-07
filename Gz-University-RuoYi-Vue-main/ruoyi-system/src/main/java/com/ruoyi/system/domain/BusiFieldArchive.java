package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 外业数据归档对象 busi_field_archive
 * 
 * @author ruoyi
 * @date 2025-12-22
 */
public class BusiFieldArchive extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long archiveId;

    /** 关联外业启动ID */
    @Excel(name = "关联外业启动ID")
    private Long startId;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 地点 */
    @Excel(name = "地点")
    private String location;

    /** 归档人 */
    @Excel(name = "归档人")
    private String archiverName;

    /** 外业负责人 */
    @Excel(name = "外业负责人")
    private String leaderName;

    /** 工作照路径 */
    @Excel(name = "工作照路径")
    private String workPhotoUrl;

    /** 物种照路径 */
    @Excel(name = "物种照路径")
    private String speciesPhotoUrl;

    /** 生境照路径 */
    @Excel(name = "生境照路径")
    private String habitatPhotoUrl;

    /** 奥维数据路径 */
    @Excel(name = "奥维数据路径")
    private String ovitalDataUrl;

    /** 红外相机图像路径 */
    @Excel(name = "红外相机图像路径")
    private String cameraDataUrl;

    /** 信息表路径 */
    @Excel(name = "信息表路径")
    private String infoSheetUrl;

    /** 审核状态 */
    @Excel(name = "审核状态")
    private String auditStatus;

    /** 审核意见 */
    @Excel(name = "审核意见")
    private String auditReason;

    public void setArchiveId(Long archiveId) 
    {
        this.archiveId = archiveId;
    }

    public Long getArchiveId() 
    {
        return archiveId;
    }

    public void setStartId(Long startId) 
    {
        this.startId = startId;
    }

    public Long getStartId() 
    {
        return startId;
    }

    public void setProjectName(String projectName) 
    {
        this.projectName = projectName;
    }

    public String getProjectName() 
    {
        return projectName;
    }

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }

    public void setArchiverName(String archiverName) 
    {
        this.archiverName = archiverName;
    }

    public String getArchiverName() 
    {
        return archiverName;
    }

    public void setLeaderName(String leaderName) 
    {
        this.leaderName = leaderName;
    }

    public String getLeaderName() 
    {
        return leaderName;
    }

    public void setWorkPhotoUrl(String workPhotoUrl) 
    {
        this.workPhotoUrl = workPhotoUrl;
    }

    public String getWorkPhotoUrl() 
    {
        return workPhotoUrl;
    }

    public void setSpeciesPhotoUrl(String speciesPhotoUrl) 
    {
        this.speciesPhotoUrl = speciesPhotoUrl;
    }

    public String getSpeciesPhotoUrl() 
    {
        return speciesPhotoUrl;
    }

    public void setHabitatPhotoUrl(String habitatPhotoUrl) 
    {
        this.habitatPhotoUrl = habitatPhotoUrl;
    }

    public String getHabitatPhotoUrl() 
    {
        return habitatPhotoUrl;
    }

    public void setOvitalDataUrl(String ovitalDataUrl) 
    {
        this.ovitalDataUrl = ovitalDataUrl;
    }

    public String getOvitalDataUrl() 
    {
        return ovitalDataUrl;
    }

    public void setCameraDataUrl(String cameraDataUrl) 
    {
        this.cameraDataUrl = cameraDataUrl;
    }

    public String getCameraDataUrl() 
    {
        return cameraDataUrl;
    }

    public void setInfoSheetUrl(String infoSheetUrl) 
    {
        this.infoSheetUrl = infoSheetUrl;
    }

    public String getInfoSheetUrl() 
    {
        return infoSheetUrl;
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
            .append("archiveId", getArchiveId())
            .append("startId", getStartId())
            .append("projectName", getProjectName())
            .append("location", getLocation())
            .append("archiverName", getArchiverName())
            .append("leaderName", getLeaderName())
            .append("workPhotoUrl", getWorkPhotoUrl())
            .append("speciesPhotoUrl", getSpeciesPhotoUrl())
            .append("habitatPhotoUrl", getHabitatPhotoUrl())
            .append("ovitalDataUrl", getOvitalDataUrl())
            .append("cameraDataUrl", getCameraDataUrl())
            .append("infoSheetUrl", getInfoSheetUrl())
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
