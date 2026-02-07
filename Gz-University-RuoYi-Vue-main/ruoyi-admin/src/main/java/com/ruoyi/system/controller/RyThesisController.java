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
import com.ruoyi.system.domain.RyThesis;
import com.ruoyi.system.service.IRyThesisService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;

/**
 * 学位论文管理Controller
 * * @author ruoyi
 * @date 2025-12-13
 */
@RestController
@RequestMapping("/system/thesis")
public class RyThesisController extends BaseController
{
    @Autowired
    private IRyThesisService ryThesisService;

    /**
     * 查询学位论文管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:thesis:list')")
    @GetMapping("/list")
    public TableDataInfo list(RyThesis ryThesis)
    {
        // 1. 获取前端传来的 queryMode
        String queryMode = null;
        if (ryThesis.getParams() != null) {
            queryMode = (String) ryThesis.getParams().get("queryMode");
        }

        // 2. 权限隔离逻辑
        // 如果是 'private' (我的提交)，强制 createBy = 当前用户
        if ("private".equals(queryMode)) {
            ryThesis.setCreateBy(SecurityUtils.getUsername());
        }
        // 如果是学生，且 不是老师、不是学位论文负责人、不是管理员，且不是看 'public'
        // 则强制只能看自己的
        // 修改点：加入了 !SecurityUtils.hasRole("thesis_manager")
        else if (SecurityUtils.hasRole("student")
                && !SecurityUtils.hasRole("teacher")
                && !SecurityUtils.hasRole("thesis_manager")
                && !SecurityUtils.hasRole("admin")
                && !"public".equals(queryMode)) {
            ryThesis.setCreateBy(SecurityUtils.getUsername());
        }

        startPage();
        List<RyThesis> list = ryThesisService.selectRyThesisList(ryThesis);
        return getDataTable(list);
    }

    /**
     * 导出学位论文管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:thesis:export')")
    @Log(title = "学位论文管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RyThesis ryThesis)
    {
        // 导出时学生只能导出自己的
        if (SecurityUtils.hasRole("student")) {
            ryThesis.setCreateBy(SecurityUtils.getUsername());
        }

        List<RyThesis> list = ryThesisService.selectRyThesisList(ryThesis);
        ExcelUtil<RyThesis> util = new ExcelUtil<RyThesis>(RyThesis.class);
        util.exportExcel(response, list, "学位论文管理数据");
    }

    /**
     * 获取学位论文管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:thesis:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(ryThesisService.selectRyThesisById(id));
    }

    /**
     * 新增学位论文管理
     */
    @PreAuthorize("@ss.hasPermi('system:thesis:add')")
    @Log(title = "学位论文管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RyThesis ryThesis)
    {
        return toAjax(ryThesisService.insertRyThesis(ryThesis));
    }

    /**
     * 修改学位论文管理
     */
    @PreAuthorize("@ss.hasPermi('system:thesis:edit')")
    @Log(title = "学位论文管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RyThesis ryThesis)
    {
        return toAjax(ryThesisService.updateRyThesis(ryThesis));
    }

    /**
     * 审核学位论文管理 (新增)
     */
    @PreAuthorize("@ss.hasPermi('system:thesis:audit')")
    @Log(title = "学位论文审核", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody RyThesis ryThesis)
    {
        if (ryThesis.getId() == null) {
            return error("ID不能为空");
        }
        if (StringUtils.isEmpty(ryThesis.getAuditStatus())) {
            return error("审核状态不能为空");
        }
        return toAjax(ryThesisService.auditRyThesis(ryThesis));
    }

    /**
     * 删除学位论文管理
     */
    @PreAuthorize("@ss.hasPermi('system:thesis:remove')")
    @Log(title = "学位论文管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(ryThesisService.deleteRyThesisByIds(ids));
    }
}