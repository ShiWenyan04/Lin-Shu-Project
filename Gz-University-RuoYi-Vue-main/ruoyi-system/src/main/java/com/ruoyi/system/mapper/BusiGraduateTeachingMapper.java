package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BusiGraduateTeaching;

/**
 * 研究生助教安排Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-08
 */
public interface BusiGraduateTeachingMapper 
{
    /**
     * 查询研究生助教安排
     * 
     * @param id 研究生助教安排主键
     * @return 研究生助教安排
     */
    public BusiGraduateTeaching selectBusiGraduateTeachingById(Long id);

    /**
     * 查询研究生助教安排列表
     * 
     * @param busiGraduateTeaching 研究生助教安排
     * @return 研究生助教安排集合
     */
    public List<BusiGraduateTeaching> selectBusiGraduateTeachingList(BusiGraduateTeaching busiGraduateTeaching);

    /**
     * 新增研究生助教安排
     * 
     * @param busiGraduateTeaching 研究生助教安排
     * @return 结果
     */
    public int insertBusiGraduateTeaching(BusiGraduateTeaching busiGraduateTeaching);

    /**
     * 修改研究生助教安排
     * 
     * @param busiGraduateTeaching 研究生助教安排
     * @return 结果
     */
    public int updateBusiGraduateTeaching(BusiGraduateTeaching busiGraduateTeaching);

    /**
     * 删除研究生助教安排
     * 
     * @param id 研究生助教安排主键
     * @return 结果
     */
    public int deleteBusiGraduateTeachingById(Long id);

    /**
     * 批量删除研究生助教安排
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusiGraduateTeachingByIds(Long[] ids);
}
