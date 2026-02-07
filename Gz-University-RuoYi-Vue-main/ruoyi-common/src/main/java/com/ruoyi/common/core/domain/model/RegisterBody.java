package com.ruoyi.common.core.domain.model;

/**
 * 用户注册对象
 * * @author ruoyi
 */
public class RegisterBody extends LoginBody
{
    /** 真实姓名 */
    private String nickName;

    /** 用户性别 */
    private String sex;

    /** 民族 */
    private String ethnicity;

    /** 手机号码 */
    private String phonenumber;

    /** 邮箱 */
    private String email;

    /** 家庭住址 */
    private String address;

    /** 学号 */
    private String studentId;

    /** 入学年份 */
    private String enrollmentYear;

    /** 学位类别 */
    private String degreeType;

    /** 专业 */
    private String major;

    /** 当前状态 */
    private String currentStatus;

    /** 本科毕业院校 */
    private String undergradUniv;

    /** 硕士毕业院校 */
    private String masterUniv;

    /** 就业单位 */
    private String employer;

    /** 紧急联系人 */
    private String emergencyContact;

    /** 与本人关系 */
    private String emergencyRelation;

    /** 紧急联系电话 */
    private String emergencyPhone;

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getEthnicity()
    {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity)
    {
        this.ethnicity = ethnicity;
    }

    public String getPhonenumber()
    {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber)
    {
        this.phonenumber = phonenumber;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getStudentId()
    {
        return studentId;
    }

    public void setStudentId(String studentId)
    {
        this.studentId = studentId;
    }

    public String getEnrollmentYear()
    {
        return enrollmentYear;
    }

    public void setEnrollmentYear(String enrollmentYear)
    {
        this.enrollmentYear = enrollmentYear;
    }

    public String getDegreeType()
    {
        return degreeType;
    }

    public void setDegreeType(String degreeType)
    {
        this.degreeType = degreeType;
    }

    public String getMajor()
    {
        return major;
    }

    public void setMajor(String major)
    {
        this.major = major;
    }

    public String getCurrentStatus()
    {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus)
    {
        this.currentStatus = currentStatus;
    }

    public String getUndergradUniv()
    {
        return undergradUniv;
    }

    public void setUndergradUniv(String undergradUniv)
    {
        this.undergradUniv = undergradUniv;
    }

    public String getMasterUniv()
    {
        return masterUniv;
    }

    public void setMasterUniv(String masterUniv)
    {
        this.masterUniv = masterUniv;
    }

    public String getEmployer()
    {
        return employer;
    }

    public void setEmployer(String employer)
    {
        this.employer = employer;
    }

    public String getEmergencyContact()
    {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact)
    {
        this.emergencyContact = emergencyContact;
    }

    public String getEmergencyRelation()
    {
        return emergencyRelation;
    }

    public void setEmergencyRelation(String emergencyRelation)
    {
        this.emergencyRelation = emergencyRelation;
    }

    public String getEmergencyPhone()
    {
        return emergencyPhone;
    }

    public void setEmergencyPhone(String emergencyPhone)
    {
        this.emergencyPhone = emergencyPhone;
    }
}