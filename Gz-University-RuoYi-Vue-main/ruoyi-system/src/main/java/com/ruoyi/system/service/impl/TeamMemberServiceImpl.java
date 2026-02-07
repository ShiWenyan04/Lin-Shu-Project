package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.TeamMember;
import com.ruoyi.system.mapper.TeamMemberMapper;
import com.ruoyi.system.service.ITeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 团队成员Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-18
 */
@Service
public class TeamMemberServiceImpl implements ITeamMemberService 
{
    @Autowired
    private TeamMemberMapper teamMemberMapper;

    /**
     * 查询团队成员
     * 
     * @param id 团队成员主键
     * @return 团队成员
     */
    @Override
    public TeamMember selectTeamMemberById(Long id)
    {
        return teamMemberMapper.selectTeamMemberById(id);
    }

    /**
     * 查询团队成员列表
     * 
     * @param teamMember 团队成员
     * @return 团队成员
     */
    @Override
    public List<TeamMember> selectTeamMemberList(TeamMember teamMember)
    {
        return teamMemberMapper.selectTeamMemberList(teamMember);
    }

    /**
     * 新增团队成员
     * 
     * @param teamMember 团队成员
     * @return 结果
     */
    @Override
    public int insertTeamMember(TeamMember teamMember)
    {
        return teamMemberMapper.insertTeamMember(teamMember);
    }

    /**
     * 修改团队成员
     * 
     * @param teamMember 团队成员
     * @return 结果
     */
    @Override
    public int updateTeamMember(TeamMember teamMember)
    {
        return teamMemberMapper.updateTeamMember(teamMember);
    }

    /**
     * 批量删除团队成员
     * 
     * @param ids 需要删除的团队成员主键
     * @return 结果
     */
    @Override
    public int deleteTeamMemberByIds(Long[] ids)
    {
        return teamMemberMapper.deleteTeamMemberByIds(ids);
    }

    /**
     * 删除团队成员信息
     * 
     * @param id 团队成员主键
     * @return 结果
     */
    @Override
    public int deleteTeamMemberById(Long id)
    {
        return teamMemberMapper.deleteTeamMemberById(id);
    }
}
