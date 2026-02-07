package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.AcademicPaperSubmissionMapper;
import com.ruoyi.system.domain.AcademicPaperSubmission;
import com.ruoyi.system.service.IAcademicPaperSubmissionService;

/**
 * 学术论文投稿Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-28
 */
@Service
public class AcademicPaperSubmissionServiceImpl implements IAcademicPaperSubmissionService 
{
    @Autowired
    private AcademicPaperSubmissionMapper academicPaperSubmissionMapper;

    /**
     * 查询学术论文投稿
     * 
     * @param id 学术论文投稿主键
     * @return 学术论文投稿
     */
    @Override
    public AcademicPaperSubmission selectAcademicPaperSubmissionById(Long id)
    {
        return academicPaperSubmissionMapper.selectAcademicPaperSubmissionById(id);
    }

    /**
     * 查询学术论文投稿列表
     * 
     * @param academicPaperSubmission 学术论文投稿
     * @return 学术论文投稿
     */
    @Override
    public List<AcademicPaperSubmission> selectAcademicPaperSubmissionList(AcademicPaperSubmission academicPaperSubmission)
    {
        return academicPaperSubmissionMapper.selectAcademicPaperSubmissionList(academicPaperSubmission);
    }

    /**
     * 新增学术论文投稿
     * 
     * @param academicPaperSubmission 学术论文投稿
     * @return 结果
     */
    @Override
    public int insertAcademicPaperSubmission(AcademicPaperSubmission academicPaperSubmission)
    {
        academicPaperSubmission.setCreateTime(DateUtils.getNowDate());
        return academicPaperSubmissionMapper.insertAcademicPaperSubmission(academicPaperSubmission);
    }

    /**
     * 修改学术论文投稿
     * 
     * @param academicPaperSubmission 学术论文投稿
     * @return 结果
     */
    @Override
    public int updateAcademicPaperSubmission(AcademicPaperSubmission academicPaperSubmission)
    {
        academicPaperSubmission.setUpdateTime(DateUtils.getNowDate());
        return academicPaperSubmissionMapper.updateAcademicPaperSubmission(academicPaperSubmission);
    }

    /**
     * 批量删除学术论文投稿
     * 
     * @param ids 需要删除的学术论文投稿主键
     * @return 结果
     */
    @Override
    public int deleteAcademicPaperSubmissionByIds(Long[] ids)
    {
        return academicPaperSubmissionMapper.deleteAcademicPaperSubmissionByIds(ids);
    }

    /**
     * 删除学术论文投稿信息
     * 
     * @param id 学术论文投稿主键
     * @return 结果
     */
    @Override
    public int deleteAcademicPaperSubmissionById(Long id)
    {
        return academicPaperSubmissionMapper.deleteAcademicPaperSubmissionById(id);
    }
}
