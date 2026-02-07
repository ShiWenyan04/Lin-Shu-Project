package com.ruoyi.system.controller;

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
import com.ruoyi.system.domain.OtherAchievement;
import com.ruoyi.system.service.IOtherAchievementService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;

/**
 * 其他成果管理Controller
 * * @author ruoyi
 * @date 2025-12-18
 */
@RestController
@RequestMapping("/system/otherAchievement")
public class OtherAchievementController extends BaseController
{
    @Autowired
    private IOtherAchievementService otherAchievementService;

    /**
     * 查询其他成果管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:otherAchievement:list')")
    @GetMapping("/list")
    public TableDataInfo list(OtherAchievement otherAchievement)
    {
        String queryMode = null;
        if (otherAchievement.getParams() != null) {
            queryMode = (String) otherAchievement.getParams().get("queryMode");
        }

        // 1. "我的提交"模式：强制只看自己
        if ("private".equals(queryMode)) {
            otherAchievement.setCreateBy(SecurityUtils.getUsername());
        }
        // 2. 权限过滤：
        // 如果是学生，且 不是老师、不是本模块负责人、不是管理员，且不是看Public
        // 则强制只能看自己的
        else if (SecurityUtils.hasRole("student")
                && !SecurityUtils.hasRole("teacher")
                && !SecurityUtils.hasRole("otherAchievement_manager")
                && !SecurityUtils.hasRole("admin")
                && !"public".equals(queryMode)) {
            otherAchievement.setCreateBy(SecurityUtils.getUsername());
        }

        startPage();
        List<OtherAchievement> list = otherAchievementService.selectOtherAchievementList(otherAchievement);
        return getDataTable(list);
    }

    /**
     * 导出其他成果管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:otherAchievement:export')")
    @Log(title = "其他成果管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OtherAchievement otherAchievement)
    {
        if (SecurityUtils.hasRole("student")) {
            otherAchievement.setCreateBy(SecurityUtils.getUsername());
        }

        List<OtherAchievement> list = otherAchievementService.selectOtherAchievementList(otherAchievement);
        ExcelUtil<OtherAchievement> util = new ExcelUtil<OtherAchievement>(OtherAchievement.class);
        util.exportExcel(response, list, "其他成果管理数据");
    }

    /**
     * 获取其他成果管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:otherAchievement:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(otherAchievementService.selectOtherAchievementById(id));
    }

    /**
     * 新增其他成果管理
     */
    @PreAuthorize("@ss.hasPermi('system:otherAchievement:add')")
    @Log(title = "其他成果管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OtherAchievement otherAchievement)
    {
        return toAjax(otherAchievementService.insertOtherAchievement(otherAchievement));
    }

    /**
     * 修改其他成果管理
     */
    @PreAuthorize("@ss.hasPermi('system:otherAchievement:edit')")
    @Log(title = "其他成果管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OtherAchievement otherAchievement)
    {
        return toAjax(otherAchievementService.updateOtherAchievement(otherAchievement));
    }

    /**
     * 审核其他成果 (新增接口)
     */
    @PreAuthorize("@ss.hasPermi('system:otherAchievement:audit')")
    @Log(title = "其他成果审核", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody OtherAchievement otherAchievement)
    {
        if (otherAchievement.getId() == null) {
            return error("ID不能为空");
        }
        if (StringUtils.isEmpty(otherAchievement.getAuditStatus())) {
            return error("审核状态不能为空");
        }
        return toAjax(otherAchievementService.auditOtherAchievement(otherAchievement));
    }

    /**
     * 删除其他成果管理
     */
    @PreAuthorize("@ss.hasPermi('system:otherAchievement:remove')")
    @Log(title = "其他成果管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(otherAchievementService.deleteOtherAchievementByIds(ids));
    }
}