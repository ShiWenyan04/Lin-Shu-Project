package com.ruoyi.research.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.research.domain.ResearchMaterial;
import com.ruoyi.research.service.IResearchMaterialService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 资料清单管理Controller
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
@RestController
@RequestMapping("/research/material")
public class ResearchMaterialController extends BaseController
{
    @Autowired
    private IResearchMaterialService researchMaterialService;

    /**
     * 查询资料清单管理列表
     */
    @PreAuthorize("@ss.hasPermi('research:material:list')")
    @GetMapping("/list")
    public TableDataInfo list(ResearchMaterial researchMaterial)
    {
        startPage();
        List<ResearchMaterial> list = researchMaterialService.selectResearchMaterialList(researchMaterial);
        return getDataTable(list);
    }

    /**
     * 导出资料清单管理列表
     */
    @PreAuthorize("@ss.hasPermi('research:material:export')")
    @Log(title = "资料清单管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ResearchMaterial researchMaterial)
    {
        List<ResearchMaterial> list = researchMaterialService.selectResearchMaterialList(researchMaterial);
        ExcelUtil<ResearchMaterial> util = new ExcelUtil<ResearchMaterial>(ResearchMaterial.class);
        util.exportExcel(response, list, "资料清单管理数据");
    }

    /**
     * 获取资料清单管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('research:material:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(researchMaterialService.selectResearchMaterialById(id));
    }

    /**
     * 新增资料清单管理
     */
    @PreAuthorize("@ss.hasPermi('research:material:add')")
    @Log(title = "资料清单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ResearchMaterial researchMaterial)
    {
        return toAjax(researchMaterialService.insertResearchMaterial(researchMaterial));
    }

    /**
     * 修改资料清单管理
     */
    @PreAuthorize("@ss.hasPermi('research:material:edit')")
    @Log(title = "资料清单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ResearchMaterial researchMaterial)
    {
        return toAjax(researchMaterialService.updateResearchMaterial(researchMaterial));
    }

    /**
     * 删除资料清单管理
     */
    @PreAuthorize("@ss.hasPermi('research:material:remove')")
    @Log(title = "资料清单管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(researchMaterialService.deleteResearchMaterialByIds(ids));
    }
}
