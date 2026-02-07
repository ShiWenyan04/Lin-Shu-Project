package com.ruoyi.research.service;

import java.util.List;
import com.ruoyi.research.domain.ResearchMaterial;

/**
 * 资料清单管理Service接口
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
public interface IResearchMaterialService 
{
    /**
     * 查询资料清单管理
     * 
     * @param id 资料清单管理主键
     * @return 资料清单管理
     */
    public ResearchMaterial selectResearchMaterialById(Long id);

    /**
     * 查询资料清单管理列表
     * 
     * @param researchMaterial 资料清单管理
     * @return 资料清单管理集合
     */
    public List<ResearchMaterial> selectResearchMaterialList(ResearchMaterial researchMaterial);

    /**
     * 新增资料清单管理
     * 
     * @param researchMaterial 资料清单管理
     * @return 结果
     */
    public int insertResearchMaterial(ResearchMaterial researchMaterial);

    /**
     * 修改资料清单管理
     * 
     * @param researchMaterial 资料清单管理
     * @return 结果
     */
    public int updateResearchMaterial(ResearchMaterial researchMaterial);

    /**
     * 批量删除资料清单管理
     * 
     * @param ids 需要删除的资料清单管理主键集合
     * @return 结果
     */
    public int deleteResearchMaterialByIds(Long[] ids);

    /**
     * 删除资料清单管理信息
     * 
     * @param id 资料清单管理主键
     * @return 结果
     */
    public int deleteResearchMaterialById(Long id);
}
