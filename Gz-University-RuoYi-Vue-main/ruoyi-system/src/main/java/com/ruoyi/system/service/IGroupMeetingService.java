package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.GroupMeeting;

/**
 * 大组会安排Service接口
 * 
 * @author ruoyi
 * @date 2026-01-12
 */
public interface IGroupMeetingService 
{
    /**
     * 查询大组会安排
     * 
     * @param id 大组会安排主键
     * @return 大组会安排
     */
    public GroupMeeting selectGroupMeetingById(Long id);

    /**
     * 查询大组会安排列表
     * 
     * @param groupMeeting 大组会安排
     * @return 大组会安排集合
     */
    public List<GroupMeeting> selectGroupMeetingList(GroupMeeting groupMeeting);

    /**
     * 新增大组会安排
     * 
     * @param groupMeeting 大组会安排
     * @return 结果
     */
    public int insertGroupMeeting(GroupMeeting groupMeeting);

    /**
     * 修改大组会安排
     * 
     * @param groupMeeting 大组会安排
     * @return 结果
     */
    public int updateGroupMeeting(GroupMeeting groupMeeting);

    /**
     * 批量删除大组会安排
     * 
     * @param ids 需要删除的大组会安排主键集合
     * @return 结果
     */
    public int deleteGroupMeetingByIds(Long[] ids);

    /**
     * 删除大组会安排信息
     * 
     * @param id 大组会安排主键
     * @return 结果
     */
    public int deleteGroupMeetingById(Long id);
}
