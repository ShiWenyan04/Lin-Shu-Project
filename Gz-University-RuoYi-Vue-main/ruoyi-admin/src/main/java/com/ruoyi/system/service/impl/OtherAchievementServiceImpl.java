package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.mapper.system.OtherAchievementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.system.domain.OtherAchievement;
import com.ruoyi.system.service.IOtherAchievementService;

/**
 * 其他成果管理Service业务层处理
 * * @author ruoyi
 * @date 2025-12-18
 */
@Service
public class OtherAchievementServiceImpl implements IOtherAchievementService
{
    @Autowired
    private OtherAchievementMapper otherAchievementMapper;

    /**
     * 查询其他成果管理
     */
    @Override
    public OtherAchievement selectOtherAchievementById(Long id)
    {
        return otherAchievementMapper.selectOtherAchievementById(id);
    }

    /**
     * 查询其他成果管理列表
     */
    @Override
    public List<OtherAchievement> selectOtherAchievementList(OtherAchievement otherAchievement)
    {
        return otherAchievementMapper.selectOtherAchievementList(otherAchievement);
    }

    /**
     * 新增其他成果管理
     */
    @Override
    public int insertOtherAchievement(OtherAchievement otherAchievement)
    {
        otherAchievement.setCreateTime(DateUtils.getNowDate());

        // 默认状态设为待审核 '0'
        otherAchievement.setAuditStatus("0");

        // 如果是学生，强制绑定创建人
        if (SecurityUtils.hasRole("student")) {
            otherAchievement.setCreateBy(SecurityUtils.getUsername());
        }

        return otherAchievementMapper.insertOtherAchievement(otherAchievement);
    }

    /**
     * 修改其他成果管理
     */
    @Override
    public int updateOtherAchievement(OtherAchievement otherAchievement)
    {
        otherAchievement.setUpdateTime(DateUtils.getNowDate());
        otherAchievement.setUpdateBy(SecurityUtils.getUsername());

        // 核心逻辑：普通修改重置为待审核状态
        // 修改点：加入了 !SecurityUtils.hasRole("otherAchievement_manager")
        // 只有 老师、本模块负责人、管理员 修改时，才保持原状态
        if (!SecurityUtils.hasRole("teacher")
                && !SecurityUtils.hasRole("otherAchievement_manager")
                && !SecurityUtils.hasRole("admin")) {
            otherAchievement.setAuditStatus("0");
        }

        return otherAchievementMapper.updateOtherAchievement(otherAchievement);
    }

    /**
     * 审核其他成果 (专用方法)
     */
    @Override
    public int auditOtherAchievement(OtherAchievement otherAchievement)
    {
        // 记录审核信息
        otherAchievement.setAuditTime(DateUtils.getNowDate());
        otherAchievement.setAuditorId(SecurityUtils.getUserId());

        // 只更新必要字段（Mapper XML中会处理）
        return otherAchievementMapper.updateOtherAchievement(otherAchievement);
    }

    /**
     * 批量删除其他成果管理
     */
    @Override
    public int deleteOtherAchievementByIds(Long[] ids)
    {
        return otherAchievementMapper.deleteOtherAchievementByIds(ids);
    }

    /**
     * 删除其他成果管理信息
     */
    @Override
    public int deleteOtherAchievementById(Long id)
    {
        return otherAchievementMapper.deleteOtherAchievementById(id);
    }
}