package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 团队成员对象 team_member
 * 
 * @author ruoyi
 * @date 2025-07-18
 */
public class TeamMember extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 性别 */
    @Excel(name = "性别")
    private String gender;

    /** 研究方向 */
    @Excel(name = "研究方向")
    private String researchDirection;

    /** 学号 */
    @Excel(name = "学号")
    private String studentId;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 银行卡号 */
    @Excel(name = "银行卡号")
    private String bankCard;

    /** 入学时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入学时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date enrollmentDate;

    /** 当前学历（字典：本科，硕士生，博士生） */
    @Excel(name = "当前学历", readConverterExp = "字=典：本科，硕士生，博士生")
    private String currentEducation;

    /** 当前状态(在读，毕业,休学) */
    @Excel(name = "当前状态(在读，毕业,休学)")
    private String currentStatus;

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

    public void setGender(String gender) 
    {
        this.gender = gender;
    }

    public String getGender() 
    {
        return gender;
    }

    public void setResearchDirection(String researchDirection) 
    {
        this.researchDirection = researchDirection;
    }

    public String getResearchDirection() 
    {
        return researchDirection;
    }

    public void setStudentId(String studentId) 
    {
        this.studentId = studentId;
    }

    public String getStudentId() 
    {
        return studentId;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }

    public void setBankCard(String bankCard) 
    {
        this.bankCard = bankCard;
    }

    public String getBankCard() 
    {
        return bankCard;
    }

    public void setEnrollmentDate(Date enrollmentDate) 
    {
        this.enrollmentDate = enrollmentDate;
    }

    public Date getEnrollmentDate() 
    {
        return enrollmentDate;
    }

    public void setCurrentEducation(String currentEducation) 
    {
        this.currentEducation = currentEducation;
    }

    public String getCurrentEducation() 
    {
        return currentEducation;
    }

    public void setCurrentStatus(String currentStatus) 
    {
        this.currentStatus = currentStatus;
    }

    public String getCurrentStatus() 
    {
        return currentStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("gender", getGender())
            .append("researchDirection", getResearchDirection())
            .append("studentId", getStudentId())
            .append("phone", getPhone())
            .append("email", getEmail())
            .append("bankCard", getBankCard())
            .append("enrollmentDate", getEnrollmentDate())
            .append("currentEducation", getCurrentEducation())
            .append("currentStatus", getCurrentStatus())
            .toString();
    }
}
