package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.core.domain.model.LoginUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*; // 导入所有注解
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.BusiCameraResult;
import com.ruoyi.system.service.IBusiCameraResultService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 红外相机成果管理Controller
 * * @author ruoyi
 * @date 2026-01-14
 */
@RestController
@RequestMapping("/system/cameraresult")
public class BusiCameraResultController extends BaseController
{
    @Autowired
    private IBusiCameraResultService busiCameraResultService;

    /**
     * 查询红外相机成果管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:cameraresult:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusiCameraResult busiCameraResult)
    {
        // 获取当前用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        boolean isAdmin = SecurityUtils.isAdmin(loginUser.getUserId());

        // 【核心修改】
        // 检查用户是否拥有“管理类”角色（老师 或 成果负责人）
        // 注意：这里使用的是 RoleKey，请确保你在系统管理里配置的角色字符是 'teacher' 和 'cameraResults_manager'
        boolean isManager = loginUser.getUser().getRoles().stream()
                .anyMatch(r -> "teacher".equals(r.getRoleKey())
                        || "cameraResults_manager".equals(r.getRoleKey()));

        // 逻辑变了：如果既不是超级管理员，也不是管理者，那才强制只查自己的
        if (!isAdmin && !isManager) {
            busiCameraResult.setCreateBy(loginUser.getUsername());
        }

        startPage();
        List<BusiCameraResult> list = busiCameraResultService.selectBusiCameraResultList(busiCameraResult);
        return getDataTable(list);
    }

    /**
     * 导出红外相机成果管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:cameraresult:export')")
    @Log(title = "红外相机成果管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusiCameraResult busiCameraResult)
    {
        // 导出也要遵循同样的权限逻辑
        LoginUser loginUser = SecurityUtils.getLoginUser();
        boolean isStudent = loginUser.getUser().getRoles().stream()
                .anyMatch(r -> "student".equals(r.getRoleKey()));
        if (isStudent && !SecurityUtils.isAdmin(loginUser.getUserId())) {
            busiCameraResult.setCreateBy(loginUser.getUsername());
        }

        List<BusiCameraResult> list = busiCameraResultService.selectBusiCameraResultList(busiCameraResult);
        ExcelUtil<BusiCameraResult> util = new ExcelUtil<BusiCameraResult>(BusiCameraResult.class);
        util.exportExcel(response, list, "红外相机成果管理数据");
    }

    /**
     * 获取红外相机成果管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:cameraresult:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(busiCameraResultService.selectBusiCameraResultById(id));
    }

    /**
     * 新增红外相机成果管理
     */
    @PreAuthorize("@ss.hasPermi('system:cameraresult:add')")
    @Log(title = "红外相机成果管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusiCameraResult busiCameraResult)
    {
        return toAjax(busiCameraResultService.insertBusiCameraResult(busiCameraResult));
    }

    /**
     * 修改红外相机成果管理
     */
    @PreAuthorize("@ss.hasPermi('system:cameraresult:edit')")
    @Log(title = "红外相机成果管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusiCameraResult busiCameraResult)
    {
        return toAjax(busiCameraResultService.updateBusiCameraResult(busiCameraResult));
    }

    /**
     * 【新增】审核接口
     * 需要在前端菜单里给老师/负责人配置 system:cameraresult:audit 权限
     */
    @PreAuthorize("@ss.hasPermi('system:cameraresult:audit')")
    @Log(title = "红外相机成果审核", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody BusiCameraResult busiCameraResult)
    {
        return toAjax(busiCameraResultService.auditBusiCameraResult(busiCameraResult));
    }

    /**
     * 删除红外相机成果管理
     */
    @PreAuthorize("@ss.hasPermi('system:cameraresult:remove')")
    @Log(title = "红外相机成果管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(busiCameraResultService.deleteBusiCameraResultByIds(ids));
    }
}