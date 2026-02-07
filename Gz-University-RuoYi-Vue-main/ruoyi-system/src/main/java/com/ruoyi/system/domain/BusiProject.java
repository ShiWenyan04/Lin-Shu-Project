package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 项目信息对象 busi_project
 * 
 * @author ruoyi
 * @date 2025-12-17
 */
public class BusiProject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long projectId;

    /** 项目编号 */
    @Excel(name = "项目编号")
    private String projectCode;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 项目地点 */
    @Excel(name = "项目地点")
    private String location;

    /** 项目负责人 */
    @Excel(name = "项目负责人")
    private String leaderName;

    /** 项目来源 */
    @Excel(name = "项目来源")
    private String source;

    /** 执行期 */
    @Excel(name = "执行开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    /** 执行结束日期 */
    @Excel(name = "执行结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    /** 合同信息 */
    @Excel(name = "合同信息")
    private String contractInfo;

    public void setProjectId(Long projectId) 
    {
        this.projectId = projectId;
    }

    public Long getProjectId() 
    {
        return projectId;
    }

    public void setProjectCode(String projectCode) 
    {
        this.projectCode = projectCode;
    }

    public String getProjectCode() 
    {
        return projectCode;
    }

    public void setProjectName(String projectName) 
    {
        this.projectName = projectName;
    }

    public String getProjectName() 
    {
        return projectName;
    }

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }

    public void setLeaderName(String leaderName) 
    {
        this.leaderName = leaderName;
    }

    public String getLeaderName() 
    {
        return leaderName;
    }

    public void setSource(String source) 
    {
        this.source = source;
    }

    public String getSource() 
    {
        return source;
    }

    public void setContractInfo(String contractInfo) 
    {
        this.contractInfo = contractInfo;
    }

    public String getContractInfo() 
    {
        return contractInfo;
    }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("projectId", getProjectId())
            .append("projectCode", getProjectCode())
            .append("projectName", getProjectName())
            .append("location", getLocation())
            .append("leaderName", getLeaderName())
            .append("source", getSource())
            .append("contractInfo", getContractInfo())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .toString();
    }
}
