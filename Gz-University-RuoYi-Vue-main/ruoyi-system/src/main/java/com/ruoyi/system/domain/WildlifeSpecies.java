package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 野生动物图鉴对象 wildlife_species
 * 
 * @author ruoyi
 * @date 2025-07-26
 */
public class WildlifeSpecies extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 序号 */
    @Excel(name = "序号")
    private Long sequenceNo;

    /** 物种名称 */
    @Excel(name = "物种名称")
    private String speciesName;

    /** 物种类别 */
    @Excel(name = "物种类别")
    private String objectCategory;

    /** 拍摄地点 */
    @Excel(name = "拍摄地点")
    private String location;

    /** 拍摄时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "拍摄时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date photoTime;

    /** 照片 */
    @Excel(name = "照片")
    private String photoPath;

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

    public void setSpeciesName(String speciesName) 
    {
        this.speciesName = speciesName;
    }

    public String getSpeciesName() 
    {
        return speciesName;
    }

    public void setObjectCategory(String objectCategory) 
    {
        this.objectCategory = objectCategory;
    }

    public String getObjectCategory() 
    {
        return objectCategory;
    }

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }

    public void setPhotoTime(Date photoTime) 
    {
        this.photoTime = photoTime;
    }

    public Date getPhotoTime() 
    {
        return photoTime;
    }

    public void setPhotoPath(String photoPath) 
    {
        this.photoPath = photoPath;
    }

    public String getPhotoPath() 
    {
        return photoPath;
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
            .append("speciesName", getSpeciesName())
            .append("objectCategory", getObjectCategory())
            .append("location", getLocation())
            .append("photoTime", getPhotoTime())
            .append("photoPath", getPhotoPath())
            .append("remarks", getRemarks())
            .toString();
    }
}
