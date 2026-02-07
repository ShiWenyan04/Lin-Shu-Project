package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.TeamMember;

import java.util.List;

/**
 * 团队成员Mapper接口
 * 
 * @author ruoyi
 * @date 2025-07-18
 */
public interface TeamMemberMapper 
{
    /**
     * 查询团队成员
     * 
     * @param id 团队成员主键
     * @return 团队成员
     */
    public TeamMember selectTeamMemberById(Long id);

    /**
     * 查询团队成员列表
     * 
     * @param teamMember 团队成员
     * @return 团队成员集合
     */
    public List<TeamMember> selectTeamMemberList(TeamMember teamMember);

    /**
     * 新增团队成员
     * 
     * @param teamMember 团队成员
     * @return 结果
     */
    public int insertTeamMember(TeamMember teamMember);

    /**
     * 修改团队成员
     * 
     * @param teamMember 团队成员
     * @return 结果
     */
    public int updateTeamMember(TeamMember teamMember);

    /**
     * 删除团队成员
     * 
     * @param id 团队成员主键
     * @return 结果
     */
    public int deleteTeamMemberById(Long id);

    /**
     * 批量删除团队成员
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTeamMemberByIds(Long[] ids);
}
