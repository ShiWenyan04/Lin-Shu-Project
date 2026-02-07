package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.AcademicMeetingSignup;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 学术会议报名Mapper接口
 * @author ruoyi
 * @date 2025-12-24
 */
public interface AcademicMeetingSignupMapper {
    /**
     * 新增报名信息
     */
    int insertSignup(AcademicMeetingSignup signup);

    /**
     * 根据会议ID查询报名列表
     */
    List<AcademicMeetingSignup> selectSignupListByMeetingId(Long meetingId);

    /**
     * 检查用户是否已报名（根据会议ID和用户ID）
     */
    int checkUserSignedUp(@Param("meetingId") Long meetingId, @Param("userId") Long userId);

    /**
     * 删除报名信息（取消报名）
     */
    int deleteSignupByMeetingAndUser(@Param("meetingId") Long meetingId, @Param("userId") Long userId);

    /**
     * 获取某会议的所有报名人姓名（用于更新主表）
     */
    List<String> selectNamesByMeetingId(Long meetingId);
}