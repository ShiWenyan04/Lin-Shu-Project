package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.AcademicMeeting;
import java.util.List;

/**
 * 学术会议信息Mapper接口
 * * @author ruoyi
 * @date 2025-12-23
 */
public interface AcademicMeetingMapper
{
    /**
     * 查询学术会议信息
     * * @param id 学术会议信息主键
     * @return 学术会议信息
     */
    public AcademicMeeting selectAcademicMeetingById(Long id);

    /**
     * 查询学术会议信息列表
     * * @param academicMeeting 学术会议信息
     * @return 学术会议信息集合
     */
    public List<AcademicMeeting> selectAcademicMeetingList(AcademicMeeting academicMeeting);

    /**
     * 新增学术会议信息
     * * @param academicMeeting 学术会议信息
     * @return 结果
     */
    public int insertAcademicMeeting(AcademicMeeting academicMeeting);

    /**
     * 修改学术会议信息
     * * @param academicMeeting 学术会议信息
     * @return 结果
     */
    public int updateAcademicMeeting(AcademicMeeting academicMeeting);

    /**
     * 删除学术会议信息
     * * @param id 学术会议信息主键
     * @return 结果
     */
    public int deleteAcademicMeetingById(Long id);

    /**
     * 批量删除学术会议信息
     * * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAcademicMeetingByIds(Long[] ids);
}