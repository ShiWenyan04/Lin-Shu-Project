package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WildlifeSpeciesMapper;
import com.ruoyi.system.domain.WildlifeSpecies;
import com.ruoyi.system.service.IWildlifeSpeciesService;

/**
 * 野生动物图鉴Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-26
 */
@Service
public class WildlifeSpeciesServiceImpl implements IWildlifeSpeciesService
{
    @Autowired
    private WildlifeSpeciesMapper wildlifeSpeciesMapper;

    /**
     * 查询野生动物图鉴
     *
     * @param id 野生动物图鉴主键
     * @return 野生动物图鉴
     */
    @Override
    public WildlifeSpecies selectWildlifeSpeciesById(Long id)
    {
        return wildlifeSpeciesMapper.selectWildlifeSpeciesById(id);
    }

    /**
     * 查询野生动物图鉴列表
     *
     * @param wildlifeSpecies 野生动物图鉴
     * @return 野生动物图鉴
     */
    @Override
    public List<WildlifeSpecies> selectWildlifeSpeciesList(WildlifeSpecies wildlifeSpecies)
    {
        return wildlifeSpeciesMapper.selectWildlifeSpeciesList(wildlifeSpecies);
    }

    /**
     * 新增野生动物图鉴
     *
     * @param wildlifeSpecies 野生动物图鉴
     * @return 结果
     */
    @Override
    public int insertWildlifeSpecies(WildlifeSpecies wildlifeSpecies)
    {
        return wildlifeSpeciesMapper.insertWildlifeSpecies(wildlifeSpecies);
    }

    /**
     * 修改野生动物图鉴
     *
     * @param wildlifeSpecies 野生动物图鉴
     * @return 结果
     */
    @Override
    public int updateWildlifeSpecies(WildlifeSpecies wildlifeSpecies)
    {
        return wildlifeSpeciesMapper.updateWildlifeSpecies(wildlifeSpecies);
    }

    /**
     * 批量删除野生动物图鉴
     *
     * @param ids 需要删除的野生动物图鉴主键
     * @return 结果
     */
    @Override
    public int deleteWildlifeSpeciesByIds(Long[] ids)
    {
        return wildlifeSpeciesMapper.deleteWildlifeSpeciesByIds(ids);
    }

    /**
     * 删除野生动物图鉴信息
     *
     * @param id 野生动物图鉴主键
     * @return 结果
     */
    @Override
    public int deleteWildlifeSpeciesById(Long id)
    {
        return wildlifeSpeciesMapper.deleteWildlifeSpeciesById(id);
    }
}
