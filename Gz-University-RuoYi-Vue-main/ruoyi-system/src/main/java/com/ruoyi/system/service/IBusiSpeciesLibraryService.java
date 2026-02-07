package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BusiSpeciesLibrary;

/**
 * 物种百科资料库Service接口
 * 
 * @author ruoyi
 * @date 2026-01-15
 */
public interface IBusiSpeciesLibraryService 
{
    /**
     * 查询物种百科资料库
     * 
     * @param id 物种百科资料库主键
     * @return 物种百科资料库
     */
    public BusiSpeciesLibrary selectBusiSpeciesLibraryById(Long id);

    /**
     * 查询物种百科资料库列表
     * 
     * @param busiSpeciesLibrary 物种百科资料库
     * @return 物种百科资料库集合
     */
    public List<BusiSpeciesLibrary> selectBusiSpeciesLibraryList(BusiSpeciesLibrary busiSpeciesLibrary);

    /**
     * 新增物种百科资料库
     * 
     * @param busiSpeciesLibrary 物种百科资料库
     * @return 结果
     */
    public int insertBusiSpeciesLibrary(BusiSpeciesLibrary busiSpeciesLibrary);

    /**
     * 修改物种百科资料库
     * 
     * @param busiSpeciesLibrary 物种百科资料库
     * @return 结果
     */
    public int updateBusiSpeciesLibrary(BusiSpeciesLibrary busiSpeciesLibrary);

    /**
     * 批量删除物种百科资料库
     * 
     * @param ids 需要删除的物种百科资料库主键集合
     * @return 结果
     */
    public int deleteBusiSpeciesLibraryByIds(Long[] ids);

    /**
     * 删除物种百科资料库信息
     * 
     * @param id 物种百科资料库主键
     * @return 结果
     */
    public int deleteBusiSpeciesLibraryById(Long id);
}
