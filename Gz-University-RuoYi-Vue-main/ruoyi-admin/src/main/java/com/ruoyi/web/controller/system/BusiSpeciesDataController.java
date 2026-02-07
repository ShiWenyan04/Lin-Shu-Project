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
import com.ruoyi.system.domain.BusiSpeciesData;
import com.ruoyi.system.service.IBusiSpeciesDataService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 物种监测数据Controller
 * 
 * @author ruoyi
 * @date 2026-01-12
 */
@RestController
@RequestMapping("/system/speciesdata")
public class BusiSpeciesDataController extends BaseController
{
    @Autowired
    private IBusiSpeciesDataService busiSpeciesDataService;

    /**
     * 查询物种监测数据列表
     */
    @PreAuthorize("@ss.hasPermi('system:speciesdata:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusiSpeciesData busiSpeciesData)
    {
        startPage();
        List<BusiSpeciesData> list = busiSpeciesDataService.selectBusiSpeciesDataList(busiSpeciesData);
        return getDataTable(list);
    }

    /**
     * 导出物种监测数据列表
     */
    @PreAuthorize("@ss.hasPermi('system:speciesdata:export')")
    @Log(title = "物种监测数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusiSpeciesData busiSpeciesData)
    {
        List<BusiSpeciesData> list = busiSpeciesDataService.selectBusiSpeciesDataList(busiSpeciesData);
        ExcelUtil<BusiSpeciesData> util = new ExcelUtil<BusiSpeciesData>(BusiSpeciesData.class);
        util.exportExcel(response, list, "物种监测数据数据");
    }

    /**
     * 获取物种监测数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:speciesdata:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(busiSpeciesDataService.selectBusiSpeciesDataById(id));
    }

    /**
     * 新增物种监测数据
     */
    @PreAuthorize("@ss.hasPermi('system:speciesdata:add')")
    @Log(title = "物种监测数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusiSpeciesData busiSpeciesData)
    {
        return toAjax(busiSpeciesDataService.insertBusiSpeciesData(busiSpeciesData));
    }

    /**
     * 修改物种监测数据
     */
    @PreAuthorize("@ss.hasPermi('system:speciesdata:edit')")
    @Log(title = "物种监测数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusiSpeciesData busiSpeciesData)
    {
        return toAjax(busiSpeciesDataService.updateBusiSpeciesData(busiSpeciesData));
    }

    /**
     * 删除物种监测数据
     */
    @PreAuthorize("@ss.hasPermi('system:speciesdata:remove')")
    @Log(title = "物种监测数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(busiSpeciesDataService.deleteBusiSpeciesDataByIds(ids));
    }
}
