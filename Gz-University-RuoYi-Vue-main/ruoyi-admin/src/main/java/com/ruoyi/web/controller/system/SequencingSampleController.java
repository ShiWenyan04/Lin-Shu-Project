package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SequencingSample;
import com.ruoyi.system.service.ISequencingSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 测序信息Controller
 *
 * @author ruoyi
 * @date 2025-07-20
 */
@RestController
@RequestMapping("/system/sample")
public class SequencingSampleController extends BaseController
{
    @Autowired
    private ISequencingSampleService sequencingSampleService;

    /**
     * 查询测序信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:sample:list')")
    @GetMapping("/list")
    public TableDataInfo list(SequencingSample sequencingSample)
    {
        startPage();
        List<SequencingSample> list = sequencingSampleService.selectSequencingSampleList(sequencingSample);
        return getDataTable(list);
    }

    /**
     * 导出测序信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:sample:export')")
    @Log(title = "测序信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SequencingSample sequencingSample)
    {
        List<SequencingSample> list = sequencingSampleService.selectSequencingSampleList(sequencingSample);
        ExcelUtil<SequencingSample> util = new ExcelUtil<SequencingSample>(SequencingSample.class);
        util.exportExcel(response, list, "测序信息数据");
    }

    /**
     * 获取测序信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:sample:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sequencingSampleService.selectSequencingSampleById(id));
    }

    /**
     * 新增测序信息
     */
    @PreAuthorize("@ss.hasPermi('system:sample:add')")
    @Log(title = "测序信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SequencingSample sequencingSample)
    {
        return toAjax(sequencingSampleService.insertSequencingSample(sequencingSample));
    }

    /**
     * 修改测序信息
     */
    @PreAuthorize("@ss.hasPermi('system:sample:edit')")
    @Log(title = "测序信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SequencingSample sequencingSample)
    {
        return toAjax(sequencingSampleService.updateSequencingSample(sequencingSample));
    }

    /**
     * 删除测序信息
     */
    @PreAuthorize("@ss.hasPermi('system:sample:remove')")
    @Log(title = "测序信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sequencingSampleService.deleteSequencingSampleByIds(ids));
    }

    /**
     * 导入测序信息
     */
    @PreAuthorize("@ss.hasPermi('system:sample:import')")
    @Log(title = "测序信息", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<SequencingSample> util = new ExcelUtil<>(SequencingSample.class);
        List<SequencingSample> sampleList = util.importExcel(file.getInputStream());
        String message = sequencingSampleService.importSequencingSample(sampleList, updateSupport);
        return success(message);
    }

    /**
     * 下载导入模板
     */
    @PreAuthorize("@ss.hasPermi('system:sample:import')")
    @GetMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<SequencingSample> util = new ExcelUtil<>(SequencingSample.class);
        util.importTemplateExcel(response, "测序样本数据");
    }
}