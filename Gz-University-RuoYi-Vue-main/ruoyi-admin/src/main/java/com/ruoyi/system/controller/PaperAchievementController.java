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
import com.ruoyi.system.domain.PaperAchievement;
import com.ruoyi.system.service.IPaperAchievementService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;

/**
 * 论文成果管理Controller
 * * @author ruoyi
 * @date 2025-12-20
 */
@RestController
@RequestMapping("/system/paperAchievement")
public class PaperAchievementController extends BaseController
{
    @Autowired
    private IPaperAchievementService paperAchievementService;

    /**
     * 查询论文成果管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:paperAchievement:list')")
    @GetMapping("/list")
    public TableDataInfo list(PaperAchievement paperAchievement)
    {
        // 获取前端传来的 queryMode
        String queryMode = null;
        if (paperAchievement.getParams() != null) {
            queryMode = (String) paperAchievement.getParams().get("queryMode");
        }

        // 【修正后的逻辑】
        // 1. 如果是“我的提交”模式 (private)，无论是谁，都强制只能看自己的
        if ("private".equals(queryMode)) {
            paperAchievement.setCreateBy(SecurityUtils.getUsername());
        }
        // 2. 权限过滤：如果是学生，且没有“老师”或“论文成果负责人”或“管理员”身份，且不是看Public，则只能看自己的
        // 修改点：加入了 !SecurityUtils.hasRole("papeAachievement_manager")
        else if (SecurityUtils.hasRole("student")
                && !SecurityUtils.hasRole("teacher")
                && !SecurityUtils.hasRole("papeAachievement_manager")
                && !SecurityUtils.hasRole("admin")
                && !"public".equals(queryMode)) {
            paperAchievement.setCreateBy(SecurityUtils.getUsername());
        }

        startPage();
        List<PaperAchievement> list = paperAchievementService.selectPaperAchievementList(paperAchievement);
        return getDataTable(list);
    }

    /**
     * 导出论文成果管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:paperAchievement:export')")
    @Log(title = "论文成果管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PaperAchievement paperAchievement)
    {
        // 权限控制：如果是学生，只能导出自己创建的
        if (SecurityUtils.hasRole("student")) {
            paperAchievement.setCreateBy(SecurityUtils.getUsername());
        }

        List<PaperAchievement> list = paperAchievementService.selectPaperAchievementList(paperAchievement);
        ExcelUtil<PaperAchievement> util = new ExcelUtil<PaperAchievement>(PaperAchievement.class);
        util.exportExcel(response, list, "论文成果管理数据");
    }

    /**
     * 获取论文成果管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:paperAchievement:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(paperAchievementService.selectPaperAchievementById(id));
    }

    /**
     * 新增论文成果管理
     */
    @PreAuthorize("@ss.hasPermi('system:paperAchievement:add')")
    @Log(title = "论文成果管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PaperAchievement paperAchievement)
    {
        return toAjax(paperAchievementService.insertPaperAchievement(paperAchievement));
    }

    /**
     * 修改论文成果管理
     */
    @PreAuthorize("@ss.hasPermi('system:paperAchievement:edit')")
    @Log(title = "论文成果管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PaperAchievement paperAchievement)
    {
        return toAjax(paperAchievementService.updatePaperAchievement(paperAchievement));
    }

    /**
     * 审核论文成果管理 (新增接口)
     */
    @PreAuthorize("@ss.hasPermi('system:paperAchievement:audit')")
    @Log(title = "论文成果管理审核", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody PaperAchievement paperAchievement)
    {
        if (paperAchievement.getId() == null) {
            return error("ID不能为空");
        }
        if (StringUtils.isEmpty(paperAchievement.getAuditStatus())) {
            return error("审核状态不能为空");
        }
        return toAjax(paperAchievementService.auditPaperAchievement(paperAchievement));
    }

    /**
     * 删除论文成果管理
     */
    @PreAuthorize("@ss.hasPermi('system:paperAchievement:remove')")
    @Log(title = "论文成果管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(paperAchievementService.deletePaperAchievementByIds(ids));
    }
}