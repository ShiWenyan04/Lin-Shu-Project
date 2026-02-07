package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.AcademicPaper;

/**
 * 学术论文写作Service接口
 * 
 * @author ruoyi
 * @date 2025-12-28
 */
public interface IAcademicPaperService 
{
    /**
     * 查询学术论文写作
     * 
     * @param id 学术论文写作主键
     * @return 学术论文写作
     */
    public AcademicPaper selectAcademicPaperById(Long id);

    /**
     * 查询学术论文写作列表
     * 
     * @param academicPaper 学术论文写作
     * @return 学术论文写作集合
     */
    public List<AcademicPaper> selectAcademicPaperList(AcademicPaper academicPaper);

    /**
     * 新增学术论文写作
     * 
     * @param academicPaper 学术论文写作
     * @return 结果
     */
    public int insertAcademicPaper(AcademicPaper academicPaper);

    /**
     * 修改学术论文写作
     * 
     * @param academicPaper 学术论文写作
     * @return 结果
     */
    public int updateAcademicPaper(AcademicPaper academicPaper);

    /**
     * 批量删除学术论文写作
     * 
     * @param ids 需要删除的学术论文写作主键集合
     * @return 结果
     */
    public int deleteAcademicPaperByIds(Long[] ids);

    /**
     * 删除学术论文写作信息
     * 
     * @param id 学术论文写作主键
     * @return 结果
     */
    public int deleteAcademicPaperById(Long id);
}
