package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.TeamMember;
import com.ruoyi.system.service.ITeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 团队成员Controller
 * 
 * @author ruoyi
 * @date 2025-07-18
 */
@RestController
@RequestMapping("/system/member")
public class TeamMemberController extends BaseController
{
    @Autowired
    private ITeamMemberService teamMemberService;

    /**
     * 查询团队成员列表
     */
    @PreAuthorize("@ss.hasPermi('system:member:list')")
    @GetMapping("/list")
    public TableDataInfo list(TeamMember teamMember)
    {
        startPage();
        List<TeamMember> list = teamMemberService.selectTeamMemberList(teamMember);
        return getDataTable(list);
    }

    /**
     * 导出团队成员列表
     */
    @PreAuthorize("@ss.hasPermi('system:member:export')")
    @Log(title = "团队成员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TeamMember teamMember)
    {
        List<TeamMember> list = teamMemberService.selectTeamMemberList(teamMember);
        ExcelUtil<TeamMember> util = new ExcelUtil<TeamMember>(TeamMember.class);
        util.exportExcel(response, list, "团队成员数据");
    }

    /**
     * 获取团队成员详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:member:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(teamMemberService.selectTeamMemberById(id));
    }

    /**
     * 新增团队成员
     */
    @PreAuthorize("@ss.hasPermi('system:member:add')")
    @Log(title = "团队成员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TeamMember teamMember)
    {
        return toAjax(teamMemberService.insertTeamMember(teamMember));
    }

    /**
     * 修改团队成员
     */
    @PreAuthorize("@ss.hasPermi('system:member:edit')")
    @Log(title = "团队成员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TeamMember teamMember)
    {
        return toAjax(teamMemberService.updateTeamMember(teamMember));
    }

    /**
     * 删除团队成员
     */
    @PreAuthorize("@ss.hasPermi('system:member:remove')")
    @Log(title = "团队成员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(teamMemberService.deleteTeamMemberByIds(ids));
    }
}
