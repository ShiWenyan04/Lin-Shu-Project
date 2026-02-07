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
import com.ruoyi.system.domain.FieldSurvey;
import com.ruoyi.system.service.IFieldSurveyService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 外业调查设备Controller
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
@RestController
@RequestMapping("/system/survey")
public class FieldSurveyController extends BaseController
{
    @Autowired
    private IFieldSurveyService fieldSurveyService;

    /**
     * 查询外业调查设备列表
     */
    @PreAuthorize("@ss.hasPermi('system:survey:list')")
    @GetMapping("/list")
    public TableDataInfo list(FieldSurvey fieldSurvey)
    {
        // === 关键修改开始 ===
        // 1. 获取前端传来的 params 参数
        String queryMode = null;
        if (fieldSurvey.getParams() != null) {
            queryMode = (String) fieldSurvey.getParams().get("queryMode");
        }

        // 2. 如果是“我的提交”模式
        if ("private".equals(queryMode)) {
            // 3. 强制设置 createBy 为当前登录用户
            // getUsername() 是 BaseController 提供的方法，获取当前登录账号
            fieldSurvey.setCreateBy(getUsername());
        }


        // 3. 审核/管理模式 (audit)
        else if ("audit".equals(queryMode)) {
            // 权限校验：如果是 fildsurvey_manager，也允许进入
            if (!SecurityUtils.hasRole("teacher")
                    && !SecurityUtils.hasRole("fildsurvey_manager")
                    && !SecurityUtils.hasRole("admin")) {
                // 无权访问，返回空列表
                return getDataTable(new java.util.ArrayList<>());
            }

            // 过滤逻辑：
            // 如果是 fildsurvey_manager 或 admin，允许看所有状态（不设置 auditStatus）
            // 如果仅是 teacher，只看待审核 '0'
            if (!SecurityUtils.hasRole("fildsurvey_manager") && !SecurityUtils.hasRole("admin")) {
                fieldSurvey.setAuditStatus("0");
            }

            fieldSurvey.setCreateBy(null);
        }
        // === 关键修改结束 ===

        startPage();
        List<FieldSurvey> list = fieldSurveyService.selectFieldSurveyList(fieldSurvey);
        return getDataTable(list);
    }

    /**
     * 导出外业调查设备列表
     */
    @PreAuthorize("@ss.hasPermi('system:survey:export')")
    @Log(title = "外业调查设备", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FieldSurvey fieldSurvey)
    {
        List<FieldSurvey> list = fieldSurveyService.selectFieldSurveyList(fieldSurvey);
        ExcelUtil<FieldSurvey> util = new ExcelUtil<FieldSurvey>(FieldSurvey.class);
        util.exportExcel(response, list, "外业调查设备数据");
    }

    /**
     * 获取外业调查设备详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:survey:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(fieldSurveyService.selectFieldSurveyById(id));
    }

    /**
     * 新增外业调查设备
     */
    @PreAuthorize("@ss.hasPermi('system:survey:add')")
    @Log(title = "外业调查设备", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FieldSurvey fieldSurvey)
    {
        fieldSurvey.setCreateBy(getUsername());
        return toAjax(fieldSurveyService.insertFieldSurvey(fieldSurvey));
    }

    /**
     * 修改外业调查设备
     */
    @PreAuthorize("@ss.hasPermi('system:survey:edit')")
    @Log(title = "外业调查设备", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FieldSurvey fieldSurvey)
    {
        fieldSurvey.setUpdateBy(getUsername());

        // 修改点：加入了 fildsurvey_manager
        // 只有非（老师/外业调查负责人/管理员）修改时，才重置为 '0'
        if (!SecurityUtils.hasRole("teacher") && !SecurityUtils.hasRole("fildsurvey_manager") && !SecurityUtils.hasRole("admin")) {
            // 假设你的实体类里有这个字段，虽然你发给我的Controller里没写这行，但通常需要重置
            // 如果你的业务逻辑里不需要重置，可以忽略。但标准流程建议重置。
            fieldSurvey.setAuditStatus("0");
        }

        return toAjax(fieldSurveyService.updateFieldSurvey(fieldSurvey));
    }

    /**
     * 删除外业调查设备
     */
    @PreAuthorize("@ss.hasPermi('system:survey:remove')")
    @Log(title = "外业调查设备", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fieldSurveyService.deleteFieldSurveyByIds(ids));
    }

    /**
     * 审核外业调查设备
     */
    @PreAuthorize("@ss.hasPermi('system:survey:audit')") // 记得去菜单管理给老师配这个权限
    @Log(title = "外业调查设备审核", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody FieldSurvey fieldSurvey)
    {
        // 可以在这里加校验，比如驳回时必须填原因
        return toAjax(fieldSurveyService.updateFieldSurvey(fieldSurvey));
    }
}
