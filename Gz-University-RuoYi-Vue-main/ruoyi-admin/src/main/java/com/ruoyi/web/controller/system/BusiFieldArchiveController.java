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
import com.ruoyi.system.domain.BusiFieldArchive;
import com.ruoyi.system.service.IBusiFieldArchiveService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;

@RestController
@RequestMapping("/system/archive")
public class BusiFieldArchiveController extends BaseController
{
    @Autowired
    private IBusiFieldArchiveService busiFieldArchiveService;

    /**
     * 查询外业数据归档列表
     */
    @PreAuthorize("@ss.hasPermi('system:archive:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusiFieldArchive busiFieldArchive, @RequestParam(required = false) String tabType)
    {
        // 传递给 Service 的 tabType
        String serviceTabType = tabType;

        // 【新增】处理 AUDIT 模式的权限
        if ("AUDIT".equals(tabType)) {
            // 1. 权限校验：老师、归档负责人、管理员
            if (!SecurityUtils.hasRole("teacher")
                    && !SecurityUtils.hasRole("fieldAchive_manager")
                    && !SecurityUtils.hasRole("admin")) {
                return getDataTable(new java.util.ArrayList<>());
            }

            // 2. 过滤逻辑：
            // 如果是 fieldAchive_manager 或 admin，允许看所有状态（不设置 auditStatus）
            // 如果仅是 teacher，只看待审核 '0'
            if (!SecurityUtils.hasRole("fieldAchive_manager") && !SecurityUtils.hasRole("admin")) {
                busiFieldArchive.setAuditStatus("0");
            }

            // 3. 将传递给 Service 的 tabType 置为 null，跳过 Service 内部的强制过滤
            serviceTabType = null;
        }

        startPage();
        Long userId = SecurityUtils.getUserId();
        // 注意这里传的是 serviceTabType
        List<BusiFieldArchive> list = busiFieldArchiveService.selectBusiFieldArchiveList(busiFieldArchive, serviceTabType, userId);
        return getDataTable(list);
    }

    // 🚀 获取可归档任务列表 (下拉框)
    @GetMapping("/my-finished-starts")
    public AjaxResult getMyFinishedStarts()
    {
        return success(busiFieldArchiveService.selectMyFinishedStarts(SecurityUtils.getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('system:archive:audit')")
    @Log(title = "外业归档审核", businessType = BusinessType.UPDATE)
    @PostMapping("/audit")
    public AjaxResult audit(@RequestBody BusiFieldArchive body)
    {
        return toAjax(busiFieldArchiveService.auditBusiFieldArchive(body.getArchiveId(), body.getAuditStatus(), body.getAuditReason()));
    }

    @PreAuthorize("@ss.hasPermi('system:archive:export')")
    @Log(title = "外业数据归档", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusiFieldArchive busiFieldArchive)
    {
        List<BusiFieldArchive> list = busiFieldArchiveService.selectBusiFieldArchiveList(busiFieldArchive);
        ExcelUtil<BusiFieldArchive> util = new ExcelUtil<BusiFieldArchive>(BusiFieldArchive.class);
        util.exportExcel(response, list, "外业数据归档数据");
    }

    @PreAuthorize("@ss.hasPermi('system:archive:query')")
    @GetMapping(value = "/{archiveId}")
    public AjaxResult getInfo(@PathVariable("archiveId") Long archiveId)
    {
        return success(busiFieldArchiveService.selectBusiFieldArchiveByArchiveId(archiveId));
    }

    @PreAuthorize("@ss.hasPermi('system:archive:add')")
    @Log(title = "外业数据归档", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusiFieldArchive busiFieldArchive)
    {
        // 🚀 必须设置 createBy
        busiFieldArchive.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        return toAjax(busiFieldArchiveService.insertBusiFieldArchive(busiFieldArchive));
    }

    @PreAuthorize("@ss.hasPermi('system:archive:edit')")
    @Log(title = "外业数据归档", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusiFieldArchive busiFieldArchive)
    {
        return toAjax(busiFieldArchiveService.updateBusiFieldArchive(busiFieldArchive));
    }

    @PreAuthorize("@ss.hasPermi('system:archive:remove')")
    @Log(title = "外业数据归档", businessType = BusinessType.DELETE)
    @DeleteMapping("/{archiveIds}")
    public AjaxResult remove(@PathVariable Long[] archiveIds)
    {
        return toAjax(busiFieldArchiveService.deleteBusiFieldArchiveByArchiveIds(archiveIds));
    }
}