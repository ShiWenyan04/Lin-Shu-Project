package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.GroupMeetingMapper;
import com.ruoyi.system.domain.GroupMeeting;
import com.ruoyi.system.service.IGroupMeetingService;

/**
 * 大组会安排Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-12
 */
@Service
public class GroupMeetingServiceImpl implements IGroupMeetingService 
{
    @Autowired
    private GroupMeetingMapper groupMeetingMapper;

    /**
     * 查询大组会安排
     * 
     * @param id 大组会安排主键
     * @return 大组会安排
     */
    @Override
    public GroupMeeting selectGroupMeetingById(Long id)
    {
        return groupMeetingMapper.selectGroupMeetingById(id);
    }

    /**
     * 查询大组会安排列表
     * 
     * @param groupMeeting 大组会安排
     * @return 大组会安排
     */
    @Override
    public List<GroupMeeting> selectGroupMeetingList(GroupMeeting groupMeeting)
    {
        return groupMeetingMapper.selectGroupMeetingList(groupMeeting);
    }

    /**
     * 新增大组会安排
     * 
     * @param groupMeeting 大组会安排
     * @return 结果
     */
    @Override
    public int insertGroupMeeting(GroupMeeting groupMeeting)
    {
        return groupMeetingMapper.insertGroupMeeting(groupMeeting);
    }

    /**
     * 修改大组会安排
     * 
     * @param groupMeeting 大组会安排
     * @return 结果
     */
    @Override
    public int updateGroupMeeting(GroupMeeting groupMeeting)
    {
        return groupMeetingMapper.updateGroupMeeting(groupMeeting);
    }

    /**
     * 批量删除大组会安排
     * 
     * @param ids 需要删除的大组会安排主键
     * @return 结果
     */
    @Override
    public int deleteGroupMeetingByIds(Long[] ids)
    {
        return groupMeetingMapper.deleteGroupMeetingByIds(ids);
    }

    /**
     * 删除大组会安排信息
     * 
     * @param id 大组会安排主键
     * @return 结果
     */
    @Override
    public int deleteGroupMeetingById(Long id)
    {
        return groupMeetingMapper.deleteGroupMeetingById(id);
    }
}
