package com.ruoyi.web.controller.system;

import java.util.ArrayList; // 【新增】
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysRole; // 【新增】
import com.ruoyi.common.core.domain.entity.SysUser; // 【新增】
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.BusiFieldStart;
import com.ruoyi.system.service.IBusiFieldStartService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;

/**
 * 外业启动申请Controller
 */
@RestController
@RequestMapping("/system/start")
public class BusiFieldStartController extends BaseController
{
    @Autowired
    private IBusiFieldStartService busiFieldStartService;

    /**
     * 查询外业启动申请列表
     */
    @PreAuthorize("@ss.hasPermi('system:start:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusiFieldStart busiFieldStart,
                              @RequestParam(name = "tabType", required = false) String tabType)
    {
        Long userId = SecurityUtils.getUserId();

        // 传递给 Service 的 tabType，默认保持原值
        String serviceTabType = tabType;

        // 【新增】处理 AUDIT 模式的权限和过滤
        if ("AUDIT".equals(tabType)) {
            // 1. 权限校验
            if (!SecurityUtils.hasRole("teacher")
                    && !SecurityUtils.hasRole("fildstart_manager")
                    && !SecurityUtils.hasRole("admin")) {
                return getDataTable(new ArrayList<>());
            }

            // 2. 过滤逻辑：
            // 如果是 fildstart_manager 或 admin，允许看所有状态（不设置 auditStatus）
            // 如果仅是 teacher，只看待审核 '0'
            if (!SecurityUtils.hasRole("fildstart_manager") && !SecurityUtils.hasRole("admin")) {
                busiFieldStart.setAuditStatus("0");
            }

            // 3. 关键：将传递给 Service 的 tabType 置为 null
            // 这样 Service 就不会执行它内部那句 "if AUDIT then setStatus('0')"，
            // 而是直接使用我们在上面设置好的 busiFieldStart 对象进行查询。
            serviceTabType = null;
        }

        startPage();
        List<BusiFieldStart> list = busiFieldStartService.selectBusiFieldStartList(busiFieldStart, serviceTabType, userId);
        return getDataTable(list);
    }

    /**
     * 【新增】审核接口
     */
    @PreAuthorize("@ss.hasPermi('system:start:audit')")
    @Log(title = "外业审核", businessType = BusinessType.UPDATE)
    @PostMapping("/audit")
    public AjaxResult audit(@RequestBody BusiFieldStart body)
    {
        if (body.getStartId() == null || body.getAuditStatus() == null) {
            return error("参数缺失");
        }
        return toAjax(busiFieldStartService.auditBusiFieldStart(body.getStartId(), body.getAuditStatus(), body.getAuditReason()));
    }

    /**
     * 导出 (兼容修改：导出时默认不传TabType，或者根据需求传)
     * 这里我们传 null，让Service当做普通查询处理，或者你可以指定 'ALL'
     */
    @PreAuthorize("@ss.hasPermi('system:start:export')")
    @Log(title = "外业启动申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusiFieldStart busiFieldStart)
    {
        // 导出时，通常是管理员操作，或者导出当前筛选条件的
        // 这里传 null，Service 就不会强制加 audit_status 的限制，而是遵循 busiFieldStart 对象里的搜索条件
        List<BusiFieldStart> list = busiFieldStartService.selectBusiFieldStartList(busiFieldStart, null, SecurityUtils.getUserId());
        ExcelUtil<BusiFieldStart> util = new ExcelUtil<BusiFieldStart>(BusiFieldStart.class);
        util.exportExcel(response, list, "外业启动申请数据");
    }

    @PreAuthorize("@ss.hasPermi('system:start:query')")
    @GetMapping(value = "/{startId}")
    public AjaxResult getInfo(@PathVariable("startId") Long startId)
    {
        return success(busiFieldStartService.selectBusiFieldStartByStartId(startId));
    }

    @PreAuthorize("@ss.hasPermi('system:start:add')")
    @Log(title = "外业启动申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusiFieldStart busiFieldStart)
    {
        // 自动设置 createBy 为当前用户ID
        busiFieldStart.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        return toAjax(busiFieldStartService.insertBusiFieldStart(busiFieldStart));
    }

    @PreAuthorize("@ss.hasPermi('system:start:edit')")
    @Log(title = "外业启动申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusiFieldStart busiFieldStart)
    {
        busiFieldStart.setUpdateBy(getUsername());
        return toAjax(busiFieldStartService.updateBusiFieldStart(busiFieldStart));
    }

    @PreAuthorize("@ss.hasPermi('system:start:remove')")
    @Log(title = "外业启动申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{startIds}")
    public AjaxResult remove(@PathVariable Long[] startIds)
    {
        return toAjax(busiFieldStartService.deleteBusiFieldStartByStartIds(startIds));
    }

}