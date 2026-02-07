package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.FieldSurvey;

/**
 * 外业调查设备Service接口
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
public interface IFieldSurveyService 
{
    /**
     * 查询外业调查设备
     * 
     * @param id 外业调查设备主键
     * @return 外业调查设备
     */
    public FieldSurvey selectFieldSurveyById(Long id);

    /**
     * 查询外业调查设备列表
     * 
     * @param fieldSurvey 外业调查设备
     * @return 外业调查设备集合
     */
    public List<FieldSurvey> selectFieldSurveyList(FieldSurvey fieldSurvey);

    /**
     * 新增外业调查设备
     * 
     * @param fieldSurvey 外业调查设备
     * @return 结果
     */
    public int insertFieldSurvey(FieldSurvey fieldSurvey);

    /**
     * 修改外业调查设备
     * 
     * @param fieldSurvey 外业调查设备
     * @return 结果
     */
    public int updateFieldSurvey(FieldSurvey fieldSurvey);

    /**
     * 批量删除外业调查设备
     * 
     * @param ids 需要删除的外业调查设备主键集合
     * @return 结果
     */
    public int deleteFieldSurveyByIds(Long[] ids);

    /**
     * 删除外业调查设备信息
     * 
     * @param id 外业调查设备主键
     * @return 结果
     */
    public int deleteFieldSurveyById(Long id);
}
