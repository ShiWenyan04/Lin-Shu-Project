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
import com.ruoyi.system.domain.ThesisProposal;
import com.ruoyi.system.service.IThesisProposalService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils; // 【新增】引入权限工具类

/**
 * 开题与学位论文Controller
 * * @author ruoyi
 * @date 2025-12-28
 */
@RestController
@RequestMapping("/system/proposal")
public class ThesisProposalController extends BaseController
{
    @Autowired
    private IThesisProposalService thesisProposalService;

    /**
     * 查询开题与学位论文列表
     */
    @PreAuthorize("@ss.hasPermi('system:proposal:list')")
    @GetMapping("/list")
    public TableDataInfo list(ThesisProposal thesisProposal)
    {
        // 【新增逻辑】权限控制：如果是学生，只能看自己的
        if (SecurityUtils.hasRole("student")) {
            thesisProposal.setCreateBy(SecurityUtils.getUsername());
        }

        startPage();
        List<ThesisProposal> list = thesisProposalService.selectThesisProposalList(thesisProposal);
        return getDataTable(list);
    }

    /**
     * 导出开题与学位论文列表
     */
    @PreAuthorize("@ss.hasPermi('system:proposal:export')")
    @Log(title = "开题与学位论文", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ThesisProposal thesisProposal)
    {
        // 【新增逻辑】权限控制：如果是学生，只能导出自己的
        if (SecurityUtils.hasRole("student")) {
            thesisProposal.setCreateBy(SecurityUtils.getUsername());
        }

        List<ThesisProposal> list = thesisProposalService.selectThesisProposalList(thesisProposal);
        ExcelUtil<ThesisProposal> util = new ExcelUtil<ThesisProposal>(ThesisProposal.class);
        util.exportExcel(response, list, "开题与学位论文数据");
    }

    /**
     * 获取开题与学位论文详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:proposal:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(thesisProposalService.selectThesisProposalById(id));
    }

    /**
     * 新增开题与学位论文
     */
    @PreAuthorize("@ss.hasPermi('system:proposal:add')")
    @Log(title = "开题与学位论文", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ThesisProposal thesisProposal)
    {
        return toAjax(thesisProposalService.insertThesisProposal(thesisProposal));
    }

    /**
     * 修改开题与学位论文
     */
    @PreAuthorize("@ss.hasPermi('system:proposal:edit')")
    @Log(title = "开题与学位论文", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ThesisProposal thesisProposal)
    {
        return toAjax(thesisProposalService.updateThesisProposal(thesisProposal));
    }

    /**
     * 删除开题与学位论文
     */
    @PreAuthorize("@ss.hasPermi('system:proposal:remove')")
    @Log(title = "开题与学位论文", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(thesisProposalService.deleteThesisProposalByIds(ids));
    }
}