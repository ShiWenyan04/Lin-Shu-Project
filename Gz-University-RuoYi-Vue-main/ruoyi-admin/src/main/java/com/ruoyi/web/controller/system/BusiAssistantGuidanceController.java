package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.BusiAssistantGuidance;
import com.ruoyi.system.service.IBusiAssistantGuidanceService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;

/**
 * 本科论文指导Controller
 * @author ruoyi
 * @date 2026-01-08
 */
@RestController
@RequestMapping("/system/guidance")
public class BusiAssistantGuidanceController extends BaseController
{
    @Autowired
    private IBusiAssistantGuidanceService busiAssistantGuidanceService;

    /**
     * 查询本科论文指导列表 (带 Tab 过滤)
     */
    @PreAuthorize("@ss.hasPermi('system:guidance:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusiAssistantGuidance busiAssistantGuidance, @RequestParam(required = false) String tabType)
    {
        startPage();
        Long userId = SecurityUtils.getUserId();
        // 关键：调用带 tabType 的 Service 方法
        List<BusiAssistantGuidance> list = busiAssistantGuidanceService.selectBusiAssistantGuidanceList(busiAssistantGuidance, tabType, userId);
        return getDataTable(list);
    }

    /**
     * 审核接口
     */
    @PreAuthorize("@ss.hasPermi('system:guidance:audit')")
    @Log(title = "本科论文指导审核", businessType = BusinessType.UPDATE)
    @PostMapping("/audit")
    public AjaxResult audit(@RequestBody BusiAssistantGuidance body)
    {
        return toAjax(busiAssistantGuidanceService.auditBusiAssistantGuidance(body.getId(), body.getAuditStatus(), body.getAuditReason()));
    }

    /**
     * 导出本科论文指导列表
     */
    @PreAuthorize("@ss.hasPermi('system:guidance:export')")
    @Log(title = "本科论文指导", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusiAssistantGuidance busiAssistantGuidance)
    {
        List<BusiAssistantGuidance> list = busiAssistantGuidanceService.selectBusiAssistantGuidanceList(busiAssistantGuidance);
        ExcelUtil<BusiAssistantGuidance> util = new ExcelUtil<BusiAssistantGuidance>(BusiAssistantGuidance.class);
        util.exportExcel(response, list, "本科论文指导数据");
    }

    /**
     * 获取本科论文指导详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:guidance:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(busiAssistantGuidanceService.selectBusiAssistantGuidanceById(id));
    }

    /**
     * 新增本科论文指导
     */
    @PreAuthorize("@ss.hasPermi('system:guidance:add')")
    @Log(title = "本科论文指导", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusiAssistantGuidance busiAssistantGuidance)
    {
        // 强制设置创建人
        busiAssistantGuidance.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        return toAjax(busiAssistantGuidanceService.insertBusiAssistantGuidance(busiAssistantGuidance));
    }

    /**
     * 修改本科论文指导
     */
    @PreAuthorize("@ss.hasPermi('system:guidance:edit')")
    @Log(title = "本科论文指导", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusiAssistantGuidance busiAssistantGuidance)
    {
        return toAjax(busiAssistantGuidanceService.updateBusiAssistantGuidance(busiAssistantGuidance));
    }

    /**
     * 删除本科论文指导
     */
    @PreAuthorize("@ss.hasPermi('system:guidance:remove')")
    @Log(title = "本科论文指导", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(busiAssistantGuidanceService.deleteBusiAssistantGuidanceByIds(ids));
    }
}