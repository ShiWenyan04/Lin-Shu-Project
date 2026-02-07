package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.FieldSurveyMapper;
import com.ruoyi.system.domain.FieldSurvey;
import com.ruoyi.system.service.IFieldSurveyService;

/**
 * 外业调查设备Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
@Service
public class FieldSurveyServiceImpl implements IFieldSurveyService 
{
    @Autowired
    private FieldSurveyMapper fieldSurveyMapper;

    /**
     * 查询外业调查设备
     * 
     * @param id 外业调查设备主键
     * @return 外业调查设备
     */
    @Override
    public FieldSurvey selectFieldSurveyById(Long id)
    {
        return fieldSurveyMapper.selectFieldSurveyById(id);
    }

    /**
     * 查询外业调查设备列表
     * 
     * @param fieldSurvey 外业调查设备
     * @return 外业调查设备
     */
    @Override
    public List<FieldSurvey> selectFieldSurveyList(FieldSurvey fieldSurvey)
    {
        return fieldSurveyMapper.selectFieldSurveyList(fieldSurvey);
    }

    /**
     * 新增外业调查设备
     * 
     * @param fieldSurvey 外业调查设备
     * @return 结果
     */
    @Override
    public int insertFieldSurvey(FieldSurvey fieldSurvey)
    {
        fieldSurvey.setCreateTime(DateUtils.getNowDate());
        return fieldSurveyMapper.insertFieldSurvey(fieldSurvey);
    }

    /**
     * 修改外业调查设备
     * 
     * @param fieldSurvey 外业调查设备
     * @return 结果
     */
    @Override
    public int updateFieldSurvey(FieldSurvey fieldSurvey)
    {
        fieldSurvey.setUpdateTime(DateUtils.getNowDate());
        return fieldSurveyMapper.updateFieldSurvey(fieldSurvey);
    }

    /**
     * 批量删除外业调查设备
     * 
     * @param ids 需要删除的外业调查设备主键
     * @return 结果
     */
    @Override
    public int deleteFieldSurveyByIds(Long[] ids)
    {
        return fieldSurveyMapper.deleteFieldSurveyByIds(ids);
    }

    /**
     * 删除外业调查设备信息
     * 
     * @param id 外业调查设备主键
     * @return 结果
     */
    @Override
    public int deleteFieldSurveyById(Long id)
    {
        return fieldSurveyMapper.deleteFieldSurveyById(id);
    }
}
