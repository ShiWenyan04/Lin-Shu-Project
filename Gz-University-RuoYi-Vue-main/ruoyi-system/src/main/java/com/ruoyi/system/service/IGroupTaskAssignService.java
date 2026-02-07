package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.GroupTaskAssign;

/**
 * 组务分工管理Service接口
 *
 * @author ruoyi
 * @date 2026-01-07
 */
public interface IGroupTaskAssignService
{
    /**
     * 查询组务分工管理
     */
    public GroupTaskAssign selectGroupTaskAssignById(Long id);

    /**
     * 查询组务分工管理列表
     */
    public List<GroupTaskAssign> selectGroupTaskAssignList(GroupTaskAssign groupTaskAssign);

    /**
     * 新增组务分工管理
     */
    public int insertGroupTaskAssign(GroupTaskAssign groupTaskAssign);

    /**
     * 学生申请分工
     */
    public int applyGroupTaskAssign(GroupTaskAssign groupTaskAssign);

    /**
     * 审核分工
     */
    public int auditGroupTaskAssign(Map<String, Object> params);

    /**
     * 修改组务分工管理
     */
    public int updateGroupTaskAssign(GroupTaskAssign groupTaskAssign);

    /**
     * 批量删除组务分工管理
     */
    public int deleteGroupTaskAssignByIds(Long[] ids);

    /**
     * 删除组务分工管理信息
     */
    public int deleteGroupTaskAssignById(Long id);

    public int cancelUserRole(Long roleId);
}