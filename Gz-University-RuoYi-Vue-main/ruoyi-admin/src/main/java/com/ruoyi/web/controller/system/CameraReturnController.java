package com.ruoyi.web.controller.system;

import java.util.ArrayList; // 【新增】
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysRole; // 【新增】
import com.ruoyi.common.core.domain.entity.SysUser; // 【新增】
import com.ruoyi.common.utils.SecurityUtils; // 【新增】
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
import com.ruoyi.system.domain.CameraReturn;
import com.ruoyi.system.service.ICameraReturnService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 红外相机归还与运维Controller
 * * @author ruoyi
 * @date 2025-12-18
 */
@RestController
@RequestMapping("/system/return")
public class CameraReturnController extends BaseController
{
    @Autowired
    private ICameraReturnService cameraReturnService;

    /**
     * 查询红外相机归还与运维列表
     */
    @PreAuthorize("@ss.hasPermi('system:return:list')")
    @GetMapping("/list")
    public TableDataInfo list(CameraReturn cameraReturn)
    {
        if (cameraReturn.getParams() != null) {
            String queryMode = (String) cameraReturn.getParams().get("queryMode");

            // 1. 私有模式
            if ("private".equals(queryMode)) {
                cameraReturn.setCreateBy(getUsername());
            }
            // 2. 【新增】审核管理模式
            else if ("audit".equals(queryMode)) {
                SysUser user = SecurityUtils.getLoginUser().getUser();
                boolean hasAuth = false;
                // 校验权限：管理员、老师、归还负责人(return_manager)
                if (SysUser.isAdmin(user.getUserId())) {
                    hasAuth = true;
                } else {
                    for (SysRole role : user.getRoles()) {
                        if ("teacher".equals(role.getRoleKey()) || "return_manager".equals(role.getRoleKey())) {
                            hasAuth = true; break;
                        }
                    }
                }
                if (!hasAuth) return getDataTable(new ArrayList<>());

                // 过滤逻辑：
                // 如果是 return_manager 或 admin，允许看所有状态（不设置 auditStatus）
                // 如果仅是 teacher，只看待审核 '0'
                if (!SecurityUtils.hasRole("return_manager") && !SecurityUtils.hasRole("admin")) {
                    cameraReturn.setAuditStatus("0");
                }

                // 清空 createBy 以便查看所有人
                cameraReturn.setCreateBy(null);
            }
        }

        startPage();
        List<CameraReturn> list = cameraReturnService.selectCameraReturnList(cameraReturn);
        return getDataTable(list);
    }

    /**
     * 导出红外相机归还与运维列表
     */
    @PreAuthorize("@ss.hasPermi('system:return:export')")
    @Log(title = "红外相机归还与运维", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CameraReturn cameraReturn)
    {
        List<CameraReturn> list = cameraReturnService.selectCameraReturnList(cameraReturn);
        ExcelUtil<CameraReturn> util = new ExcelUtil<CameraReturn>(CameraReturn.class);
        util.exportExcel(response, list, "红外相机归还与运维数据");
    }

    /**
     * 获取红外相机归还与运维详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:return:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(cameraReturnService.selectCameraReturnById(id));
    }

    /**
     * 新增红外相机归还与运维
     */
    @PreAuthorize("@ss.hasPermi('system:return:add')")
    @Log(title = "红外相机归还与运维", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CameraReturn cameraReturn)
    {
        // === 2. 自动设置创建人 ===
        cameraReturn.setCreateBy(getUsername());
        return toAjax(cameraReturnService.insertCameraReturn(cameraReturn));
    }

    /**
     * 修改红外相机归还与运维
     */
    @PreAuthorize("@ss.hasPermi('system:return:edit')")
    @Log(title = "红外相机归还与运维", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CameraReturn cameraReturn)
    {
        cameraReturn.setUpdateBy(getUsername());

        // 【新增】权限控制状态重置
        // 只有非（老师/归还负责人/管理员）修改时，才重置为 '0'
        if (!SecurityUtils.hasRole("teacher") && !SecurityUtils.hasRole("return_manager") && !SecurityUtils.hasRole("admin")) {
            cameraReturn.setAuditStatus("0");
        }

        return toAjax(cameraReturnService.updateCameraReturn(cameraReturn));
    }

    /**
     * 删除红外相机归还与运维
     */
    @PreAuthorize("@ss.hasPermi('system:return:remove')")
    @Log(title = "红外相机归还与运维", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cameraReturnService.deleteCameraReturnByIds(ids));
    }

    /**
     * 审核接口 (新增)
     */
    @PreAuthorize("@ss.hasPermi('system:return:audit')") // 记得给管理员配这个权限
    @Log(title = "红外相机归还审核", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody CameraReturn cameraReturn)
    {
        return toAjax(cameraReturnService.auditCameraReturn(cameraReturn));
    }
}