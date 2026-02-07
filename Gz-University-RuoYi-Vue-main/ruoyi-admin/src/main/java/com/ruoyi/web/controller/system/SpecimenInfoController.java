package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SpecimenInfo;
import com.ruoyi.system.service.ISpecimenInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 标本信息Controller
 * 
 * @author ruoyi
 * @date 2025-07-19
 */
@RestController
@RequestMapping("/system/info")
public class SpecimenInfoController extends BaseController
{
    @Autowired
    private ISpecimenInfoService specimenInfoService;

    /**
     * 查询标本信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(SpecimenInfo specimenInfo)
    {
        startPage();
        List<SpecimenInfo> list = specimenInfoService.selectSpecimenInfoList(specimenInfo);
        return getDataTable(list);
    }

    /**
     * 导出标本信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:info:export')")
    @Log(title = "标本信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SpecimenInfo specimenInfo)
    {
        List<SpecimenInfo> list = specimenInfoService.selectSpecimenInfoList(specimenInfo);
        ExcelUtil<SpecimenInfo> util = new ExcelUtil<SpecimenInfo>(SpecimenInfo.class);
        util.exportExcel(response, list, "标本信息数据");
    }

    /**
     * 获取标本信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:info:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(specimenInfoService.selectSpecimenInfoById(id));
    }

    /**
     * 新增标本信息
     */
    @PreAuthorize("@ss.hasPermi('system:info:add')")
    @Log(title = "标本信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SpecimenInfo specimenInfo)
    {
        return toAjax(specimenInfoService.insertSpecimenInfo(specimenInfo));
    }

    /**
     * 修改标本信息
     */
    @PreAuthorize("@ss.hasPermi('system:info:edit')")
    @Log(title = "标本信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SpecimenInfo specimenInfo)
    {
        return toAjax(specimenInfoService.updateSpecimenInfo(specimenInfo));
    }

    /**
     * 删除标本信息
     */
    @PreAuthorize("@ss.hasPermi('system:info:remove')")
    @Log(title = "标本信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(specimenInfoService.deleteSpecimenInfoByIds(ids));
    }
}
