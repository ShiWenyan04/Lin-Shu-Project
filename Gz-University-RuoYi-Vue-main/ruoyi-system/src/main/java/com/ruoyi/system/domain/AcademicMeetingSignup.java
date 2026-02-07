package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.Date;

/**
 * 学术会议报名信息实体
 * @author ruoyi
 * @date 2025-12-24
 */
public class AcademicMeetingSignup extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 关联会议ID */
    private Long meetingId;

    /** 用户ID */
    private Long userId;

    /** 报名人姓名 (原有字段) */
    private String name;

    /** 联系方式 (原有字段) */
    private String phone;

    // ============ 新增以下字段用于关联查询 ============
    /** 用户昵称 (来自 sys_user 表) */
    private String nickName;

    /** 用户手机 (来自 sys_user 表) */
    private String phonenumber;
    // ===============================================

    /** 报名时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signupTime;

    // Getter 和 Setter 方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMeetingId() { return meetingId; }
    public void setMeetingId(Long meetingId) { this.meetingId = meetingId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    // ============ 新增 Getter/Setter ============
    public String getNickName() { return nickName; }
    public void setNickName(String nickName) { this.nickName = nickName; }

    public String getPhonenumber() { return phonenumber; }
    public void setPhonenumber(String phonenumber) { this.phonenumber = phonenumber; }
    // ============================================

    public Date getSignupTime() { return signupTime; }
    public void setSignupTime(Date signupTime) { this.signupTime = signupTime; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("meetingId", getMeetingId())
                .append("userId", getUserId())
                .append("name", getName())
                .append("nickName", getNickName()) // toString 也加上
                .append("phone", getPhone())
                .append("phonenumber", getPhonenumber()) // toString 也加上
                .append("signupTime", getSignupTime())
                .toString();
    }
}