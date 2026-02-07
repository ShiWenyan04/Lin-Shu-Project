package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 物种百科资料库对象 busi_species_library
 * 
 * @author ruoyi
 * @date 2026-01-15
 */
public class BusiSpeciesLibrary extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 物种名称(关联键) */
    @Excel(name = "物种名称(关联键)")
    private String speciesName;

    /** 拉丁学名 */
    @Excel(name = "拉丁学名")
    private String scientificName;

    /** 别名/俗名 */
    @Excel(name = "别名/俗名")
    private String alias;

    /** 国家保护级别(一级/二级/三有) */
    @Excel(name = "国家保护级别(一级/二级/三有)")
    private String protectionLevel;

    /** 食性(食肉/食草/杂食) */
    @Excel(name = "食性(食肉/食草/杂食)")
    private String dietType;

    /** 活动节律(夜行/昼行/晨昏) */
    @Excel(name = "活动节律(夜行/昼行/晨昏)")
    private String activityPattern;

    /** 典型生境 */
    @Excel(name = "典型生境")
    private String habitat;

    /** 形态特征与习性描述 */
    @Excel(name = "形态特征与习性描述")
    private String description;

    /** 物种封面图路径 */
    @Excel(name = "物种封面图路径")
    private String coverImage;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setSpeciesName(String speciesName) 
    {
        this.speciesName = speciesName;
    }

    public String getSpeciesName() 
    {
        return speciesName;
    }

    public void setScientificName(String scientificName) 
    {
        this.scientificName = scientificName;
    }

    public String getScientificName() 
    {
        return scientificName;
    }

    public void setAlias(String alias) 
    {
        this.alias = alias;
    }

    public String getAlias() 
    {
        return alias;
    }

    public void setProtectionLevel(String protectionLevel) 
    {
        this.protectionLevel = protectionLevel;
    }

    public String getProtectionLevel() 
    {
        return protectionLevel;
    }

    public void setDietType(String dietType) 
    {
        this.dietType = dietType;
    }

    public String getDietType() 
    {
        return dietType;
    }

    public void setActivityPattern(String activityPattern) 
    {
        this.activityPattern = activityPattern;
    }

    public String getActivityPattern() 
    {
        return activityPattern;
    }

    public void setHabitat(String habitat) 
    {
        this.habitat = habitat;
    }

    public String getHabitat() 
    {
        return habitat;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setCoverImage(String coverImage) 
    {
        this.coverImage = coverImage;
    }

    public String getCoverImage() 
    {
        return coverImage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("speciesName", getSpeciesName())
            .append("scientificName", getScientificName())
            .append("alias", getAlias())
            .append("protectionLevel", getProtectionLevel())
            .append("dietType", getDietType())
            .append("activityPattern", getActivityPattern())
            .append("habitat", getHabitat())
            .append("description", getDescription())
            .append("coverImage", getCoverImage())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
