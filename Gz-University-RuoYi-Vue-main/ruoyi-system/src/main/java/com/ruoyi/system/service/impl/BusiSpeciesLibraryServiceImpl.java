package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BusiSpeciesLibraryMapper;
import com.ruoyi.system.domain.BusiSpeciesLibrary;
import com.ruoyi.system.service.IBusiSpeciesLibraryService;

/**
 * 物种百科资料库Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-15
 */
@Service
public class BusiSpeciesLibraryServiceImpl implements IBusiSpeciesLibraryService 
{
    @Autowired
    private BusiSpeciesLibraryMapper busiSpeciesLibraryMapper;

    /**
     * 查询物种百科资料库
     * 
     * @param id 物种百科资料库主键
     * @return 物种百科资料库
     */
    @Override
    public BusiSpeciesLibrary selectBusiSpeciesLibraryById(Long id)
    {
        return busiSpeciesLibraryMapper.selectBusiSpeciesLibraryById(id);
    }

    /**
     * 查询物种百科资料库列表
     * 
     * @param busiSpeciesLibrary 物种百科资料库
     * @return 物种百科资料库
     */
    @Override
    public List<BusiSpeciesLibrary> selectBusiSpeciesLibraryList(BusiSpeciesLibrary busiSpeciesLibrary)
    {
        return busiSpeciesLibraryMapper.selectBusiSpeciesLibraryList(busiSpeciesLibrary);
    }

    /**
     * 新增物种百科资料库
     * 
     * @param busiSpeciesLibrary 物种百科资料库
     * @return 结果
     */
    @Override
    public int insertBusiSpeciesLibrary(BusiSpeciesLibrary busiSpeciesLibrary)
    {
        busiSpeciesLibrary.setCreateTime(DateUtils.getNowDate());
        return busiSpeciesLibraryMapper.insertBusiSpeciesLibrary(busiSpeciesLibrary);
    }

    /**
     * 修改物种百科资料库
     * 
     * @param busiSpeciesLibrary 物种百科资料库
     * @return 结果
     */
    @Override
    public int updateBusiSpeciesLibrary(BusiSpeciesLibrary busiSpeciesLibrary)
    {
        busiSpeciesLibrary.setUpdateTime(DateUtils.getNowDate());
        return busiSpeciesLibraryMapper.updateBusiSpeciesLibrary(busiSpeciesLibrary);
    }

    /**
     * 批量删除物种百科资料库
     * 
     * @param ids 需要删除的物种百科资料库主键
     * @return 结果
     */
    @Override
    public int deleteBusiSpeciesLibraryByIds(Long[] ids)
    {
        return busiSpeciesLibraryMapper.deleteBusiSpeciesLibraryByIds(ids);
    }

    /**
     * 删除物种百科资料库信息
     * 
     * @param id 物种百科资料库主键
     * @return 结果
     */
    @Override
    public int deleteBusiSpeciesLibraryById(Long id)
    {
        return busiSpeciesLibraryMapper.deleteBusiSpeciesLibraryById(id);
    }
}
