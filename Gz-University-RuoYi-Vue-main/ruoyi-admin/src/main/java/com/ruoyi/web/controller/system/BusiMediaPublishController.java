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
import com.ruoyi.system.domain.BusiMediaPublish;
import com.ruoyi.system.service.IBusiMediaPublishService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 自媒体发布Controller
 * 
 * @author ruoyi
 * @date 2026-01-09
 */
@RestController
@RequestMapping("/system/publish")
public class BusiMediaPublishController extends BaseController
{
    @Autowired
    private IBusiMediaPublishService busiMediaPublishService;

    /**
     * 查询自媒体发布列表
     */
    @PreAuthorize("@ss.hasPermi('system:publish:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusiMediaPublish busiMediaPublish)
    {
        startPage();
        List<BusiMediaPublish> list = busiMediaPublishService.selectBusiMediaPublishList(busiMediaPublish);
        return getDataTable(list);
    }

    /**
     * 导出自媒体发布列表
     */
    @PreAuthorize("@ss.hasPermi('system:publish:export')")
    @Log(title = "自媒体发布", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusiMediaPublish busiMediaPublish)
    {
        List<BusiMediaPublish> list = busiMediaPublishService.selectBusiMediaPublishList(busiMediaPublish);
        ExcelUtil<BusiMediaPublish> util = new ExcelUtil<BusiMediaPublish>(BusiMediaPublish.class);
        util.exportExcel(response, list, "自媒体发布数据");
    }

    /**
     * 获取自媒体发布详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:publish:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(busiMediaPublishService.selectBusiMediaPublishById(id));
    }

    /**
     * 新增自媒体发布
     */
    @PreAuthorize("@ss.hasPermi('system:publish:add')")
    @Log(title = "自媒体发布", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusiMediaPublish busiMediaPublish)
    {
        return toAjax(busiMediaPublishService.insertBusiMediaPublish(busiMediaPublish));
    }

    /**
     * 修改自媒体发布
     */
    @PreAuthorize("@ss.hasPermi('system:publish:edit')")
    @Log(title = "自媒体发布", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusiMediaPublish busiMediaPublish)
    {
        return toAjax(busiMediaPublishService.updateBusiMediaPublish(busiMediaPublish));
    }

    /**
     * 删除自媒体发布
     */
    @PreAuthorize("@ss.hasPermi('system:publish:remove')")
    @Log(title = "自媒体发布", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(busiMediaPublishService.deleteBusiMediaPublishByIds(ids));
    }
}
