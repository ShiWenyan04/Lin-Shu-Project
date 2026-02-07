package com.ruoyi.web.controller.system;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.mapper.SysRoleMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.GroupTaskAssign;
import com.ruoyi.system.service.IGroupTaskAssignService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.common.core.domain.entity.SysRole;

/**
 * 组务分工管理Controller
 */
@RestController
@RequestMapping("/system/assign")
public class GroupTaskAssignController extends BaseController
{
    @Autowired
    private IGroupTaskAssignService groupTaskAssignService;

    @Autowired
    private SysRoleMapper roleMapper;

    @GetMapping("/roleOptionSelect")
    public AjaxResult roleOptionSelect()
    {
        // 直接查 Mapper，绕过权限过滤
        List<SysRole> list = roleMapper.selectRoleList(new SysRole());
        return success(list);
    }

    /**
     * 查询组务分工管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:assign:list')")
    @GetMapping("/list")
    public TableDataInfo list(GroupTaskAssign groupTaskAssign)
    {
        // 【修改逻辑】获取前端传来的 queryMode
        Map<String, Object> params = groupTaskAssign.getParams();
        String queryMode = (String) params.get("queryMode");

        // 1. 如果是 "我的申请" (private)，强制限制只查自己的
        if ("private".equals(queryMode)) {
            groupTaskAssign.setUserId(SecurityUtils.getUserId());
        }

        // 2. 如果是 "全部任务" (public)，XML 中会自动过滤 status='1'，这里无需操作

        // 3. 如果是 "审核管理" (audit)，这里不做限制，查询所有记录供导师审核

        startPage();
        List<GroupTaskAssign> list = groupTaskAssignService.selectGroupTaskAssignList(groupTaskAssign);
        return getDataTable(list);
    }

    /**
     * 导出组务分工管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:assign:export')")
    @Log(title = "组务分工管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GroupTaskAssign groupTaskAssign)
    {
        // 导出逻辑同上，根据 queryMode 处理
        Map<String, Object> params = groupTaskAssign.getParams();
        String queryMode = (String) params.get("queryMode");

        if ("private".equals(queryMode)) {
            groupTaskAssign.setUserId(SecurityUtils.getUserId());
        }

        List<GroupTaskAssign> list = groupTaskAssignService.selectGroupTaskAssignList(groupTaskAssign);
        ExcelUtil<GroupTaskAssign> util = new ExcelUtil<GroupTaskAssign>(GroupTaskAssign.class);
        util.exportExcel(response, list, "组务分工管理数据");
    }

    /**
     * 获取组务分工管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:assign:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(groupTaskAssignService.selectGroupTaskAssignById(id));
    }

    /**
     * 新增组务分工管理 (管理员直接分配)
     */
    @PreAuthorize("@ss.hasPermi('system:assign:add')")
    @Log(title = "组务分工管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GroupTaskAssign groupTaskAssign)
    {
        return toAjax(groupTaskAssignService.insertGroupTaskAssign(groupTaskAssign));
    }

    /**
     * 学生申请 (新增接口)
     */
    @PreAuthorize("@ss.hasRole('student')")
    @Log(title = "组务分工申请", businessType = BusinessType.INSERT)
    @PostMapping("/apply")
    public AjaxResult apply(@RequestBody GroupTaskAssign groupTaskAssign)
    {
        return toAjax(groupTaskAssignService.applyGroupTaskAssign(groupTaskAssign));
    }

    /**
     * 修改组务分工管理 (普通修改)
     */
    @PreAuthorize("@ss.hasPermi('system:assign:edit')")
    @Log(title = "组务分工管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GroupTaskAssign groupTaskAssign)
    {
        return toAjax(groupTaskAssignService.updateGroupTaskAssign(groupTaskAssign));
    }


    /**
     * 用户主动卸任角色 (辞职)
     */
    @PreAuthorize("@ss.hasRole('student')") // 只要是学生就能调(因为要变回学生)
    @Log(title = "卸任角色", businessType = BusinessType.GRANT)
    @PostMapping("/cancelRole")
    public AjaxResult cancelRole(@RequestBody Map<String, Long> params)
    {
        Long roleId = params.get("roleId");
        if (roleId == null) {
            return AjaxResult.error("参数缺失");
        }

        // 调用Service处理卸任逻辑
        return toAjax(groupTaskAssignService.cancelUserRole(roleId));
    }


    /**
     * 审核分工申请 (导师使用)
     */
    @PreAuthorize("@ss.hasRole('teacher') || @ss.hasRole('admin')")
    @Log(title = "组务分工审核", businessType = BusinessType.UPDATE)
    @PostMapping("/audit")
    public AjaxResult audit(@RequestBody Map<String, Object> params)
    {
        if (params.get("id") == null || params.get("status") == null) {
            return AjaxResult.error("参数缺失");
        }
        return toAjax(groupTaskAssignService.auditGroupTaskAssign(params));
    }

    /**
     * 删除组务分工管理
     */
    @PreAuthorize("@ss.hasPermi('system:assign:remove')")
    @Log(title = "组务分工管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(groupTaskAssignService.deleteGroupTaskAssignByIds(ids));
    }
}