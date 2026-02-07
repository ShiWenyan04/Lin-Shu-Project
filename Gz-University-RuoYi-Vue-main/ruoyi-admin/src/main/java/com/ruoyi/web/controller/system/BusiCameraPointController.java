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
import com.ruoyi.system.domain.BusiCameraPoint;
import com.ruoyi.system.service.IBusiCameraPointService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 红外相机位点信息Controller
 * 
 * @author ruoyi
 * @date 2026-01-12
 */
@RestController
@RequestMapping("/system/point")
public class BusiCameraPointController extends BaseController
{
    @Autowired
    private IBusiCameraPointService busiCameraPointService;

    /**
     * 查询红外相机位点信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:point:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusiCameraPoint busiCameraPoint)
    {
        startPage();
        List<BusiCameraPoint> list = busiCameraPointService.selectBusiCameraPointList(busiCameraPoint);
        return getDataTable(list);
    }

    /**
     * 导出红外相机位点信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:point:export')")
    @Log(title = "红外相机位点信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusiCameraPoint busiCameraPoint)
    {
        List<BusiCameraPoint> list = busiCameraPointService.selectBusiCameraPointList(busiCameraPoint);
        ExcelUtil<BusiCameraPoint> util = new ExcelUtil<BusiCameraPoint>(BusiCameraPoint.class);
        util.exportExcel(response, list, "红外相机位点信息数据");
    }

    /**
     * 获取红外相机位点信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:point:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(busiCameraPointService.selectBusiCameraPointById(id));
    }

    /**
     * 新增红外相机位点信息
     */
    @PreAuthorize("@ss.hasPermi('system:point:add')")
    @Log(title = "红外相机位点信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusiCameraPoint busiCameraPoint)
    {
        return toAjax(busiCameraPointService.insertBusiCameraPoint(busiCameraPoint));
    }

    /**
     * 修改红外相机位点信息
     */
    @PreAuthorize("@ss.hasPermi('system:point:edit')")
    @Log(title = "红外相机位点信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusiCameraPoint busiCameraPoint)
    {
        return toAjax(busiCameraPointService.updateBusiCameraPoint(busiCameraPoint));
    }

    /**
     * 删除红外相机位点信息
     */
    @PreAuthorize("@ss.hasPermi('system:point:remove')")
    @Log(title = "红外相机位点信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(busiCameraPointService.deleteBusiCameraPointByIds(ids));
    }
}
