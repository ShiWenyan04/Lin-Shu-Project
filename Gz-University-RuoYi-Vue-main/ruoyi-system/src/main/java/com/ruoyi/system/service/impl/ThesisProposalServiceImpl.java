package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ThesisProposalMapper;
import com.ruoyi.system.domain.ThesisProposal;
import com.ruoyi.system.service.IThesisProposalService;
import com.ruoyi.common.utils.SecurityUtils; // 【新增】
import com.ruoyi.common.core.domain.entity.SysUser; // 【新增】
import com.ruoyi.common.exception.ServiceException; // 【新增】用于抛出异常

/**
 * 开题与学位论文Service业务层处理
 * * @author ruoyi
 * @date 2025-12-28
 */
@Service
public class ThesisProposalServiceImpl implements IThesisProposalService
{
    @Autowired
    private ThesisProposalMapper thesisProposalMapper;

    /**
     * 查询开题与学位论文
     * * @param id 开题与学位论文主键
     * @return 开题与学位论文
     */
    @Override
    public ThesisProposal selectThesisProposalById(Long id)
    {
        return thesisProposalMapper.selectThesisProposalById(id);
    }

    /**
     * 查询开题与学位论文列表
     * * @param thesisProposal 开题与学位论文
     * @return 开题与学位论文
     */
    @Override
    public List<ThesisProposal> selectThesisProposalList(ThesisProposal thesisProposal)
    {
        return thesisProposalMapper.selectThesisProposalList(thesisProposal);
    }

    /**
     * 新增开题与学位论文
     * * @param thesisProposal 开题与学位论文
     * @return 结果
     */
    @Override
    public int insertThesisProposal(ThesisProposal thesisProposal)
    {
        thesisProposal.setCreateTime(DateUtils.getNowDate());

        // 【新增逻辑】如果是学生，强制设置学生姓名和创建人，防止乱填
        if (SecurityUtils.hasRole("student")) {
            SysUser currentUser = SecurityUtils.getLoginUser().getUser();
            thesisProposal.setStudentName(currentUser.getNickName()); // 设置为用户昵称
            thesisProposal.setCreateBy(currentUser.getUserName());    // 确保创建人是自己
        }

        return thesisProposalMapper.insertThesisProposal(thesisProposal);
    }

    /**
     * 修改开题与学位论文
     * * @param thesisProposal 开题与学位论文
     * @return 结果
     */
    @Override
    public int updateThesisProposal(ThesisProposal thesisProposal)
    {
        // 【新增逻辑】安全校验：如果是学生，检查是否在修改别人的数据
        if (SecurityUtils.hasRole("student")) {
            ThesisProposal oldData = thesisProposalMapper.selectThesisProposalById(thesisProposal.getId());
            if (oldData != null && !oldData.getCreateBy().equals(SecurityUtils.getUsername())) {
                throw new ServiceException("无权修改他人的论文进度信息！");
            }
        }

        thesisProposal.setUpdateTime(DateUtils.getNowDate());
        return thesisProposalMapper.updateThesisProposal(thesisProposal);
    }

    /**
     * 批量删除开题与学位论文
     * * @param ids 需要删除的开题与学位论文主键
     * @return 结果
     */
    @Override
    public int deleteThesisProposalByIds(Long[] ids)
    {
        return thesisProposalMapper.deleteThesisProposalByIds(ids);
    }

    /**
     * 删除开题与学位论文信息
     * * @param id 开题与学位论文主键
     * @return 结果
     */
    @Override
    public int deleteThesisProposalById(Long id)
    {
        return thesisProposalMapper.deleteThesisProposalById(id);
    }
}