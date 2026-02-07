package com.ruoyi.web.controller.system;

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
import com.ruoyi.system.domain.BusiProject;
import com.ruoyi.system.service.IBusiProjectService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 项目信息Controller
 * 
 * @author ruoyi
 * @date 2025-12-17
 */
@RestController
@RequestMapping("/system/project")
public class BusiProjectController extends BaseController
{
    @Autowired
    private IBusiProjectService busiProjectService;

    /**
     * 查询项目信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:project:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusiProject busiProject)
    {
        startPage();
        List<BusiProject> list = busiProjectService.selectBusiProjectList(busiProject);
        return getDataTable(list);
    }

    /**
     * 导出项目信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:project:export')")
    @Log(title = "项目信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusiProject busiProject)
    {
        List<BusiProject> list = busiProjectService.selectBusiProjectList(busiProject);
        ExcelUtil<BusiProject> util = new ExcelUtil<BusiProject>(BusiProject.class);
        util.exportExcel(response, list, "项目信息数据");
    }

    /**
     * 获取项目信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:project:query')")
    @GetMapping(value = "/{projectId}")
    public AjaxResult getInfo(@PathVariable("projectId") Long projectId)
    {
        return success(busiProjectService.selectBusiProjectByProjectId(projectId));
    }

    /**
     * 新增项目信息
     */
    @PreAuthorize("@ss.hasPermi('system:project:add')")
    @Log(title = "项目信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusiProject busiProject)
    {
        return toAjax(busiProjectService.insertBusiProject(busiProject));
    }

    /**
     * 修改项目信息
     */
    @PreAuthorize("@ss.hasPermi('system:project:edit')")
    @Log(title = "项目信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusiProject busiProject)
    {
        return toAjax(busiProjectService.updateBusiProject(busiProject));
    }

    /**
     * 删除项目信息
     */
    @PreAuthorize("@ss.hasPermi('system:project:remove')")
    @Log(title = "项目信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{projectIds}")
    public AjaxResult remove(@PathVariable Long[] projectIds)
    {
        return toAjax(busiProjectService.deleteBusiProjectByProjectIds(projectIds));
    }
}
