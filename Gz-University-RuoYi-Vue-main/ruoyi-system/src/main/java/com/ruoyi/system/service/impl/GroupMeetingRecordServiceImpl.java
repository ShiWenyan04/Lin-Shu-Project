package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.GroupMeetingRecordMapper;
import com.ruoyi.system.domain.GroupMeetingRecord;
import com.ruoyi.system.service.IGroupMeetingRecordService;

/**
 * 大组会实施记录页Service业务层处理
 */
@Service
public class GroupMeetingRecordServiceImpl implements IGroupMeetingRecordService
{
    @Autowired
    private GroupMeetingRecordMapper groupMeetingRecordMapper;

    @Override
    public GroupMeetingRecord selectGroupMeetingRecordById(Long id)
    {
        return groupMeetingRecordMapper.selectGroupMeetingRecordById(id);
    }

    @Override
    public List<GroupMeetingRecord> selectGroupMeetingRecordList(GroupMeetingRecord groupMeetingRecord)
    {
        return groupMeetingRecordMapper.selectGroupMeetingRecordList(groupMeetingRecord);
    }

    /**
     * 新增
     */
    @Override
    public int insertGroupMeetingRecord(GroupMeetingRecord groupMeetingRecord)
    {
        if (StringUtils.isEmpty(groupMeetingRecord.getAbsentees())) {
            groupMeetingRecord.setAbsentees("无");
        }
        groupMeetingRecord.setAuditStatus("0");
        groupMeetingRecord.setCreateTime(DateUtils.getNowDate());
        return groupMeetingRecordMapper.insertGroupMeetingRecord(groupMeetingRecord);
    }

    /**
     * 修改
     */
    @Override
    public int updateGroupMeetingRecord(GroupMeetingRecord groupMeetingRecord)
    {
        if (StringUtils.isEmpty(groupMeetingRecord.getAbsentees())) {
            groupMeetingRecord.setAbsentees("无");
        }

        // 1. 查出旧数据
        GroupMeetingRecord oldData = groupMeetingRecordMapper.selectGroupMeetingRecordById(groupMeetingRecord.getId());

        // 2. 核心修复：如果旧状态是"2"(已驳回)，强制重置为"0"(待审核)
        // 去掉了 && groupMeetingRecord.getAuditStatus() == null 的判断
        // 因为前端修改时往往会带上旧的状态码"2"，所以必须强制覆盖
        if (oldData != null && "2".equals(oldData.getAuditStatus())) {
            groupMeetingRecord.setAuditStatus("0");
            groupMeetingRecord.setAuditReason(""); // 清空驳回原因
        }

        groupMeetingRecord.setUpdateTime(DateUtils.getNowDate());
        return groupMeetingRecordMapper.updateGroupMeetingRecord(groupMeetingRecord);
    }

    /**
     * 审核 (老师操作)
     * 注意：这个方法直接调用 Mapper，不走上面的 updateGroupMeetingRecord 逻辑
     * 所以老师审核驳回时，不会被上面的逻辑误重置为0
     */
    @Override
    public int auditGroupMeetingRecord(GroupMeetingRecord groupMeetingRecord)
    {
        groupMeetingRecord.setUpdateTime(DateUtils.getNowDate());
        return groupMeetingRecordMapper.updateGroupMeetingRecord(groupMeetingRecord);
    }

    @Override
    public int deleteGroupMeetingRecordByIds(Long[] ids)
    {
        return groupMeetingRecordMapper.deleteGroupMeetingRecordByIds(ids);
    }

    @Override
    public int deleteGroupMeetingRecordById(Long id)
    {
        return groupMeetingRecordMapper.deleteGroupMeetingRecordById(id);
    }
}