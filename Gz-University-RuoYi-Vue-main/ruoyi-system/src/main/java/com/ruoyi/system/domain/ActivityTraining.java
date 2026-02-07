package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 实训与竞赛活动对象 activity_training
 * 
 * @author ruoyi
 * @date 2026-01-15
 */
public class ActivityTraining extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 活动时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "活动时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date activityTime;

    /** 类别 */
    @Excel(name = "类别")
    private String activityType;

    /** 内容 */
    @Excel(name = "内容")
    private String activityContent;

    /** 负责人 */
    @Excel(name = "负责人")
    private String leaderName;

    /** 参与人 */
    @Excel(name = "参与人")
    private String participants;

    /** 资料存储 */
    @Excel(name = "资料存储")
    private String materials;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setActivityTime(Date activityTime) 
    {
        this.activityTime = activityTime;
    }

    public Date getActivityTime() 
    {
        return activityTime;
    }

    public void setActivityType(String activityType) 
    {
        this.activityType = activityType;
    }

    public String getActivityType() 
    {
        return activityType;
    }

    public void setActivityContent(String activityContent) 
    {
        this.activityContent = activityContent;
    }

    public String getActivityContent() 
    {
        return activityContent;
    }

    public void setLeaderName(String leaderName) 
    {
        this.leaderName = leaderName;
    }

    public String getLeaderName() 
    {
        return leaderName;
    }

    public void setParticipants(String participants) 
    {
        this.participants = participants;
    }

    public String getParticipants() 
    {
        return participants;
    }

    public void setMaterials(String materials) 
    {
        this.materials = materials;
    }

    public String getMaterials() 
    {
        return materials;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("activityTime", getActivityTime())
            .append("activityType", getActivityType())
            .append("activityContent", getActivityContent())
            .append("leaderName", getLeaderName())
            .append("participants", getParticipants())
            .append("materials", getMaterials())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
