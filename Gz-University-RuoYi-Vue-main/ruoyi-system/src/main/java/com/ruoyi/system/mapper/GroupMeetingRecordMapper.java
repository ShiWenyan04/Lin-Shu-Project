package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.GroupMeetingRecord;

/**
 * 大组会实施记录页Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-10
 */
public interface GroupMeetingRecordMapper 
{
    /**
     * 查询大组会实施记录页
     * 
     * @param id 大组会实施记录页主键
     * @return 大组会实施记录页
     */
    public GroupMeetingRecord selectGroupMeetingRecordById(Long id);

    /**
     * 查询大组会实施记录页列表
     * 
     * @param groupMeetingRecord 大组会实施记录页
     * @return 大组会实施记录页集合
     */
    public List<GroupMeetingRecord> selectGroupMeetingRecordList(GroupMeetingRecord groupMeetingRecord);

    /**
     * 新增大组会实施记录页
     * 
     * @param groupMeetingRecord 大组会实施记录页
     * @return 结果
     */
    public int insertGroupMeetingRecord(GroupMeetingRecord groupMeetingRecord);

    /**
     * 修改大组会实施记录页
     * 
     * @param groupMeetingRecord 大组会实施记录页
     * @return 结果
     */
    public int updateGroupMeetingRecord(GroupMeetingRecord groupMeetingRecord);

    /**
     * 删除大组会实施记录页
     * 
     * @param id 大组会实施记录页主键
     * @return 结果
     */
    public int deleteGroupMeetingRecordById(Long id);

    /**
     * 批量删除大组会实施记录页
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGroupMeetingRecordByIds(Long[] ids);
}
