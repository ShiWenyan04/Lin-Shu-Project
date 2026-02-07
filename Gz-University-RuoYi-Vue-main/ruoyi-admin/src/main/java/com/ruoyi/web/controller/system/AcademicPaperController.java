package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
// 🚀 1. 引入 SecurityUtils 获取当前用户信息
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.core.domain.model.LoginUser;
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
import com.ruoyi.system.domain.AcademicPaper;
import com.ruoyi.system.service.IAcademicPaperService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 学术论文写作Controller
 * * @author ruoyi
 * @date 2025-12-28
 */
@RestController
@RequestMapping("/system/paper")
public class AcademicPaperController extends BaseController
{
    @Autowired
    private IAcademicPaperService academicPaperService;

    /**
     * 查询列表
     */
    @PreAuthorize("@ss.hasPermi('system:paper:list')")
    @GetMapping("/list")
    public TableDataInfo list(AcademicPaper academicPaper)
    {
        // 🚀 修改点 1：获取当前用户名
        String username = SecurityUtils.getUsername();

        // 权限判断
        Long userId = SecurityUtils.getUserId();
        boolean isAdmin = SecurityUtils.isAdmin(userId);
        boolean isTeacher = SecurityUtils.hasRole("teacher");
        boolean isManager = SecurityUtils.hasRole("openwriting_manager");

        // 如果不是管理角色，只能查“创建人”是“自己用户名”的数据
        if (!isAdmin && !isTeacher && !isManager) {
            academicPaper.setCreateBy(username); // 传入用户名 "xiaoming"
        }

        startPage();
        List<AcademicPaper> list = academicPaperService.selectAcademicPaperList(academicPaper);
        return getDataTable(list);
    }

    /**
     * 新增
     */
    @PreAuthorize("@ss.hasPermi('system:paper:add')")
    @Log(title = "学术论文写作", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AcademicPaper academicPaper)
    {
        // 🚀 修改点 2：存入用户名
        academicPaper.setCreateBy(SecurityUtils.getUsername());
        return toAjax(academicPaperService.insertAcademicPaper(academicPaper));
    }

    /**
     * 修改
     */
    @PreAuthorize("@ss.hasPermi('system:paper:edit')")
    @Log(title = "学术论文写作", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AcademicPaper academicPaper)
    {
        // 🚀 修改点 3：存入用户名
        academicPaper.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(academicPaperService.updateAcademicPaper(academicPaper));
    }

    /**
     * 导出学术论文写作列表
     */
    @PreAuthorize("@ss.hasPermi('system:paper:export')")
    @Log(title = "学术论文写作", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AcademicPaper academicPaper)
    {
        // 🚀 导出也要加同样的权限控制，防止学生通过导出功能看到别人的数据
        Long userId = SecurityUtils.getUserId();
        boolean isAdmin = SecurityUtils.isAdmin(userId);
        boolean isTeacher = SecurityUtils.hasRole("teacher");
        boolean isManager = SecurityUtils.hasRole("openwriting_manager");

        if (!isAdmin && !isTeacher && !isManager) {
            academicPaper.setCreateBy(String.valueOf(userId));
        }

        List<AcademicPaper> list = academicPaperService.selectAcademicPaperList(academicPaper);
        ExcelUtil<AcademicPaper> util = new ExcelUtil<AcademicPaper>(AcademicPaper.class);
        util.exportExcel(response, list, "学术论文写作数据");
    }

    /**
     * 获取学术论文写作详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:paper:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(academicPaperService.selectAcademicPaperById(id));
    }


    /**
     * 删除学术论文写作
     */
    @PreAuthorize("@ss.hasPermi('system:paper:remove')")
    @Log(title = "学术论文写作", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(academicPaperService.deleteAcademicPaperByIds(ids));
    }
}