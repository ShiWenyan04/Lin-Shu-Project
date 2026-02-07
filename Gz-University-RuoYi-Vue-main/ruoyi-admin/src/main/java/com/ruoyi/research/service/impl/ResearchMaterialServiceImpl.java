package com.ruoyi.research.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.research.mapper.ResearchMaterialMapper;
import com.ruoyi.research.domain.ResearchMaterial;
import com.ruoyi.research.service.IResearchMaterialService;

/**
 * 资料清单管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
@Service
public class ResearchMaterialServiceImpl implements IResearchMaterialService 
{
    @Autowired
    private ResearchMaterialMapper researchMaterialMapper;

    /**
     * 查询资料清单管理
     * 
     * @param id 资料清单管理主键
     * @return 资料清单管理
     */
    @Override
    public ResearchMaterial selectResearchMaterialById(Long id)
    {
        return researchMaterialMapper.selectResearchMaterialById(id);
    }

    /**
     * 查询资料清单管理列表
     * 
     * @param researchMaterial 资料清单管理
     * @return 资料清单管理
     */
    @Override
    public List<ResearchMaterial> selectResearchMaterialList(ResearchMaterial researchMaterial)
    {
        return researchMaterialMapper.selectResearchMaterialList(researchMaterial);
    }

    /**
     * 新增资料清单管理
     *
     * @param researchMaterial 资料清单管理
     * @return 结果
     */
    @Override
    public int insertResearchMaterial(ResearchMaterial researchMaterial)
    {
        researchMaterial.setCreateTime(DateUtils.getNowDate());
        return researchMaterialMapper.insertResearchMaterial(researchMaterial);
    }

    /**
     * 修改资料清单管理
     * 
     * @param researchMaterial 资料清单管理
     * @return 结果
     */
    @Override
    public int updateResearchMaterial(ResearchMaterial researchMaterial)
    {
        researchMaterial.setUpdateTime(DateUtils.getNowDate());
        return researchMaterialMapper.updateResearchMaterial(researchMaterial);
    }

    /**
     * 批量删除资料清单管理
     * 
     * @param ids 需要删除的资料清单管理主键
     * @return 结果
     */
    @Override
    public int deleteResearchMaterialByIds(Long[] ids)
    {
        return researchMaterialMapper.deleteResearchMaterialByIds(ids);
    }

    /**
     * 删除资料清单管理信息
     * 
     * @param id 资料清单管理主键
     * @return 结果
     */
    @Override
    public int deleteResearchMaterialById(Long id)
    {
        return researchMaterialMapper.deleteResearchMaterialById(id);
    }
}
