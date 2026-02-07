package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 组务分工管理对象 group_task_assign
 * * @author ruoyi
 * @date 2026-01-07
 */
public class GroupTaskAssign extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 分工任务名称 */
    @Excel(name = "分工任务名称")
    private String taskName;

    /** 关联系统角色ID */
    @Excel(name = "关联系统角色ID")
    private Long roleId;

    /** 任务要求 */
    @Excel(name = "任务要求")
    private String requirements;

    /** 负责人ID */
    private Long userId;

    /** 负责人姓名 */
    @Excel(name = "负责人姓名")
    private String userName;

    /** 负责开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "负责开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 负责结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "负责结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /** 状态 (0-待审核 1-已通过 2-已驳回) */
    @Excel(name = "状态")
    private String status;

    /** 审核/驳回原因 (新增字段) */
    @Excel(name = "审核原因")
    private String auditReason;

    /** 申请人当前角色 (非数据库字段，仅用于查询显示) */
    private String currentUserRole;

    public void setCurrentUserRole(String currentUserRole) {
        this.currentUserRole = currentUserRole;
    }

    public String getCurrentUserRole() {
        return currentUserRole;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public String getTaskName()
    {
        return taskName;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRequirements(String requirements)
    {
        this.requirements = requirements;
    }

    public String getRequirements()
    {
        return requirements;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
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

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setAuditReason(String auditReason)
    {
        this.auditReason = auditReason;
    }

    public String getAuditReason()
    {
        return auditReason;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("taskName", getTaskName())
                .append("roleId", getRoleId())
                .append("requirements", getRequirements())
                .append("userId", getUserId())
                .append("userName", getUserName())
                .append("startDate", getStartDate())
                .append("endDate", getEndDate())
                .append("status", getStatus())
                .append("auditReason", getAuditReason())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("currentUserRole", getCurrentUserRole())
                .toString();
    }
}