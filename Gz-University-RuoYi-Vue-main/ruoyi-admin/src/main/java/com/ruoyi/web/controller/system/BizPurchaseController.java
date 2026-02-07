package com.ruoyi.web.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
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
import com.ruoyi.system.domain.BizPurchase;
import com.ruoyi.system.service.IBizPurchaseService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 购买支出信息Controller
 * 
 * @author ruoyi
 * @date 2025-12-17
 */
@RestController
@RequestMapping("/system/purchase")
public class BizPurchaseController extends BaseController
{
    @Autowired
    private IBizPurchaseService bizPurchaseService;

    /**
     * 查询购买支出信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:purchase:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizPurchase bizPurchase) // 这里的类名改为你生成的实体类名
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();

        if (bizPurchase.getParams() == null) {
            bizPurchase.setParams(new HashMap<>());
        }
        String queryMode = (String) bizPurchase.getParams().get("queryMode");
        if (StringUtils.isEmpty(queryMode)) {
            queryMode = "public";
        }

        if ("public".equals(queryMode)) {
            // 公共池：只看已通过
            bizPurchase.setAuditStatus("1");
            bizPurchase.setCreateBy(null);
        } else if ("private".equals(queryMode)) {
            // 我的提交：看自己创建的
            bizPurchase.setCreateBy(user.getUserName());
            bizPurchase.setAuditStatus(null);
        } else if ("audit".equals(queryMode)) {
            // 审核池：权限判断 + 只看待审核
            boolean hasAuth = false;
            if (SysUser.isAdmin(user.getUserId())) {
                hasAuth = true;
            } else {
                for (SysRole role : user.getRoles()) {
                    if ("teacher".equals(role.getRoleKey()) || "purchase_manager".equals(role.getRoleKey())) {
                        hasAuth = true; break;
                    }
                }
            }
            if (!hasAuth) return getDataTable(new ArrayList<>());
            if (!SecurityUtils.hasRole("purchase_manager") && !SecurityUtils.hasRole("admin")) {
                bizPurchase.setAuditStatus("0");
            }

            bizPurchase.setCreateBy(null);
        }

        startPage();
        List<BizPurchase> list = bizPurchaseService.selectBizPurchaseList(bizPurchase);
        return getDataTable(list);
    }
    // 2. 新增逻辑 (存入创建人)
    @PreAuthorize("@ss.hasPermi('system:purchase:add')")
    @Log(title = "购买支出", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizPurchase bizPurchase)
    {
        // ▼ 核心 ▼
        bizPurchase.setCreateBy(SecurityUtils.getUsername());
        return toAjax(bizPurchaseService.insertBizPurchase(bizPurchase));
    }

    /**
     * 导出购买支出信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:purchase:export')")
    @Log(title = "购买支出信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizPurchase bizPurchase)
    {
        List<BizPurchase> list = bizPurchaseService.selectBizPurchaseList(bizPurchase);
        ExcelUtil<BizPurchase> util = new ExcelUtil<BizPurchase>(BizPurchase.class);
        util.exportExcel(response, list, "购买支出信息数据");
    }

    /**
     * 获取购买支出信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:purchase:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bizPurchaseService.selectBizPurchaseById(id));
    }



    /**
     * 修改购买支出信息
     */
    @PreAuthorize("@ss.hasPermi('system:purchase:edit')")
    @Log(title = "购买支出", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizPurchase bizPurchase)
    {
        bizPurchase.setUpdateBy(SecurityUtils.getUsername());

        // 修改点：加入了 purchase_manager
        // 只有非（老师/购买负责人/管理员）修改时，才重置为 '0'
        if (!SecurityUtils.hasRole("teacher") && !SecurityUtils.hasRole("purchase_manager") && !SecurityUtils.hasRole("admin")) {
            bizPurchase.setAuditStatus("0");
        }

        return toAjax(bizPurchaseService.updateBizPurchase(bizPurchase));
    }
    // 4. 审核接口 (新增)
    @PreAuthorize("@ss.hasPermi('system:purchase:audit')")
    @Log(title = "购买支出审核", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody BizPurchase bizPurchase)
    {
        if (bizPurchase.getId() == null || bizPurchase.getAuditStatus() == null) {
            return AjaxResult.error("参数错误");
        }
        bizPurchase.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(bizPurchaseService.updateBizPurchase(bizPurchase));
    }

    /**
     * 删除购买支出信息
     */
    @PreAuthorize("@ss.hasPermi('system:purchase:remove')")
    @Log(title = "购买支出信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bizPurchaseService.deleteBizPurchaseByIds(ids));
    }
}
