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
import com.ruoyi.system.domain.BusiFieldDoc;
import com.ruoyi.system.service.IBusiFieldDocService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 外业文档模板Controller
 * 
 * @author ruoyi
 * @date 2025-12-22
 */
@RestController
@RequestMapping("/system/doc")
public class BusiFieldDocController extends BaseController
{
    @Autowired
    private IBusiFieldDocService busiFieldDocService;

    /**
     * 查询外业文档模板列表
     */
    @PreAuthorize("@ss.hasPermi('system:doc:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusiFieldDoc busiFieldDoc)
    {
        startPage();
        List<BusiFieldDoc> list = busiFieldDocService.selectBusiFieldDocList(busiFieldDoc);
        return getDataTable(list);
    }

    /**
     * 导出外业文档模板列表
     */
    @PreAuthorize("@ss.hasPermi('system:doc:export')")
    @Log(title = "外业文档模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusiFieldDoc busiFieldDoc)
    {
        List<BusiFieldDoc> list = busiFieldDocService.selectBusiFieldDocList(busiFieldDoc);
        ExcelUtil<BusiFieldDoc> util = new ExcelUtil<BusiFieldDoc>(BusiFieldDoc.class);
        util.exportExcel(response, list, "外业文档模板数据");
    }

    /**
     * 获取外业文档模板详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:doc:query')")
    @GetMapping(value = "/{docId}")
    public AjaxResult getInfo(@PathVariable("docId") Long docId)
    {
        return success(busiFieldDocService.selectBusiFieldDocByDocId(docId));
    }

    /**
     * 新增外业文档模板
     */
    @PreAuthorize("@ss.hasPermi('system:doc:add')")
    @Log(title = "外业文档模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusiFieldDoc busiFieldDoc)
    {
        return toAjax(busiFieldDocService.insertBusiFieldDoc(busiFieldDoc));
    }

    /**
     * 修改外业文档模板
     */
    @PreAuthorize("@ss.hasPermi('system:doc:edit')")
    @Log(title = "外业文档模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusiFieldDoc busiFieldDoc)
    {
        return toAjax(busiFieldDocService.updateBusiFieldDoc(busiFieldDoc));
    }

    /**
     * 删除外业文档模板
     */
    @PreAuthorize("@ss.hasPermi('system:doc:remove')")
    @Log(title = "外业文档模板", businessType = BusinessType.DELETE)
	@DeleteMapping("/{docIds}")
    public AjaxResult remove(@PathVariable Long[] docIds)
    {
        return toAjax(busiFieldDocService.deleteBusiFieldDocByDocIds(docIds));
    }
}
