package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.RyThesis;

/**
 * 学位论文管理Service接口
 *
 * @author ruoyi
 * @date 2025-12-13
 */
public interface IRyThesisService
{
    /**
     * 查询学位论文管理
     */
    public RyThesis selectRyThesisById(Long id);

    /**
     * 查询学位论文管理列表
     */
    public List<RyThesis> selectRyThesisList(RyThesis ryThesis);

    /**
     * 新增学位论文管理
     */
    public int insertRyThesis(RyThesis ryThesis);

    /**
     * 修改学位论文管理
     */
    public int updateRyThesis(RyThesis ryThesis);

    /**
     * 审核学位论文管理 (新增)
     */
    public int auditRyThesis(RyThesis ryThesis);

    /**
     * 批量删除学位论文管理
     */
    public int deleteRyThesisByIds(Long[] ids);

    /**
     * 删除学位论文管理信息
     */
    public int deleteRyThesisById(Long id);
}