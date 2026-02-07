package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.system.PaperAchievementMapper;
import com.ruoyi.system.domain.PaperAchievement;
import com.ruoyi.system.service.IPaperAchievementService;

/**
 * 论文成果管理Service业务层处理
 * * @author ruoyi
 * @date 2025-12-20
 */
@Service
public class PaperAchievementServiceImpl implements IPaperAchievementService
{
    @Autowired
    private PaperAchievementMapper paperAchievementMapper;

    /**
     * 查询论文成果管理
     */
    @Override
    public PaperAchievement selectPaperAchievementById(Long id)
    {
        return paperAchievementMapper.selectPaperAchievementById(id);
    }

    /**
     * 查询论文成果管理列表
     */
    @Override
    public List<PaperAchievement> selectPaperAchievementList(PaperAchievement paperAchievement)
    {
        return paperAchievementMapper.selectPaperAchievementList(paperAchievement);
    }

    /**
     * 新增论文成果管理
     */
    @Override
    public int insertPaperAchievement(PaperAchievement paperAchievement)
    {
        paperAchievement.setCreateTime(DateUtils.getNowDate());

        // 默认状态设为待审核 '0'
        paperAchievement.setAuditStatus("0");

        // 如果是学生，强制绑定创建人
        if (SecurityUtils.hasRole("student")) {
            paperAchievement.setCreateBy(SecurityUtils.getUsername());
        }

        return paperAchievementMapper.insertPaperAchievement(paperAchievement);
    }

    /**
     * 修改论文成果管理 (学生/普通修改)
     */
    @Override
    public int updatePaperAchievement(PaperAchievement paperAchievement)
    {
        paperAchievement.setUpdateTime(DateUtils.getNowDate());
        paperAchievement.setUpdateBy(SecurityUtils.getUsername());

        // 核心逻辑：任何信息的修改，都应该重置审核状态为“待审核”
        // 修改点：加入了 !SecurityUtils.hasRole("papeAachievement_manager")
        // 只有 老师、论文成果负责人、管理员 修改时，才不会重置审核状态
        if (!SecurityUtils.hasRole("teacher")
                && !SecurityUtils.hasRole("papeAachievement_manager")
                && !SecurityUtils.hasRole("admin")) {
            paperAchievement.setAuditStatus("0");
        }

        return paperAchievementMapper.updatePaperAchievement(paperAchievement);
    }

    /**
     * 审核论文成果管理 (老师/管理员专用)
     */
    @Override
    public int auditPaperAchievement(PaperAchievement paperAchievement)
    {
        // 只更新状态、备注、更新时间和更新人
        paperAchievement.setUpdateTime(DateUtils.getNowDate());
        paperAchievement.setUpdateBy(SecurityUtils.getUsername());
        return paperAchievementMapper.updatePaperAchievement(paperAchievement);
    }

    /**
     * 批量删除论文成果管理
     */
    @Override
    public int deletePaperAchievementByIds(Long[] ids)
    {
        return paperAchievementMapper.deletePaperAchievementByIds(ids);
    }

    /**
     * 删除论文成果管理信息
     */
    @Override
    public int deletePaperAchievementById(Long id)
    {
        return paperAchievementMapper.deletePaperAchievementById(id);
    }
}