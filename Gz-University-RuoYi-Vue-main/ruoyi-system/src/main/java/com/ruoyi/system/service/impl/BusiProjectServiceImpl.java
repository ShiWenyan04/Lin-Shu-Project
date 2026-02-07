package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BusiProjectMapper;
import com.ruoyi.system.domain.BusiProject;
import com.ruoyi.system.service.IBusiProjectService;

/**
 * 项目信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-17
 */
@Service
public class BusiProjectServiceImpl implements IBusiProjectService 
{
    @Autowired
    private BusiProjectMapper busiProjectMapper;

    /**
     * 查询项目信息
     * 
     * @param projectId 项目信息主键
     * @return 项目信息
     */
    @Override
    public BusiProject selectBusiProjectByProjectId(Long projectId)
    {
        return busiProjectMapper.selectBusiProjectByProjectId(projectId);
    }

    /**
     * 查询项目信息列表
     * 
     * @param busiProject 项目信息
     * @return 项目信息
     */
    @Override
    public List<BusiProject> selectBusiProjectList(BusiProject busiProject)
    {
        return busiProjectMapper.selectBusiProjectList(busiProject);
    }

    /**
     * 新增项目信息
     * 
     * @param busiProject 项目信息
     * @return 结果
     */
    @Override
    public int insertBusiProject(BusiProject busiProject)
    {
        busiProject.setCreateTime(DateUtils.getNowDate());
        return busiProjectMapper.insertBusiProject(busiProject);
    }

    /**
     * 修改项目信息
     * 
     * @param busiProject 项目信息
     * @return 结果
     */
    @Override
    public int updateBusiProject(BusiProject busiProject)
    {
        busiProject.setUpdateTime(DateUtils.getNowDate());
        return busiProjectMapper.updateBusiProject(busiProject);
    }

    /**
     * 批量删除项目信息
     * 
     * @param projectIds 需要删除的项目信息主键
     * @return 结果
     */
    @Override
    public int deleteBusiProjectByProjectIds(Long[] projectIds)
    {
        return busiProjectMapper.deleteBusiProjectByProjectIds(projectIds);
    }

    /**
     * 删除项目信息信息
     * 
     * @param projectId 项目信息主键
     * @return 结果
     */
    @Override
    public int deleteBusiProjectByProjectId(Long projectId)
    {
        return busiProjectMapper.deleteBusiProjectByProjectId(projectId);
    }
}
