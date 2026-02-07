package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.BusiGraduateTeaching;
import com.ruoyi.system.service.IBusiGraduateTeachingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;

@RestController
@RequestMapping("/system/teaching")
public class BusiGraduateTeachingController extends BaseController
{
    @Autowired
    private IBusiGraduateTeachingService busiGraduateTeachingService;

    /**
     * 查询列表 (带 Tab)
     */
    @PreAuthorize("@ss.hasPermi('system:teaching:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusiGraduateTeaching busiGraduateTeaching, @RequestParam(required = false) String tabType)
    {
        startPage();
        Long userId = SecurityUtils.getUserId();

        // --- 核心修复逻辑开始 ---

        // 1. 如果是【我的提交】(MINE)
        // 强制只查自己创建的数据
        if ("MINE".equals(tabType)) {
            busiGraduateTeaching.setCreateBy(String.valueOf(userId));
        }
        // 2. 如果是【审核管理】(AUDIT)
        // 强制只查【待审核】的数据 (audit_status = '0')
        else if ("AUDIT".equals(tabType)) {
            busiGraduateTeaching.setAuditStatus("0");
            // 注意：这里绝对不能设置 setCreateBy，否则老师只能审自己的
        }
        // 3. 如果是【全部记录】(ALL)
        else {
            // 默认只显示【已通过】的给所有人看
            // 如果你想让所有人看到所有状态，就把这一行删掉
            busiGraduateTeaching.setAuditStatus("1");
        }

        // --- 核心修复逻辑结束 ---

        // 调用 Service 时，只需要传一个对象即可，因为条件已经塞进去了
        // 注意：原本的 Service 方法 selectBusiGraduateTeachingList(obj, tabType, userId) 可能需要改回标准签名
        // 如果你不想改 Service 签名，就保持原样调用，但 XML 只认对象里的属性
        List<BusiGraduateTeaching> list = busiGraduateTeachingService.selectBusiGraduateTeachingList(busiGraduateTeaching);

        return getDataTable(list);
    }

    /**
     * 审核接口
     */
    @PreAuthorize("@ss.hasPermi('system:teaching:audit')")
    @Log(title = "研究生助教审核", businessType = BusinessType.UPDATE)
    @PostMapping("/audit")
    public AjaxResult audit(@RequestBody BusiGraduateTeaching body)
    {
        return toAjax(busiGraduateTeachingService.auditBusiGraduateTeaching(body.getId(), body.getAuditStatus(), body.getAuditReason()));
    }

    // --- 标准 CRUD ---

    @PreAuthorize("@ss.hasPermi('system:teaching:export')")
    @Log(title = "研究生助教安排", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusiGraduateTeaching busiGraduateTeaching)
    {
        List<BusiGraduateTeaching> list = busiGraduateTeachingService.selectBusiGraduateTeachingList(busiGraduateTeaching);
        ExcelUtil<BusiGraduateTeaching> util = new ExcelUtil<BusiGraduateTeaching>(BusiGraduateTeaching.class);
        util.exportExcel(response, list, "研究生助教安排数据");
    }

    @PreAuthorize("@ss.hasPermi('system:teaching:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(busiGraduateTeachingService.selectBusiGraduateTeachingById(id));
    }

    @PreAuthorize("@ss.hasPermi('system:teaching:add')")
    @Log(title = "研究生助教安排", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusiGraduateTeaching busiGraduateTeaching)
    {
        // 🚀 强制设置创建人
        busiGraduateTeaching.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        return toAjax(busiGraduateTeachingService.insertBusiGraduateTeaching(busiGraduateTeaching));
    }

    @PreAuthorize("@ss.hasPermi('system:teaching:edit')")
    @Log(title = "研究生助教安排", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusiGraduateTeaching busiGraduateTeaching)
    {
        return toAjax(busiGraduateTeachingService.updateBusiGraduateTeaching(busiGraduateTeaching));
    }

    @PreAuthorize("@ss.hasPermi('system:teaching:remove')")
    @Log(title = "研究生助教安排", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(busiGraduateTeachingService.deleteBusiGraduateTeachingByIds(ids));
    }
}