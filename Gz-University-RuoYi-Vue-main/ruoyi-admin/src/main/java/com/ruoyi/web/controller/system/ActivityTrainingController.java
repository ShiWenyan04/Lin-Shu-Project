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
import com.ruoyi.system.domain.ActivityTraining;
import com.ruoyi.system.service.IActivityTrainingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 实训与竞赛活动Controller
 * 
 * @author ruoyi
 * @date 2026-01-15
 */
@RestController
@RequestMapping("/system/grouptraining")
public class ActivityTrainingController extends BaseController
{
    @Autowired
    private IActivityTrainingService activityTrainingService;

    /**
     * 查询实训与竞赛活动列表
     */
    @PreAuthorize("@ss.hasPermi('system:grouptraining:list')")
    @GetMapping("/list")
    public TableDataInfo list(ActivityTraining activityTraining)
    {
        startPage();
        List<ActivityTraining> list = activityTrainingService.selectActivityTrainingList(activityTraining);
        return getDataTable(list);
    }

    /**
     * 导出实训与竞赛活动列表
     */
    @PreAuthorize("@ss.hasPermi('system:grouptraining:export')")
    @Log(title = "实训与竞赛活动", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ActivityTraining activityTraining)
    {
        List<ActivityTraining> list = activityTrainingService.selectActivityTrainingList(activityTraining);
        ExcelUtil<ActivityTraining> util = new ExcelUtil<ActivityTraining>(ActivityTraining.class);
        util.exportExcel(response, list, "实训与竞赛活动数据");
    }

    /**
     * 获取实训与竞赛活动详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:grouptraining:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(activityTrainingService.selectActivityTrainingById(id));
    }

    /**
     * 新增实训与竞赛活动
     */
    @PreAuthorize("@ss.hasPermi('system:grouptraining:add')")
    @Log(title = "实训与竞赛活动", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ActivityTraining activityTraining)
    {
        return toAjax(activityTrainingService.insertActivityTraining(activityTraining));
    }

    /**
     * 修改实训与竞赛活动
     */
    @PreAuthorize("@ss.hasPermi('system:grouptraining:edit')")
    @Log(title = "实训与竞赛活动", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ActivityTraining activityTraining)
    {
        return toAjax(activityTrainingService.updateActivityTraining(activityTraining));
    }

    /**
     * 删除实训与竞赛活动
     */
    @PreAuthorize("@ss.hasPermi('system:grouptraining:remove')")
    @Log(title = "实训与竞赛活动", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(activityTrainingService.deleteActivityTrainingByIds(ids));
    }
}
