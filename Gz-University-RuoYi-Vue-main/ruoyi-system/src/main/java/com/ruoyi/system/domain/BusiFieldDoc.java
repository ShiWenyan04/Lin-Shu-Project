package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 外业文档模板对象 busi_field_doc
 * 
 * @author ruoyi
 * @date 2025-12-22
 */
public class BusiFieldDoc extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long docId;

    /** 文档名称 */
    @Excel(name = "文档名称")
    private String docName;

    /** 文档分类 */
    @Excel(name = "文档分类")
    private String docCategory;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String fileUrl;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Long sortOrder;

    public void setDocId(Long docId) 
    {
        this.docId = docId;
    }

    public Long getDocId() 
    {
        return docId;
    }

    public void setDocName(String docName) 
    {
        this.docName = docName;
    }

    public String getDocName() 
    {
        return docName;
    }

    public void setDocCategory(String docCategory) 
    {
        this.docCategory = docCategory;
    }

    public String getDocCategory() 
    {
        return docCategory;
    }

    public void setFileUrl(String fileUrl) 
    {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() 
    {
        return fileUrl;
    }

    public void setSortOrder(Long sortOrder) 
    {
        this.sortOrder = sortOrder;
    }

    public Long getSortOrder() 
    {
        return sortOrder;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("docId", getDocId())
            .append("docName", getDocName())
            .append("docCategory", getDocCategory())
            .append("fileUrl", getFileUrl())
            .append("sortOrder", getSortOrder())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
