package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 自媒体发布对象 busi_media_publish
 * 
 * @author ruoyi
 * @date 2026-01-09
 */
public class BusiMediaPublish extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 发布类型 */
    @Excel(name = "发布类型")
    private String publishType;

    /** 发布题目 */
    @Excel(name = "发布题目")
    private String title;

    /** 发布日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发布日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date publishDate;

    /** 发布链接 */
    @Excel(name = "发布链接")
    private String linkUrl;

    /** 撰稿人 */
    @Excel(name = "撰稿人")
    private String author;

    /** 一审人(文稿审查) */
    @Excel(name = "一审人(文稿审查)")
    private String reviewerFirst;

    /** 二审人(文稿及政治检查) */
    @Excel(name = "二审人(文稿及政治检查)")
    private String reviewerSecond;

    /** 终审人 */
    @Excel(name = "终审人")
    private String reviewerFinal;

    /** 剪辑人 */
    @Excel(name = "剪辑人")
    private String videoEditor;

    /** 审查人 */
    @Excel(name = "审查人")
    private String videoReviewer;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setPublishType(String publishType) 
    {
        this.publishType = publishType;
    }

    public String getPublishType() 
    {
        return publishType;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setPublishDate(Date publishDate) 
    {
        this.publishDate = publishDate;
    }

    public Date getPublishDate() 
    {
        return publishDate;
    }

    public void setLinkUrl(String linkUrl) 
    {
        this.linkUrl = linkUrl;
    }

    public String getLinkUrl() 
    {
        return linkUrl;
    }

    public void setAuthor(String author) 
    {
        this.author = author;
    }

    public String getAuthor() 
    {
        return author;
    }

    public void setReviewerFirst(String reviewerFirst) 
    {
        this.reviewerFirst = reviewerFirst;
    }

    public String getReviewerFirst() 
    {
        return reviewerFirst;
    }

    public void setReviewerSecond(String reviewerSecond) 
    {
        this.reviewerSecond = reviewerSecond;
    }

    public String getReviewerSecond() 
    {
        return reviewerSecond;
    }

    public void setReviewerFinal(String reviewerFinal) 
    {
        this.reviewerFinal = reviewerFinal;
    }

    public String getReviewerFinal() 
    {
        return reviewerFinal;
    }

    public void setVideoEditor(String videoEditor) 
    {
        this.videoEditor = videoEditor;
    }

    public String getVideoEditor() 
    {
        return videoEditor;
    }

    public void setVideoReviewer(String videoReviewer) 
    {
        this.videoReviewer = videoReviewer;
    }

    public String getVideoReviewer() 
    {
        return videoReviewer;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("publishType", getPublishType())
            .append("title", getTitle())
            .append("publishDate", getPublishDate())
            .append("linkUrl", getLinkUrl())
            .append("author", getAuthor())
            .append("reviewerFirst", getReviewerFirst())
            .append("reviewerSecond", getReviewerSecond())
            .append("reviewerFinal", getReviewerFinal())
            .append("videoEditor", getVideoEditor())
            .append("videoReviewer", getVideoReviewer())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
