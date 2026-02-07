package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
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
import com.ruoyi.system.domain.AcademicPaperSubmission;
import com.ruoyi.system.service.IAcademicPaperSubmissionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 学术论文投稿Controller
 * 
 * @author ruoyi
 * @date 2025-12-28
 */
@RestController
@RequestMapping("/system/submission")
public class AcademicPaperSubmissionController extends BaseController
{
    @Autowired
    private IAcademicPaperSubmissionService academicPaperSubmissionService;

    /**
     * 查询列表
     */
    @PreAuthorize("@ss.hasPermi('system:submission:list')")
    @GetMapping("/list")
    public TableDataInfo list(AcademicPaperSubmission academicPaperSubmission)
    {
        // 🚀 这里的逻辑和上面一样
        String username = SecurityUtils.getUsername();
        Long userId = SecurityUtils.getUserId();

        boolean isAdmin = SecurityUtils.isAdmin(userId);
        boolean isTeacher = SecurityUtils.hasRole("teacher");
        boolean isManager = SecurityUtils.hasRole("openwriting_manager");

        if (!isAdmin && !isTeacher && !isManager) {
            academicPaperSubmission.setCreateBy(username); // 只能看自己提交的
        }

        startPage();
        List<AcademicPaperSubmission> list = academicPaperSubmissionService.selectAcademicPaperSubmissionList(academicPaperSubmission);
        return getDataTable(list);
    }

    /**
     * 新增
     */
    @PreAuthorize("@ss.hasPermi('system:submission:add')")
    @Log(title = "学术论文投稿", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AcademicPaperSubmission academicPaperSubmission)
    {
        // 🚀 存入用户名
        academicPaperSubmission.setCreateBy(SecurityUtils.getUsername());
        return toAjax(academicPaperSubmissionService.insertAcademicPaperSubmission(academicPaperSubmission));
    }

    /**
     * 修改
     */
    @PreAuthorize("@ss.hasPermi('system:submission:edit')")
    @Log(title = "学术论文投稿", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AcademicPaperSubmission academicPaperSubmission)
    {
        // 🚀 存入用户名
        academicPaperSubmission.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(academicPaperSubmissionService.updateAcademicPaperSubmission(academicPaperSubmission));
    }

    /**
     * 导出学术论文投稿列表
     */
    @PreAuthorize("@ss.hasPermi('system:submission:export')")
    @Log(title = "学术论文投稿", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AcademicPaperSubmission academicPaperSubmission)
    {
        List<AcademicPaperSubmission> list = academicPaperSubmissionService.selectAcademicPaperSubmissionList(academicPaperSubmission);
        ExcelUtil<AcademicPaperSubmission> util = new ExcelUtil<AcademicPaperSubmission>(AcademicPaperSubmission.class);
        util.exportExcel(response, list, "学术论文投稿数据");
    }

    /**
     * 获取学术论文投稿详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:submission:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(academicPaperSubmissionService.selectAcademicPaperSubmissionById(id));
    }


    /**
     * 删除学术论文投稿
     */
    @PreAuthorize("@ss.hasPermi('system:submission:remove')")
    @Log(title = "学术论文投稿", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(academicPaperSubmissionService.deleteAcademicPaperSubmissionByIds(ids));
    }
}
