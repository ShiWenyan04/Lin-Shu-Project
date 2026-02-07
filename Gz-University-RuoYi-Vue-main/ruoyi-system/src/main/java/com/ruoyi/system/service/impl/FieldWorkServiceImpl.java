package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.FieldWorkMapper;
import com.ruoyi.system.domain.FieldWork;
import com.ruoyi.system.service.IFieldWorkService;

/**
 * 外业信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-21
 */
@Service
public class FieldWorkServiceImpl implements IFieldWorkService 
{
    @Autowired
    private FieldWorkMapper fieldWorkMapper;

    /**
     * 查询外业信息
     * 
     * @param id 外业信息主键
     * @return 外业信息
     */
    @Override
    public FieldWork selectFieldWorkById(Long id)
    {
        return fieldWorkMapper.selectFieldWorkById(id);
    }

    /**
     * 查询外业信息列表
     * 
     * @param fieldWork 外业信息
     * @return 外业信息
     */
    @Override
    public List<FieldWork> selectFieldWorkList(FieldWork fieldWork)
    {
        return fieldWorkMapper.selectFieldWorkList(fieldWork);
    }

    /**
     * 新增外业信息
     * 
     * @param fieldWork 外业信息
     * @return 结果
     */
    @Override
    public int insertFieldWork(FieldWork fieldWork)
    {
        return fieldWorkMapper.insertFieldWork(fieldWork);
    }

    /**
     * 修改外业信息
     * 
     * @param fieldWork 外业信息
     * @return 结果
     */
    @Override
    public int updateFieldWork(FieldWork fieldWork)
    {
        return fieldWorkMapper.updateFieldWork(fieldWork);
    }

    /**
     * 批量删除外业信息
     * 
     * @param ids 需要删除的外业信息主键
     * @return 结果
     */
    @Override
    public int deleteFieldWorkByIds(Long[] ids)
    {
        return fieldWorkMapper.deleteFieldWorkByIds(ids);
    }

    /**
     * 删除外业信息信息
     * 
     * @param id 外业信息主键
     * @return 结果
     */
    @Override
    public int deleteFieldWorkById(Long id)
    {
        return fieldWorkMapper.deleteFieldWorkById(id);
    }
}
