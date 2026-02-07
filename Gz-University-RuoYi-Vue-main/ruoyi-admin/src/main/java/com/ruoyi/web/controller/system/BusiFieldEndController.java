package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.BusiFieldEnd;
import com.ruoyi.system.service.IBusiFieldEndService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 外业行程结束Controller
 * 
 * @author ruoyi
 * @date 2025-12-22
 */
@RestController
@RequestMapping("/system/end")
public class BusiFieldEndController extends BaseController
{
    @Autowired
    private IBusiFieldEndService busiFieldEndService;

    /**
     * 查询外业行程结束列表
     */
    @PreAuthorize("@ss.hasPermi('system:end:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusiFieldEnd busiFieldEnd, @RequestParam(required = false) String tabType)
    {
        // 传递给 Service 的 tabType
        String serviceTabType = tabType;

        // 【新增】处理 AUDIT 模式的权限
        if ("AUDIT".equals(tabType)) {
            // 1. 权限校验：老师、结束负责人、管理员
            if (!SecurityUtils.hasRole("teacher")
                    && !SecurityUtils.hasRole("fieldend_manager")
                    && !SecurityUtils.hasRole("admin")) {
                return getDataTable(new java.util.ArrayList<>());
            }

            // 2. 过滤逻辑：
            // 如果是 fieldend_manager 或 admin，允许看所有状态（不设置 auditStatus）
            // 如果仅是 teacher，只看待审核 '0'
            if (!SecurityUtils.hasRole("fieldend_manager") && !SecurityUtils.hasRole("admin")) {
                busiFieldEnd.setAuditStatus("0");
            }

            // 3. 将传递给 Service 的 tabType 置为 null，跳过 Service 内部的强制过滤
            serviceTabType = null;
        }

        startPage();
        Long userId = SecurityUtils.getUserId();
        // 注意这里传的是 serviceTabType
        List<BusiFieldEnd> list = busiFieldEndService.selectBusiFieldEndList(busiFieldEnd, serviceTabType, userId);
        return getDataTable(list);
    }

    /**
     * 获取我可用的启动申请 (下拉框)
     */
    @GetMapping("/my-starts")
    public AjaxResult getMyStarts()
    {
        return success(busiFieldEndService.selectMyAvailableStarts(SecurityUtils.getUserId()));
    }

    /**
     * 审核
     */
    @PreAuthorize("@ss.hasPermi('system:end:audit')")
    @Log(title = "外业结束审核", businessType = BusinessType.UPDATE)
    @PostMapping("/audit")
    public AjaxResult audit(@RequestBody BusiFieldEnd body)
    {
        return toAjax(busiFieldEndService.auditBusiFieldEnd(body.getEndId(), body.getAuditStatus(), body.getAuditReason()));
    }

    /**
     * 导出外业行程结束列表
     */
    @PreAuthorize("@ss.hasPermi('system:end:export')")
    @Log(title = "外业行程结束", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusiFieldEnd busiFieldEnd)
    {
        List<BusiFieldEnd> list = busiFieldEndService.selectBusiFieldEndList(busiFieldEnd);
        ExcelUtil<BusiFieldEnd> util = new ExcelUtil<BusiFieldEnd>(BusiFieldEnd.class);
        util.exportExcel(response, list, "外业行程结束数据");
    }

    /**
     * 获取外业行程结束详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:end:query')")
    @GetMapping(value = "/{endId}")
    public AjaxResult getInfo(@PathVariable("endId") Long endId)
    {
        return success(busiFieldEndService.selectBusiFieldEndByEndId(endId));
    }

    /**
     * 新增外业行程结束
     */
    @PreAuthorize("@ss.hasPermi('system:end:add')")
    @Log(title = "外业行程结束", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusiFieldEnd busiFieldEnd)
    {
        busiFieldEnd.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        return toAjax(busiFieldEndService.insertBusiFieldEnd(busiFieldEnd));
    }

    /**
     * 修改外业行程结束
     */
    @PreAuthorize("@ss.hasPermi('system:end:edit')")
    @Log(title = "外业行程结束", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusiFieldEnd busiFieldEnd)
    {
        return toAjax(busiFieldEndService.updateBusiFieldEnd(busiFieldEnd));
    }

    /**
     * 删除外业行程结束
     */
    @PreAuthorize("@ss.hasPermi('system:end:remove')")
    @Log(title = "外业行程结束", businessType = BusinessType.DELETE)
	@DeleteMapping("/{endIds}")
    public AjaxResult remove(@PathVariable Long[] endIds)
    {
        return toAjax(busiFieldEndService.deleteBusiFieldEndByEndIds(endIds));
    }
}
