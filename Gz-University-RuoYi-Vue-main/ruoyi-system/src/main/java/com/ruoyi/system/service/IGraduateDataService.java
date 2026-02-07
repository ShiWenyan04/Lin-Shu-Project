package com.ruoyi.system.service;

import com.ruoyi.system.domain.GraduateData;

import java.util.List;

/**
 * 毕业生资料管理Service接口
 * 
 * @author ruoyi
 * @date 2025-07-19
 */
public interface IGraduateDataService 
{
    /**
     * 查询毕业生资料管理
     * 
     * @param id 毕业生资料管理主键
     * @return 毕业生资料管理
     */
    public GraduateData selectGraduateDataById(Long id);

    /**
     * 查询毕业生资料管理列表
     * 
     * @param graduateData 毕业生资料管理
     * @return 毕业生资料管理集合
     */
    public List<GraduateData> selectGraduateDataList(GraduateData graduateData);

    /**
     * 新增毕业生资料管理
     * 
     * @param graduateData 毕业生资料管理
     * @return 结果
     */
    public int insertGraduateData(GraduateData graduateData);

    /**
     * 修改毕业生资料管理
     * 
     * @param graduateData 毕业生资料管理
     * @return 结果
     */
    public int updateGraduateData(GraduateData graduateData);

    /**
     * 批量删除毕业生资料管理
     * 
     * @param ids 需要删除的毕业生资料管理主键集合
     * @return 结果
     */
    public int deleteGraduateDataByIds(Long[] ids);

    /**
     * 删除毕业生资料管理信息
     * 
     * @param id 毕业生资料管理主键
     * @return 结果
     */
    public int deleteGraduateDataById(Long id);
}
