package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.AcademicPaperMapper;
import com.ruoyi.system.domain.AcademicPaper;
import com.ruoyi.system.service.IAcademicPaperService;

/**
 * 学术论文写作Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-28
 */
@Service
public class AcademicPaperServiceImpl implements IAcademicPaperService 
{
    @Autowired
    private AcademicPaperMapper academicPaperMapper;

    /**
     * 查询学术论文写作
     * 
     * @param id 学术论文写作主键
     * @return 学术论文写作
     */
    @Override
    public AcademicPaper selectAcademicPaperById(Long id)
    {
        return academicPaperMapper.selectAcademicPaperById(id);
    }

    /**
     * 查询学术论文写作列表
     * 
     * @param academicPaper 学术论文写作
     * @return 学术论文写作
     */
    @Override
    public List<AcademicPaper> selectAcademicPaperList(AcademicPaper academicPaper)
    {
        return academicPaperMapper.selectAcademicPaperList(academicPaper);
    }

    /**
     * 新增学术论文写作
     * 
     * @param academicPaper 学术论文写作
     * @return 结果
     */
    @Override
    public int insertAcademicPaper(AcademicPaper academicPaper)
    {
        academicPaper.setCreateTime(DateUtils.getNowDate());
        return academicPaperMapper.insertAcademicPaper(academicPaper);
    }

    /**
     * 修改学术论文写作
     * 
     * @param academicPaper 学术论文写作
     * @return 结果
     */
    @Override
    public int updateAcademicPaper(AcademicPaper academicPaper)
    {
        academicPaper.setUpdateTime(DateUtils.getNowDate());
        return academicPaperMapper.updateAcademicPaper(academicPaper);
    }

    /**
     * 批量删除学术论文写作
     * 
     * @param ids 需要删除的学术论文写作主键
     * @return 结果
     */
    @Override
    public int deleteAcademicPaperByIds(Long[] ids)
    {
        return academicPaperMapper.deleteAcademicPaperByIds(ids);
    }

    /**
     * 删除学术论文写作信息
     * 
     * @param id 学术论文写作主键
     * @return 结果
     */
    @Override
    public int deleteAcademicPaperById(Long id)
    {
        return academicPaperMapper.deleteAcademicPaperById(id);
    }
}
