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
import com.ruoyi.system.domain.BusiSpeciesLibrary;
import com.ruoyi.system.service.IBusiSpeciesLibraryService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 物种百科资料库Controller
 * * @author ruoyi
 * @date 2026-01-15
 */
@RestController
@RequestMapping("/system/library")
public class BusiSpeciesLibraryController extends BaseController
{
    @Autowired
    private IBusiSpeciesLibraryService busiSpeciesLibraryService;

    /**
     * 查询物种百科资料库列表
     */
    @PreAuthorize("@ss.hasPermi('system:library:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusiSpeciesLibrary busiSpeciesLibrary)
    {
        startPage();
        List<BusiSpeciesLibrary> list = busiSpeciesLibraryService.selectBusiSpeciesLibraryList(busiSpeciesLibrary);
        return getDataTable(list);
    }

    /**
     * 【新增接口】根据名称查询物种详细信息（供地图页使用）
     * 这一步是为了前端在地图点击点位时，能自动获取信息
     * 不需要严格的 @PreAuthorize 权限，只要登录用户即可查询，方便展示
     */
    @GetMapping(value = "/name/{speciesName}")
    public AjaxResult getInfoByName(@PathVariable("speciesName") String speciesName)
    {
        BusiSpeciesLibrary search = new BusiSpeciesLibrary();
        search.setSpeciesName(speciesName); // 精确匹配
        List<BusiSpeciesLibrary> list = busiSpeciesLibraryService.selectBusiSpeciesLibraryList(search);

        if (list != null && list.size() > 0) {
            // 找到记录，返回第一条
            return success(list.get(0));
        }
        // 没找到记录，返回 success 但 data 为 null，前端据此判断显示“新增表单”
        return success();
    }

    /**
     * 导出物种百科资料库列表
     */
    @PreAuthorize("@ss.hasPermi('system:library:export')")
    @Log(title = "物种百科资料库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusiSpeciesLibrary busiSpeciesLibrary)
    {
        List<BusiSpeciesLibrary> list = busiSpeciesLibraryService.selectBusiSpeciesLibraryList(busiSpeciesLibrary);
        ExcelUtil<BusiSpeciesLibrary> util = new ExcelUtil<BusiSpeciesLibrary>(BusiSpeciesLibrary.class);
        util.exportExcel(response, list, "物种百科资料库数据");
    }

    /**
     * 获取物种百科资料库详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:library:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(busiSpeciesLibraryService.selectBusiSpeciesLibraryById(id));
    }

    /**
     * 新增物种百科资料库
     */
    @PreAuthorize("@ss.hasPermi('system:library:add')")
    @Log(title = "物种百科资料库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusiSpeciesLibrary busiSpeciesLibrary)
    {
        return toAjax(busiSpeciesLibraryService.insertBusiSpeciesLibrary(busiSpeciesLibrary));
    }

    /**
     * 修改物种百科资料库
     */
    @PreAuthorize("@ss.hasPermi('system:library:edit')")
    @Log(title = "物种百科资料库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusiSpeciesLibrary busiSpeciesLibrary)
    {
        return toAjax(busiSpeciesLibraryService.updateBusiSpeciesLibrary(busiSpeciesLibrary));
    }

    /**
     * 删除物种百科资料库
     */
    @PreAuthorize("@ss.hasPermi('system:library:remove')")
    @Log(title = "物种百科资料库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(busiSpeciesLibraryService.deleteBusiSpeciesLibraryByIds(ids));
    }
}