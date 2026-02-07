package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 红外相机数据对象 camera_statistics
 * 
 * @author ruoyi
 * @date 2025-07-26
 */
public class CameraStatistics extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 序号 */
    @Excel(name = "序号")
    private Long sequenceNo;

    /** 布设区域 */
    @Excel(name = "布设区域")
    private String area;

    /** 存储路径 */
    @Excel(name = "存储路径")
    private String storagePath;

    /** 文件编号 */
    @Excel(name = "文件编号")
    private String fileNo;

    /** 原始文件编号 */
    @Excel(name = "原始文件编号")
    private String originalFileNo;

    /** 文件格式 */
    @Excel(name = "文件格式")
    private String fileFormat;

    /** 文件夹编号 */
    @Excel(name = "文件夹编号")
    private String folderNo;

    /** 相机编号 */
    @Excel(name = "相机编号")
    private String cameraNo;

    /** 布设位点编号 */
    @Excel(name = "布设位点编号")
    private String siteNo;

    /** 拍摄日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "拍摄日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date photoDate;

    /** 拍摄时间 */
    @JsonFormat(pattern = "HH:mm:ss")
    @Excel(name = "拍摄时间", width = 30, dateFormat = "HH:mm:ss")
    private Date photoTime;

    /** 工作天数 */
    @Excel(name = "工作天数")
    private Long workingDays;

    /** 对象类别 */
    @Excel(name = "对象类别")
    private String objectCategory;

    /** 物种名称 */
    @Excel(name = "物种名称")
    private String speciesName;

    /** 动物数量 */
    @Excel(name = "动物数量")
    private Long animalCount;

    /** 性别 */
    @Excel(name = "性别")
    private String gender;

    /** 独立探测首张 */
    @Excel(name = "独立探测首张")
    private Long firstPhoto;

    /** 有效照片 */
    @Excel(name = "有效照片")
    private Long validPhotos;

    /** 温度 */
    @Excel(name = "温度")
    private Long temperature;

    /** 备注 */
    @Excel(name = "备注")
    private String remarks;

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

    public void setArea(String area) 
    {
        this.area = area;
    }

    public String getArea() 
    {
        return area;
    }

    public void setStoragePath(String storagePath) 
    {
        this.storagePath = storagePath;
    }

    public String getStoragePath() 
    {
        return storagePath;
    }

    public void setFileNo(String fileNo) 
    {
        this.fileNo = fileNo;
    }

    public String getFileNo() 
    {
        return fileNo;
    }

    public void setOriginalFileNo(String originalFileNo) 
    {
        this.originalFileNo = originalFileNo;
    }

    public String getOriginalFileNo() 
    {
        return originalFileNo;
    }

    public void setFileFormat(String fileFormat) 
    {
        this.fileFormat = fileFormat;
    }

    public String getFileFormat() 
    {
        return fileFormat;
    }

    public void setFolderNo(String folderNo) 
    {
        this.folderNo = folderNo;
    }

    public String getFolderNo() 
    {
        return folderNo;
    }

    public void setCameraNo(String cameraNo) 
    {
        this.cameraNo = cameraNo;
    }

    public String getCameraNo() 
    {
        return cameraNo;
    }

    public void setSiteNo(String siteNo) 
    {
        this.siteNo = siteNo;
    }

    public String getSiteNo() 
    {
        return siteNo;
    }

    public void setPhotoDate(Date photoDate) 
    {
        this.photoDate = photoDate;
    }

    public Date getPhotoDate() 
    {
        return photoDate;
    }

    public void setPhotoTime(Date photoTime) 
    {
        this.photoTime = photoTime;
    }

    public Date getPhotoTime() 
    {
        return photoTime;
    }

    public void setWorkingDays(Long workingDays) 
    {
        this.workingDays = workingDays;
    }

    public Long getWorkingDays() 
    {
        return workingDays;
    }

    public void setObjectCategory(String objectCategory) 
    {
        this.objectCategory = objectCategory;
    }

    public String getObjectCategory() 
    {
        return objectCategory;
    }

    public void setSpeciesName(String speciesName) 
    {
        this.speciesName = speciesName;
    }

    public String getSpeciesName() 
    {
        return speciesName;
    }

    public void setAnimalCount(Long animalCount) 
    {
        this.animalCount = animalCount;
    }

    public Long getAnimalCount() 
    {
        return animalCount;
    }

    public void setGender(String gender) 
    {
        this.gender = gender;
    }

    public String getGender() 
    {
        return gender;
    }

    public void setFirstPhoto(Long firstPhoto) 
    {
        this.firstPhoto = firstPhoto;
    }

    public Long getFirstPhoto() 
    {
        return firstPhoto;
    }

    public void setValidPhotos(Long validPhotos) 
    {
        this.validPhotos = validPhotos;
    }

    public Long getValidPhotos() 
    {
        return validPhotos;
    }

    public void setTemperature(Long temperature) 
    {
        this.temperature = temperature;
    }

    public Long getTemperature() 
    {
        return temperature;
    }

    public void setRemarks(String remarks) 
    {
        this.remarks = remarks;
    }

    public String getRemarks() 
    {
        return remarks;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("sequenceNo", getSequenceNo())
            .append("area", getArea())
            .append("storagePath", getStoragePath())
            .append("fileNo", getFileNo())
            .append("originalFileNo", getOriginalFileNo())
            .append("fileFormat", getFileFormat())
            .append("folderNo", getFolderNo())
            .append("cameraNo", getCameraNo())
            .append("siteNo", getSiteNo())
            .append("photoDate", getPhotoDate())
            .append("photoTime", getPhotoTime())
            .append("workingDays", getWorkingDays())
            .append("objectCategory", getObjectCategory())
            .append("speciesName", getSpeciesName())
            .append("animalCount", getAnimalCount())
            .append("gender", getGender())
            .append("firstPhoto", getFirstPhoto())
            .append("validPhotos", getValidPhotos())
            .append("temperature", getTemperature())
            .append("remarks", getRemarks())
            .toString();
    }
}
