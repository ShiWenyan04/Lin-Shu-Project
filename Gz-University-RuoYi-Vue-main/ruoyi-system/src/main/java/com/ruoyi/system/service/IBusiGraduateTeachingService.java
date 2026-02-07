package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BusiGraduateTeaching;

/**
 * 研究生助教安排Service接口
 */
public interface IBusiGraduateTeachingService
{
    public BusiGraduateTeaching selectBusiGraduateTeachingById(Long id);

    public List<BusiGraduateTeaching> selectBusiGraduateTeachingList(BusiGraduateTeaching busiGraduateTeaching);

    // 🚀 新增：带 Tab 过滤的查询
    public List<BusiGraduateTeaching> selectBusiGraduateTeachingList(BusiGraduateTeaching busiGraduateTeaching, String tabType, Long userId);

    // 🚀 新增：审核方法
    public int auditBusiGraduateTeaching(Long id, String status, String reason);

    public int insertBusiGraduateTeaching(BusiGraduateTeaching busiGraduateTeaching);
    public int updateBusiGraduateTeaching(BusiGraduateTeaching busiGraduateTeaching);
    public int deleteBusiGraduateTeachingByIds(Long[] ids);
    public int deleteBusiGraduateTeachingById(Long id);
}