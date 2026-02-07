package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.GraduateData;
import com.ruoyi.system.mapper.GraduateDataMapper;
import com.ruoyi.system.service.IGraduateDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 毕业生资料管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-19
 */
@Service
public class GraduateDataServiceImpl implements IGraduateDataService 
{
    @Autowired
    private GraduateDataMapper graduateDataMapper;

    /**
     * 查询毕业生资料管理
     * 
     * @param id 毕业生资料管理主键
     * @return 毕业生资料管理
     */
    @Override
    public GraduateData selectGraduateDataById(Long id)
    {
        return graduateDataMapper.selectGraduateDataById(id);
    }

    /**
     * 查询毕业生资料管理列表
     * 
     * @param graduateData 毕业生资料管理
     * @return 毕业生资料管理
     */
    @Override
    public List<GraduateData> selectGraduateDataList(GraduateData graduateData)
    {
        return graduateDataMapper.selectGraduateDataList(graduateData);
    }

    /**
     * 新增毕业生资料管理
     * 
     * @param graduateData 毕业生资料管理
     * @return 结果
     */
    @Override
    public int insertGraduateData(GraduateData graduateData)
    {
        graduateData.setCreatedTime(DateUtils.getNowDate());
        graduateData.setCreatedBy(SecurityUtils.getUsername());
        return graduateDataMapper.insertGraduateData(graduateData);
    }

    /**
     * 修改毕业生资料管理
     * 
     * @param graduateData 毕业生资料管理
     * @return 结果
     */
    @Override
    public int updateGraduateData(GraduateData graduateData)
    {
        graduateData.setUpdatedTime(DateUtils.getNowDate());
        return graduateDataMapper.updateGraduateData(graduateData);
    }

    /**
     * 批量删除毕业生资料管理
     * 
     * @param ids 需要删除的毕业生资料管理主键
     * @return 结果
     */
    @Override
    public int deleteGraduateDataByIds(Long[] ids)
    {
        return graduateDataMapper.deleteGraduateDataByIds(ids);
    }

    /**
     * 删除毕业生资料管理信息
     * 
     * @param id 毕业生资料管理主键
     * @return 结果
     */
    @Override
    public int deleteGraduateDataById(Long id)
    {
        return graduateDataMapper.deleteGraduateDataById(id);
    }
}
