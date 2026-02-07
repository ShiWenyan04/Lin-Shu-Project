package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.mapper.GroupTaskAssignMapper;
import com.ruoyi.system.mapper.SysUserRoleMapper;
import com.ruoyi.system.domain.GroupTaskAssign;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.service.IGroupTaskAssignService;

/**
 * 组务分工管理Service业务层处理
 * * @author ruoyi
 * @date 2026-01-07
 */
@Service
public class GroupTaskAssignServiceImpl implements IGroupTaskAssignService
{
    @Autowired
    private GroupTaskAssignMapper groupTaskAssignMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public GroupTaskAssign selectGroupTaskAssignById(Long id)
    {
        return groupTaskAssignMapper.selectGroupTaskAssignById(id);
    }

    @Override
    public List<GroupTaskAssign> selectGroupTaskAssignList(GroupTaskAssign groupTaskAssign)
    {
        return groupTaskAssignMapper.selectGroupTaskAssignList(groupTaskAssign);
    }

    @Override
    public int insertGroupTaskAssign(GroupTaskAssign groupTaskAssign)
    {
        groupTaskAssign.setCreateTime(DateUtils.getNowDate());
        return groupTaskAssignMapper.insertGroupTaskAssign(groupTaskAssign);
    }

    @Override
    public int applyGroupTaskAssign(GroupTaskAssign groupTaskAssign) {
        groupTaskAssign.setUserId(SecurityUtils.getUserId());
        groupTaskAssign.setUserName(SecurityUtils.getLoginUser().getUser().getNickName());
        groupTaskAssign.setStatus("0"); // 待审核
        groupTaskAssign.setCreateBy(SecurityUtils.getUsername());
        groupTaskAssign.setCreateTime(DateUtils.getNowDate());
        return groupTaskAssignMapper.insertGroupTaskAssign(groupTaskAssign);
    }

    /**
     * 导师审核
     * 修改点：【追加模式】保留旧角色，追加新角色
     */
    @Override
    @Transactional // 开启事务
    public int auditGroupTaskAssign(Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        String status = params.get("status").toString(); // 1=通过, 2=驳回
        String reason = (String) params.get("auditReason");

        // 1. 获取原申请记录
        GroupTaskAssign original = groupTaskAssignMapper.selectGroupTaskAssignById(id);
        if (original == null) {
            throw new RuntimeException("记录不存在");
        }

        // 2. 更新申请状态
        GroupTaskAssign update = new GroupTaskAssign();
        update.setId(id);
        update.setStatus(status);
        update.setAuditReason(reason);
        update.setUpdateBy(SecurityUtils.getUsername());
        update.setUpdateTime(DateUtils.getNowDate());

        int rows = groupTaskAssignMapper.updateGroupTaskAssign(update);

        // 3. 如果状态为 '1' (已通过)，则赋予系统角色 (追加模式)
        if ("1".equals(status)) {
            Long userId = original.getUserId();
            Long roleId = original.getRoleId();

            if (userId != null && roleId != null) {
                // 构造关联对象
                List<SysUserRole> list = new ArrayList<>();
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);

                // =======================================================
                // 【核心修改】追加模式
                // 1. 我们 删除了 deleteUserRoleByUserId(userId)，这样“学生”角色会被保留。
                // 2. 使用 try-catch 包裹，防止重复添加同一个角色导致报错
                // =======================================================
                try {
                    // 若依自带的批量插入，如果数据库已经存在该(userId, roleId)组合，会报主键冲突
                    // 我们捕获异常忽略它即可，保证流程不中断
                    sysUserRoleMapper.batchUserRole(list);
                } catch (Exception e) {
                    // 打印日志：用户已有该角色，无需重复添加
                    System.out.println("警告：用户 " + userId + " 已拥有角色 " + roleId + "，跳过插入。");
                }
            }
        }

        return rows;
    }

    /**
     * 修改组务分工管理
     * 修改核心：修改后重置状态为待审核
     */
    @Override
    public int updateGroupTaskAssign(GroupTaskAssign groupTaskAssign)
    {
        // 1. 设置更新人和更新时间
        groupTaskAssign.setUpdateBy(SecurityUtils.getUsername());
        groupTaskAssign.setUpdateTime(DateUtils.getNowDate());

        // 2. 状态重置为 "0" (待审核)
        groupTaskAssign.setStatus("0");

        // 3. 清空驳回原因
        groupTaskAssign.setAuditReason("");

        return groupTaskAssignMapper.updateGroupTaskAssign(groupTaskAssign);
    }

    @Override
    public int deleteGroupTaskAssignByIds(Long[] ids)
    {
        return groupTaskAssignMapper.deleteGroupTaskAssignByIds(ids);
    }

    /**
     * 用户卸任角色
     */
    @Override
    public int cancelUserRole(Long roleId)
    {
        Long userId = SecurityUtils.getUserId();

        // 1. 安全检查：禁止卸任基础角色 (假设角色ID 100 是学生，或者根据角色Key判断)
        // 更好的做法是查一下这个roleId对应的Key
        // 这里简单处理：如果当前用户只有一个角色，不允许卸任（防止变成无角色用户）
        int count = sysUserRoleMapper.countUserRoleByUserId(userId);
        if (count <= 1) {
            throw new RuntimeException("您至少需要保留一个身份，无法继续卸任！");
        }

        // 2. 执行删除
        // SysUserRoleMapper 中通常有 deleteUserRoleInfo(SysUserRole userRole)
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);

        return sysUserRoleMapper.deleteUserRoleInfo(userRole);
    }

    @Override
    public int deleteGroupTaskAssignById(Long id)
    {
        return groupTaskAssignMapper.deleteGroupTaskAssignById(id);
    }
}