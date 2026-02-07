package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.mapper.system.RyThesisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.RyThesis;
import com.ruoyi.system.service.IRyThesisService;

/**
 * 学位论文管理Service业务层处理
 * * @author ruoyi
 * @date 2025-12-13
 */
@Service
public class RyThesisServiceImpl implements IRyThesisService
{
    @Autowired
    private RyThesisMapper ryThesisMapper;

    /**
     * 查询学位论文管理
     */
    @Override
    public RyThesis selectRyThesisById(Long id)
    {
        return ryThesisMapper.selectRyThesisById(id);
    }

    /**
     * 查询学位论文管理列表
     */
    @Override
    public List<RyThesis> selectRyThesisList(RyThesis ryThesis)
    {
        return ryThesisMapper.selectRyThesisList(ryThesis);
    }

    /**
     * 新增学位论文管理
     */
    @Override
    public int insertRyThesis(RyThesis ryThesis)
    {
        ryThesis.setCreateTime(DateUtils.getNowDate());

        // 默认状态：待审核
        ryThesis.setAuditStatus("0");

        // 如果是学生，强制绑定创建人
        if (SecurityUtils.hasRole("student")) {
            ryThesis.setCreateBy(SecurityUtils.getUsername());
        }

        return ryThesisMapper.insertRyThesis(ryThesis);
    }

    /**
     * 修改学位论文管理
     */
    @Override
    public int updateRyThesis(RyThesis ryThesis)
    {
        ryThesis.setUpdateTime(DateUtils.getNowDate());
        ryThesis.setUpdateBy(SecurityUtils.getUsername());

        // 核心逻辑：非管理员/老师/负责人修改，重置为待审核
        // 修改点：加入了 !SecurityUtils.hasRole("thesis_manager")
        if (!SecurityUtils.hasRole("teacher")
                && !SecurityUtils.hasRole("thesis_manager")
                && !SecurityUtils.hasRole("admin")) {
            ryThesis.setAuditStatus("0");
        }

        return ryThesisMapper.updateRyThesis(ryThesis);
    }

    /**
     * 审核学位论文管理 (新增)
     */
    @Override
    public int auditRyThesis(RyThesis ryThesis)
    {
        ryThesis.setAuditTime(DateUtils.getNowDate());
        ryThesis.setAuditorId(SecurityUtils.getUserId());
        return ryThesisMapper.updateRyThesis(ryThesis);
    }

    /**
     * 批量删除学位论文管理
     */
    @Override
    public int deleteRyThesisByIds(Long[] ids)
    {
        return ryThesisMapper.deleteRyThesisByIds(ids);
    }

    /**
     * 删除学位论文管理信息
     */
    @Override
    public int deleteRyThesisById(Long id)
    {
        return ryThesisMapper.deleteRyThesisById(id);
    }
}