package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 特种设备考证信息对象 special_info
 * 
 * @author ruoyi
 * @date 2025-12-16
 */
public class SpecialInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 考证人员 */
    @Excel(name = "考证人员")
    private String examinee;

    /** 考证时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "考证时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date examTime;

    /** 到期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "到期时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expireTime;

    /** 附件路径 */
    @Excel(name = "附件路径")
    private String attachment;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setExaminee(String examinee) 
    {
        this.examinee = examinee;
    }

    public String getExaminee() 
    {
        return examinee;
    }

    public void setExamTime(Date examTime) 
    {
        this.examTime = examTime;
    }

    public Date getExamTime() 
    {
        return examTime;
    }

    public void setExpireTime(Date expireTime) 
    {
        this.expireTime = expireTime;
    }

    public Date getExpireTime() 
    {
        return expireTime;
    }

    public void setAttachment(String attachment) 
    {
        this.attachment = attachment;
    }

    public String getAttachment() 
    {
        return attachment;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("examinee", getExaminee())
            .append("examTime", getExamTime())
            .append("expireTime", getExpireTime())
            .append("attachment", getAttachment())
            .toString();
    }
}
