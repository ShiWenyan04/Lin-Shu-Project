package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.FieldWork;

/**
 * 外业信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-07-21
 */
public interface FieldWorkMapper 
{
    /**
     * 查询外业信息
     * 
     * @param id 外业信息主键
     * @return 外业信息
     */
    public FieldWork selectFieldWorkById(Long id);

    /**
     * 查询外业信息列表
     * 
     * @param fieldWork 外业信息
     * @return 外业信息集合
     */
    public List<FieldWork> selectFieldWorkList(FieldWork fieldWork);

    /**
     * 新增外业信息
     * 
     * @param fieldWork 外业信息
     * @return 结果
     */
    public int insertFieldWork(FieldWork fieldWork);

    /**
     * 修改外业信息
     * 
     * @param fieldWork 外业信息
     * @return 结果
     */
    public int updateFieldWork(FieldWork fieldWork);

    /**
     * 删除外业信息
     * 
     * @param id 外业信息主键
     * @return 结果
     */
    public int deleteFieldWorkById(Long id);

    /**
     * 批量删除外业信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFieldWorkByIds(Long[] ids);
}
