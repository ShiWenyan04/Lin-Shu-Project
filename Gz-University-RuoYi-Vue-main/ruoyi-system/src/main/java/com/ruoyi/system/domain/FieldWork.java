package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 外业信息对象 field_work
 *
 * @author ruoyi
 * @date 2025-07-21
 */
public class FieldWork extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 序号 */
    @Excel(name = "序号")
    private Long sequenceNo;

    /** 统计月份 */
    @Excel(name = "统计月份")
    private String statMonth;

    /** 出发日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出发日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 返校日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "返校日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /** 外业地点 */
    @Excel(name = "外业地点")
    private String location;

    /** 外业内容 */
    @Excel(name = "外业内容")
    private String content;

    /** 外业负责人 */
    @Excel(name = "外业负责人")
    private String leader;

    /** 参与人员 */
    @Excel(name = "参与人员")
    private String participants;

    /** 外业天数 */
    @Excel(name = "外业天数")
    private Long fieldDays;

    /** 共计天数 */
    @Excel(name = "共计天数")
    private Long totalDays;

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

    public void setStatMonth(String statMonth)
    {
        this.statMonth = statMonth;
    }

    public String getStatMonth()
    {
        return statMonth;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getLocation()
    {
        return location;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }

    public void setLeader(String leader)
    {
        this.leader = leader;
    }

    public String getLeader()
    {
        return leader;
    }

    public void setParticipants(String participants)
    {
        this.participants = participants;
    }

    public String getParticipants()
    {
        return participants;
    }

    public void setFieldDays(Long fieldDays)
    {
        this.fieldDays = fieldDays;
    }

    public Long getFieldDays()
    {
        return fieldDays;
    }

    public void setTotalDays(Long totalDays)
    {
        this.totalDays = totalDays;
    }

    public Long getTotalDays()
    {
        return totalDays;
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
            .append("statMonth", getStatMonth())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("location", getLocation())
            .append("content", getContent())
            .append("leader", getLeader())
            .append("participants", getParticipants())
            .append("fieldDays", getFieldDays())
            .append("totalDays", getTotalDays())
            .append("remarks", getRemarks())
            .toString();
    }
}
