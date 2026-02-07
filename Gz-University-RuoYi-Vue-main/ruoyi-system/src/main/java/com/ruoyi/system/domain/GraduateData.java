package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 毕业生资料管理对象 graduate_data
 * 
 * @author ruoyi
 * @date 2025-07-19
 */
public class GraduateData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 序号 */
    @Excel(name = "序号")
    private Long sequenceNo;

    /** 文件名 */
    @Excel(name = "文件名")
    private String fileName;

    /** 所属成员 */
    @Excel(name = "所属成员")
    private String student;

    /** 文件类型 */
    @Excel(name = "文件类型")
    private String fileType;

    /** 文件大小 */
    private Double fileSize;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createdBy;

    /** 备注 */
    @Excel(name = "备注")
    private String remarks;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedTime;

    /** 文件存储路径 */
    @Excel(name = "文件存储路径")
    private String filePath;

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

    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }

    public void setStudent(String student) 
    {
        this.student = student;
    }

    public String getStudent() 
    {
        return student;
    }

    public void setFileType(String fileType) 
    {
        this.fileType = fileType;
    }

    public String getFileType() 
    {
        return fileType;
    }

    public void setFileSize(Double fileSize)
    {
        this.fileSize = fileSize;
    }

    public Double getFileSize()
    {
        return fileSize;
    }

    public void setCreatedBy(String createdBy) 
    {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() 
    {
        return createdBy;
    }

    public void setRemarks(String remarks) 
    {
        this.remarks = remarks;
    }

    public String getRemarks() 
    {
        return remarks;
    }

    public void setCreatedTime(Date createdTime) 
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() 
    {
        return createdTime;
    }

    public void setUpdatedTime(Date updatedTime) 
    {
        this.updatedTime = updatedTime;
    }

    public Date getUpdatedTime() 
    {
        return updatedTime;
    }

    public void setFilePath(String filePath) 
    {
        this.filePath = filePath;
    }

    public String getFilePath() 
    {
        return filePath;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("sequenceNo", getSequenceNo())
            .append("fileName", getFileName())
            .append("student", getStudent())
            .append("fileType", getFileType())
            .append("fileSize", getFileSize())
            .append("createdBy", getCreatedBy())
            .append("remarks", getRemarks())
            .append("createdTime", getCreatedTime())
            .append("updatedTime", getUpdatedTime())
            .append("filePath", getFilePath())
            .toString();
    }
}
