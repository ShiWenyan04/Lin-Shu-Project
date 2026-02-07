package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.AcademicPaperSubmission;

/**
 * 学术论文投稿Service接口
 * 
 * @author ruoyi
 * @date 2025-12-28
 */
public interface IAcademicPaperSubmissionService 
{
    /**
     * 查询学术论文投稿
     * 
     * @param id 学术论文投稿主键
     * @return 学术论文投稿
     */
    public AcademicPaperSubmission selectAcademicPaperSubmissionById(Long id);

    /**
     * 查询学术论文投稿列表
     * 
     * @param academicPaperSubmission 学术论文投稿
     * @return 学术论文投稿集合
     */
    public List<AcademicPaperSubmission> selectAcademicPaperSubmissionList(AcademicPaperSubmission academicPaperSubmission);

    /**
     * 新增学术论文投稿
     * 
     * @param academicPaperSubmission 学术论文投稿
     * @return 结果
     */
    public int insertAcademicPaperSubmission(AcademicPaperSubmission academicPaperSubmission);

    /**
     * 修改学术论文投稿
     * 
     * @param academicPaperSubmission 学术论文投稿
     * @return 结果
     */
    public int updateAcademicPaperSubmission(AcademicPaperSubmission academicPaperSubmission);

    /**
     * 批量删除学术论文投稿
     * 
     * @param ids 需要删除的学术论文投稿主键集合
     * @return 结果
     */
    public int deleteAcademicPaperSubmissionByIds(Long[] ids);

    /**
     * 删除学术论文投稿信息
     * 
     * @param id 学术论文投稿主键
     * @return 结果
     */
    public int deleteAcademicPaperSubmissionById(Long id);
}
