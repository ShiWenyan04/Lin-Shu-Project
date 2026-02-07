package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.WildlifeSpecies;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
/**
 * 野生动物图鉴Mapper接口
 * 
 * @author ruoyi
 * @date 2025-07-26
 */
public interface WildlifeSpeciesMapper 
{
    /**
     * 查询野生动物图鉴
     * 
     * @param id 野生动物图鉴主键
     * @return 野生动物图鉴
     */
    public WildlifeSpecies selectWildlifeSpeciesById(Long id);

    /**
     * 查询野生动物图鉴列表
     * 
     * @param wildlifeSpecies 野生动物图鉴
     * @return 野生动物图鉴集合
     */
    public List<WildlifeSpecies> selectWildlifeSpeciesList(WildlifeSpecies wildlifeSpecies);

    /**
     * 新增野生动物图鉴
     * 
     * @param wildlifeSpecies 野生动物图鉴
     * @return 结果
     */
    public int insertWildlifeSpecies(WildlifeSpecies wildlifeSpecies);

    /**
     * 修改野生动物图鉴
     * 
     * @param wildlifeSpecies 野生动物图鉴
     * @return 结果
     */
    public int updateWildlifeSpecies(WildlifeSpecies wildlifeSpecies);

    /**
     * 删除野生动物图鉴
     * 
     * @param id 野生动物图鉴主键
     * @return 结果
     */
    public int deleteWildlifeSpeciesById(Long id);

    /**
     * 批量删除野生动物图鉴
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWildlifeSpeciesByIds(Long[] ids);

    public int countBySpeciesName(@Param("speciesName") String speciesName);
}
