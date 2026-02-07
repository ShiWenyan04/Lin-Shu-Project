package com.ruoyi.web.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
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
import com.ruoyi.system.domain.ExperimentalEquipment;
import com.ruoyi.system.service.IExperimentalEquipmentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.model.LoginUser;

/**
 * 实验设备信息Controller
 * * @author ruoyi
 * @date 2025-12-16
 */
@RestController
@RequestMapping("/system/equipment")
public class ExperimentalEquipmentController extends BaseController
{
    @Autowired
    private IExperimentalEquipmentService experimentalEquipmentService;

    @PreAuthorize("@ss.hasPermi('system:equipment:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExperimentalEquipment experimentalEquipment)
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();

        if (experimentalEquipment.getParams() == null) {
            experimentalEquipment.setParams(new HashMap<>());
        }
        String queryMode = (String) experimentalEquipment.getParams().get("queryMode");

        if (StringUtils.isEmpty(queryMode)) {
            queryMode = "public";
        }

        // --- 核心过滤逻辑 ---
        if ("public".equals(queryMode)) {
            // 公共池：只看 audit_status = 1
            experimentalEquipment.setAuditStatus("1");
            experimentalEquipment.setCreateBy(null);
        }
        else if ("private".equals(queryMode)) {
            // 我的提交：只看 create_by = 我
            experimentalEquipment.setCreateBy(user.getUserName());
            experimentalEquipment.setAuditStatus(null);
        }
        else if ("audit".equals(queryMode)) {
            // 审核/管理模式：校验权限
            boolean hasAuth = false;
            // 权限校验：管理员、老师、实验设备负责人
            if (SysUser.isAdmin(user.getUserId())) {
                hasAuth = true;
            } else {
                for (SysRole role : user.getRoles()) {
                    if ("teacher".equals(role.getRoleKey()) || "experimental_manager".equals(role.getRoleKey())) {
                        hasAuth = true; break;
                    }
                }
            }
            if (!hasAuth) return getDataTable(new ArrayList<>());

            // 数据过滤逻辑：
            // 1. 如果是实验设备负责人(experimental_manager) 或 管理员(admin)，可以看到所有状态的数据（用于维护）
            // 2. 如果只是老师(teacher)，通常只关心待审核(0)的数据
            if (!SecurityUtils.hasRole("experimental_manager") && !SecurityUtils.hasRole("admin")) {
                experimentalEquipment.setAuditStatus("0");
            }

            experimentalEquipment.setCreateBy(null);
        }

        startPage();
        List<ExperimentalEquipment> list = experimentalEquipmentService.selectExperimentalEquipmentList(experimentalEquipment);
        return getDataTable(list);
    }

    /**
     * 导出实验设备信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:equipment:export')")
    @Log(title = "实验设备信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExperimentalEquipment experimentalEquipment)
    {
        // 导出权限控制：学生只能导出自己的
        if (SecurityUtils.hasRole("student")
                && !SecurityUtils.hasRole("teacher")
                && !SecurityUtils.hasRole("experimental_manager")
                && !SecurityUtils.hasRole("admin")) {
            experimentalEquipment.setCreateBy(SecurityUtils.getUsername());
        }

        List<ExperimentalEquipment> list = experimentalEquipmentService.selectExperimentalEquipmentList(experimentalEquipment);
        ExcelUtil<ExperimentalEquipment> util = new ExcelUtil<ExperimentalEquipment>(ExperimentalEquipment.class);
        util.exportExcel(response, list, "实验设备信息数据");
    }

    /**
     * 获取实验设备信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:equipment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(experimentalEquipmentService.selectExperimentalEquipmentById(id));
    }

    /**
     * 新增实验设备信息
     */
    @PreAuthorize("@ss.hasPermi('system:equipment:add')")
    @Log(title = "实验设备信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExperimentalEquipment experimentalEquipment)
    {
        experimentalEquipment.setCreateBy(SecurityUtils.getUsername());
        return toAjax(experimentalEquipmentService.insertExperimentalEquipment(experimentalEquipment));
    }

    /**
     * 修改实验设备信息
     */
    @PreAuthorize("@ss.hasPermi('system:equipment:edit')")
    @Log(title = "实验设备信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExperimentalEquipment experimentalEquipment)
    {
        experimentalEquipment.setUpdateBy(SecurityUtils.getUsername());

        // 核心修改：如果是 实验设备负责人、老师、管理员 修改，不重置审核状态
        // 其他人（如学生）修改，重置为“待审核”
        if (!SecurityUtils.hasRole("experimental_manager") && !SecurityUtils.hasRole("teacher") && !SecurityUtils.hasRole("admin")) {
            experimentalEquipment.setAuditStatus("0");
        }

        return toAjax(experimentalEquipmentService.updateExperimentalEquipment(experimentalEquipment));
    }

    /**
     * 删除实验设备信息
     */
    @PreAuthorize("@ss.hasPermi('system:equipment:remove')")
    @Log(title = "实验设备信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(experimentalEquipmentService.deleteExperimentalEquipmentByIds(ids));
    }

    /**
     * 审核实验设备
     */
    @PreAuthorize("@ss.hasPermi('system:equipment:audit')")
    @Log(title = "实验设备审核", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody ExperimentalEquipment experimentalEquipment)
    {
        if (experimentalEquipment.getId() == null || experimentalEquipment.getAuditStatus() == null) {
            return AjaxResult.error("参数错误");
        }
        experimentalEquipment.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(experimentalEquipmentService.updateExperimentalEquipment(experimentalEquipment));
    }
}