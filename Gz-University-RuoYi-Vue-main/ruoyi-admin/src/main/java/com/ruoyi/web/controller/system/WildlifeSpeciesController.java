package com.ruoyi.web.controller.system;

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
import com.ruoyi.system.domain.WildlifeSpecies;
import com.ruoyi.system.service.IWildlifeSpeciesService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 野生动物图鉴Controller
 * 
 * @author ruoyi
 * @date 2025-07-26
 */
@RestController
@RequestMapping("/system/species")
public class WildlifeSpeciesController extends BaseController
{
    @Autowired
    private IWildlifeSpeciesService wildlifeSpeciesService;

    /**
     * 查询野生动物图鉴列表
     */
    @PreAuthorize("@ss.hasPermi('system:species:list')")
    @GetMapping("/list")
    public TableDataInfo list(WildlifeSpecies wildlifeSpecies)
    {
        startPage();
        List<WildlifeSpecies> list = wildlifeSpeciesService.selectWildlifeSpeciesList(wildlifeSpecies);
        return getDataTable(list);
    }

    /**
     * 导出野生动物图鉴列表
     */
    @PreAuthorize("@ss.hasPermi('system:species:export')")
    @Log(title = "野生动物图鉴", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WildlifeSpecies wildlifeSpecies)
    {
        List<WildlifeSpecies> list = wildlifeSpeciesService.selectWildlifeSpeciesList(wildlifeSpecies);
        ExcelUtil<WildlifeSpecies> util = new ExcelUtil<WildlifeSpecies>(WildlifeSpecies.class);
        util.exportExcel(response, list, "野生动物图鉴数据");
    }

    /**
     * 获取野生动物图鉴详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:species:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wildlifeSpeciesService.selectWildlifeSpeciesById(id));
    }

    /**
     * 新增野生动物图鉴
     */
    @PreAuthorize("@ss.hasPermi('system:species:add')")
    @Log(title = "野生动物图鉴", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WildlifeSpecies wildlifeSpecies)
    {
        return toAjax(wildlifeSpeciesService.insertWildlifeSpecies(wildlifeSpecies));
    }

    /**
     * 修改野生动物图鉴
     */
    @PreAuthorize("@ss.hasPermi('system:species:edit')")
    @Log(title = "野生动物图鉴", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WildlifeSpecies wildlifeSpecies)
    {
        return toAjax(wildlifeSpeciesService.updateWildlifeSpecies(wildlifeSpecies));
    }

    /**
     * 删除野生动物图鉴
     */
    @PreAuthorize("@ss.hasPermi('system:species:remove')")
    @Log(title = "野生动物图鉴", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wildlifeSpeciesService.deleteWildlifeSpeciesByIds(ids));
    }
}
