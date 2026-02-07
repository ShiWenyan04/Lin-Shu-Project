package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.GraduateData;
import com.ruoyi.system.service.IGraduateDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 毕业生资料管理Controller
 * 
 * @author ruoyi
 * @date 2025-07-19
 */
@RestController
@RequestMapping("/system/data")
public class GraduateDataController extends BaseController
{
    @Autowired
    private IGraduateDataService graduateDataService;

    /**
     * 查询毕业生资料管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:data:list')")
    @GetMapping("/list")
    public TableDataInfo list(GraduateData graduateData)
    {
        startPage();
        List<GraduateData> list = graduateDataService.selectGraduateDataList(graduateData);
        return getDataTable(list);
    }

    /**
     * 导出毕业生资料管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:data:export')")
    @Log(title = "毕业生资料管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GraduateData graduateData)
    {
        List<GraduateData> list = graduateDataService.selectGraduateDataList(graduateData);
        ExcelUtil<GraduateData> util = new ExcelUtil<GraduateData>(GraduateData.class);
        util.exportExcel(response, list, "毕业生资料管理数据");
    }

    /**
     * 获取毕业生资料管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:data:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(graduateDataService.selectGraduateDataById(id));
    }

    /**
     * 新增毕业生资料管理
     */
    @PreAuthorize("@ss.hasPermi('system:data:add')")
    @Log(title = "毕业生资料管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GraduateData graduateData)
    {
        return toAjax(graduateDataService.insertGraduateData(graduateData));
    }

    /**
     * 修改毕业生资料管理
     */
    @PreAuthorize("@ss.hasPermi('system:data:edit')")
    @Log(title = "毕业生资料管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GraduateData graduateData)
    {
        return toAjax(graduateDataService.updateGraduateData(graduateData));
    }

    /**
     * 删除毕业生资料管理
     */
    @PreAuthorize("@ss.hasPermi('system:data:remove')")
    @Log(title = "毕业生资料管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(graduateDataService.deleteGraduateDataByIds(ids));
    }
}
