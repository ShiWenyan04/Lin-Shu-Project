package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SampleUsageRecord;
import com.ruoyi.system.service.ISampleUsageRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 样本使用记录Controller
 *
 * @author ruoyi
 * @date 2025-07-19
 */
@RestController
@RequestMapping("/system/record")
public class SampleUsageRecordController extends BaseController
{
    @Autowired
    private ISampleUsageRecordService sampleUsageRecordService;

    /**
     * 查询样本使用记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(SampleUsageRecord sampleUsageRecord)
    {
        startPage();
        List<SampleUsageRecord> list = sampleUsageRecordService.selectSampleUsageRecordList(sampleUsageRecord);
        return getDataTable(list);
    }

    /**
     * 导出样本使用记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:export')")
    @Log(title = "样本使用记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SampleUsageRecord sampleUsageRecord)
    {
        List<SampleUsageRecord> list = sampleUsageRecordService.selectSampleUsageRecordList(sampleUsageRecord);
        ExcelUtil<SampleUsageRecord> util = new ExcelUtil<SampleUsageRecord>(SampleUsageRecord.class);
        util.exportExcel(response, list, "样本使用记录数据");
    }

    /**
     * 获取样本使用记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sampleUsageRecordService.selectSampleUsageRecordById(id));
    }

    /**
     * 新增样本使用记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:add')")
    @Log(title = "样本使用记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SampleUsageRecord sampleUsageRecord)
    {
        return toAjax(sampleUsageRecordService.insertSampleUsageRecord(sampleUsageRecord));
    }

    /**
     * 修改样本使用记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:edit')")
    @Log(title = "样本使用记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SampleUsageRecord sampleUsageRecord)
    {
        return toAjax(sampleUsageRecordService.updateSampleUsageRecord(sampleUsageRecord));
    }

    /**
     * 删除样本使用记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:remove')")
    @Log(title = "样本使用记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sampleUsageRecordService.deleteSampleUsageRecordByIds(ids));
    }
}