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
import com.ruoyi.system.domain.FieldWork;
import com.ruoyi.system.service.IFieldWorkService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 外业信息Controller
 * 
 * @author ruoyi
 * @date 2025-07-21
 */
@RestController
@RequestMapping("/system/work")
public class FieldWorkController extends BaseController
{
    @Autowired
    private IFieldWorkService fieldWorkService;

    /**
     * 查询外业信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:work:list')")
    @GetMapping("/list")
    public TableDataInfo list(FieldWork fieldWork)
    {
        startPage();
        List<FieldWork> list = fieldWorkService.selectFieldWorkList(fieldWork);
        return getDataTable(list);
    }

    /**
     * 导出外业信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:work:export')")
    @Log(title = "外业信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FieldWork fieldWork)
    {
        List<FieldWork> list = fieldWorkService.selectFieldWorkList(fieldWork);
        ExcelUtil<FieldWork> util = new ExcelUtil<FieldWork>(FieldWork.class);
        util.exportExcel(response, list, "外业信息数据");
    }

    /**
     * 获取外业信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:work:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(fieldWorkService.selectFieldWorkById(id));
    }

    /**
     * 新增外业信息
     */
    @PreAuthorize("@ss.hasPermi('system:work:add')")
    @Log(title = "外业信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FieldWork fieldWork)
    {
        return toAjax(fieldWorkService.insertFieldWork(fieldWork));
    }

    /**
     * 修改外业信息
     */
    @PreAuthorize("@ss.hasPermi('system:work:edit')")
    @Log(title = "外业信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FieldWork fieldWork)
    {
        return toAjax(fieldWorkService.updateFieldWork(fieldWork));
    }

    /**
     * 删除外业信息
     */
    @PreAuthorize("@ss.hasPermi('system:work:remove')")
    @Log(title = "外业信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fieldWorkService.deleteFieldWorkByIds(ids));
    }
}
