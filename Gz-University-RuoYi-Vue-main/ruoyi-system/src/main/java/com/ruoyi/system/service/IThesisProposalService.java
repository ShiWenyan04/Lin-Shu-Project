package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.ThesisProposal;

/**
 * 开题与学位论文Service接口
 * 
 * @author ruoyi
 * @date 2025-12-28
 */
public interface IThesisProposalService 
{
    /**
     * 查询开题与学位论文
     * 
     * @param id 开题与学位论文主键
     * @return 开题与学位论文
     */
    public ThesisProposal selectThesisProposalById(Long id);

    /**
     * 查询开题与学位论文列表
     * 
     * @param thesisProposal 开题与学位论文
     * @return 开题与学位论文集合
     */
    public List<ThesisProposal> selectThesisProposalList(ThesisProposal thesisProposal);

    /**
     * 新增开题与学位论文
     * 
     * @param thesisProposal 开题与学位论文
     * @return 结果
     */
    public int insertThesisProposal(ThesisProposal thesisProposal);

    /**
     * 修改开题与学位论文
     * 
     * @param thesisProposal 开题与学位论文
     * @return 结果
     */
    public int updateThesisProposal(ThesisProposal thesisProposal);

    /**
     * 批量删除开题与学位论文
     * 
     * @param ids 需要删除的开题与学位论文主键集合
     * @return 结果
     */
    public int deleteThesisProposalByIds(Long[] ids);

    /**
     * 删除开题与学位论文信息
     * 
     * @param id 开题与学位论文主键
     * @return 结果
     */
    public int deleteThesisProposalById(Long id);
}
