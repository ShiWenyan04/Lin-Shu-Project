package com.ruoyi.web.controller.system;

import java.util.ArrayList; // 【新增】
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysRole; // 【新增】
import com.ruoyi.common.core.domain.entity.SysUser; // 【新增】
import com.ruoyi.common.utils.SecurityUtils; // 【新增】
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
import com.ruoyi.system.domain.FieldworkBorrowing;
import com.ruoyi.system.service.IFieldworkBorrowingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 外业调查设备借用Controller
 * * @author ruoyi
 * @date 2025-12-18
 */
@RestController
@RequestMapping("/system/borrowing")
public class FieldworkBorrowingController extends BaseController
{
    @Autowired
    private IFieldworkBorrowingService fieldworkBorrowingService;

    /**
     * 查询外业调查设备借用列表
     */
    @PreAuthorize("@ss.hasPermi('system:borrowing:list')")
    @GetMapping("/list")
    public TableDataInfo list(FieldworkBorrowing fieldworkBorrowing)
    {
        if (fieldworkBorrowing.getParams() != null) {
            String queryMode = (String) fieldworkBorrowing.getParams().get("queryMode");

            // 2. 如果是“我的提交”模式
            if ("private".equals(queryMode)) {
                fieldworkBorrowing.setCreateBy(getUsername());
            }
            // 3. 【新增】审核管理模式
            else if ("audit".equals(queryMode)) {
                SysUser user = SecurityUtils.getLoginUser().getUser();
                boolean hasAuth = false;
                // 校验权限：管理员、老师、借用负责人(boeeowing_manager)
                if (SysUser.isAdmin(user.getUserId())) {
                    hasAuth = true;
                } else {
                    for (SysRole role : user.getRoles()) {
                        if ("teacher".equals(role.getRoleKey()) || "boeeowing_manager".equals(role.getRoleKey())) {
                            hasAuth = true; break;
                        }
                    }
                }
                if (!hasAuth) return getDataTable(new ArrayList<>());

                // 过滤逻辑：
                // 如果是 boeeowing_manager 或 admin，允许看所有状态（不设置 auditStatus）
                // 如果仅是 teacher，只看待审核 '0'
                if (!SecurityUtils.hasRole("boeeowing_manager") && !SecurityUtils.hasRole("admin")) {
                    fieldworkBorrowing.setAuditStatus("0");
                }

                // 清空 createBy 以便查看所有人
                fieldworkBorrowing.setCreateBy(null);
            }
        }
        startPage();
        List<FieldworkBorrowing> list = fieldworkBorrowingService.selectFieldworkBorrowingList(fieldworkBorrowing);
        return getDataTable(list);
    }

    /**
     * 导出外业调查设备借用列表
     */
    @PreAuthorize("@ss.hasPermi('system:borrowing:export')")
    @Log(title = "外业调查设备借用", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FieldworkBorrowing fieldworkBorrowing)
    {
        // 导出权限控制：普通学生只能导出自己的
        if (SecurityUtils.hasRole("student")
                && !SecurityUtils.hasRole("teacher")
                && !SecurityUtils.hasRole("boeeowing_manager")
                && !SecurityUtils.hasRole("admin")) {
            fieldworkBorrowing.setCreateBy(getUsername());
        }

        List<FieldworkBorrowing> list = fieldworkBorrowingService.selectFieldworkBorrowingList(fieldworkBorrowing);
        ExcelUtil<FieldworkBorrowing> util = new ExcelUtil<FieldworkBorrowing>(FieldworkBorrowing.class);
        util.exportExcel(response, list, "外业调查设备借用数据");
    }

    /**
     * 获取外业调查设备借用详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:borrowing:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(fieldworkBorrowingService.selectFieldworkBorrowingById(id));
    }

    /**
     * 新增外业调查设备借用
     */
    @PreAuthorize("@ss.hasPermi('system:borrowing:add')")
    @Log(title = "外业调查设备借用", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FieldworkBorrowing fieldworkBorrowing)
    {
        fieldworkBorrowing.setCreateBy(getUsername());
        return toAjax(fieldworkBorrowingService.insertFieldworkBorrowing(fieldworkBorrowing));
    }

    /**
     * 修改外业调查设备借用
     */
    @PreAuthorize("@ss.hasPermi('system:borrowing:edit')")
    @Log(title = "外业调查设备借用", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FieldworkBorrowing fieldworkBorrowing)
    {
        fieldworkBorrowing.setUpdateBy(getUsername());

        // 【新增】权限控制状态重置
        // 只有非（老师/借用负责人/管理员）修改时，才重置为 '0'
        if (!SecurityUtils.hasRole("teacher") && !SecurityUtils.hasRole("boeeowing_manager") && !SecurityUtils.hasRole("admin")) {
            fieldworkBorrowing.setAuditStatus("0");
        }

        return toAjax(fieldworkBorrowingService.updateFieldworkBorrowing(fieldworkBorrowing));
    }

    /**
     * 删除外业调查设备借用
     */
    @PreAuthorize("@ss.hasPermi('system:borrowing:remove')")
    @Log(title = "外业调查设备借用", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fieldworkBorrowingService.deleteFieldworkBorrowingByIds(ids));
    }

    /**
     * 审核接口 (新增)
     */
    @PreAuthorize("@ss.hasPermi('system:borrowing:audit')")
    @Log(title = "外业调查设备借用审核", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody FieldworkBorrowing fieldworkBorrowing)
    {
        return toAjax(fieldworkBorrowingService.auditFieldworkBorrowing(fieldworkBorrowing));
    }
}