package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BusiProject;

/**
 * 项目信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-17
 */
public interface BusiProjectMapper 
{
    /**
     * 查询项目信息
     * 
     * @param projectId 项目信息主键
     * @return 项目信息
     */
    public BusiProject selectBusiProjectByProjectId(Long projectId);

    /**
     * 查询项目信息列表
     * 
     * @param busiProject 项目信息
     * @return 项目信息集合
     */
    public List<BusiProject> selectBusiProjectList(BusiProject busiProject);

    /**
     * 新增项目信息
     * 
     * @param busiProject 项目信息
     * @return 结果
     */
    public int insertBusiProject(BusiProject busiProject);

    /**
     * 修改项目信息
     * 
     * @param busiProject 项目信息
     * @return 结果
     */
    public int updateBusiProject(BusiProject busiProject);

    /**
     * 删除项目信息
     * 
     * @param projectId 项目信息主键
     * @return 结果
     */
    public int deleteBusiProjectByProjectId(Long projectId);

    /**
     * 批量删除项目信息
     * 
     * @param projectIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusiProjectByProjectIds(Long[] projectIds);
}
