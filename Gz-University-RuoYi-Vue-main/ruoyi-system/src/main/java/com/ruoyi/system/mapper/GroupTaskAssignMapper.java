package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.GroupTaskAssign;

/**
 * 组务分工管理Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-07
 */
public interface GroupTaskAssignMapper 
{
    /**
     * 查询组务分工管理
     * 
     * @param id 组务分工管理主键
     * @return 组务分工管理
     */
    public GroupTaskAssign selectGroupTaskAssignById(Long id);

    /**
     * 查询组务分工管理列表
     * 
     * @param groupTaskAssign 组务分工管理
     * @return 组务分工管理集合
     */
    public List<GroupTaskAssign> selectGroupTaskAssignList(GroupTaskAssign groupTaskAssign);

    /**
     * 新增组务分工管理
     * 
     * @param groupTaskAssign 组务分工管理
     * @return 结果
     */
    public int insertGroupTaskAssign(GroupTaskAssign groupTaskAssign);

    /**
     * 修改组务分工管理
     * 
     * @param groupTaskAssign 组务分工管理
     * @return 结果
     */
    public int updateGroupTaskAssign(GroupTaskAssign groupTaskAssign);

    /**
     * 删除组务分工管理
     * 
     * @param id 组务分工管理主键
     * @return 结果
     */
    public int deleteGroupTaskAssignById(Long id);

    /**
     * 批量删除组务分工管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGroupTaskAssignByIds(Long[] ids);
}
