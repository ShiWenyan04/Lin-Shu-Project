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
import com.ruoyi.system.domain.SpecialInfo;
import com.ruoyi.system.service.ISpecialInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 特种设备考证信息Controller
 * 
 * @author ruoyi
 * @date 2025-12-16
 */
@RestController
@RequestMapping("/system/specialinfo")
public class SpecialInfoController extends BaseController
{
    @Autowired
    private ISpecialInfoService specialInfoService;

    /**
     * 查询特种设备考证信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:specialinfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(SpecialInfo specialInfo)
    {
        startPage();
        List<SpecialInfo> list = specialInfoService.selectSpecialInfoList(specialInfo);
        return getDataTable(list);
    }

    /**
     * 导出特种设备考证信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:specialinfo:export')")
    @Log(title = "特种设备考证信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SpecialInfo specialInfo)
    {
        List<SpecialInfo> list = specialInfoService.selectSpecialInfoList(specialInfo);
        ExcelUtil<SpecialInfo> util = new ExcelUtil<SpecialInfo>(SpecialInfo.class);
        util.exportExcel(response, list, "特种设备考证信息数据");
    }

    /**
     * 获取特种设备考证信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:specialinfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(specialInfoService.selectSpecialInfoById(id));
    }

    /**
     * 新增特种设备考证信息
     */
    @PreAuthorize("@ss.hasPermi('system:specialinfo:add')")
    @Log(title = "特种设备考证信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SpecialInfo specialInfo)
    {
        return toAjax(specialInfoService.insertSpecialInfo(specialInfo));
    }

    /**
     * 修改特种设备考证信息
     */
    @PreAuthorize("@ss.hasPermi('system:specialinfo:edit')")
    @Log(title = "特种设备考证信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SpecialInfo specialInfo)
    {
        return toAjax(specialInfoService.updateSpecialInfo(specialInfo));
    }

    /**
     * 删除特种设备考证信息
     */
    @PreAuthorize("@ss.hasPermi('system:specialinfo:remove')")
    @Log(title = "特种设备考证信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(specialInfoService.deleteSpecialInfoByIds(ids));
    }
}
