package com.ruoyi.research.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 资料清单管理对象 research_material
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
public class ResearchMaterial extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 类型（1专著 2文献 3实验数据 4研究报告 5技术文档） */
    @Excel(name = "类型", readConverterExp = "1=专著,2=文献,3=实验数据,4=研究报告,5=技术文档")
    private String type;

    /** 内容概述 */
    @Excel(name = "内容概述")
    private String contentSummary;

    /** 方向类别 */
    @Excel(name = "方向类别")
    private String directionCategory;

    /** 储存路径 */
    private String storagePath;

    /** 录入人员 */
    @Excel(name = "录入人员")
    private String inputPerson;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }

    public void setContentSummary(String contentSummary) 
    {
        this.contentSummary = contentSummary;
    }

    public String getContentSummary() 
    {
        return contentSummary;
    }

    public void setDirectionCategory(String directionCategory) 
    {
        this.directionCategory = directionCategory;
    }

    public String getDirectionCategory() 
    {
        return directionCategory;
    }

    public void setStoragePath(String storagePath) 
    {
        this.storagePath = storagePath;
    }

    public String getStoragePath() 
    {
        return storagePath;
    }

    public void setInputPerson(String inputPerson) 
    {
        this.inputPerson = inputPerson;
    }

    public String getInputPerson() 
    {
        return inputPerson;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("type", getType())
            .append("contentSummary", getContentSummary())
            .append("directionCategory", getDirectionCategory())
            .append("storagePath", getStoragePath())
            .append("inputPerson", getInputPerson())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
