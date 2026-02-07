package com.ruoyi.web.controller.system;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.CameraDeployment;
import com.ruoyi.system.service.ICameraDeploymentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 红外相机布设管理Controller
 *
 * @author ruoyi
 * @date 2025-07-21
 */
@RestController
@RequestMapping("/system/deployment")
public class CameraDeploymentController extends BaseController
{
    @Autowired
    private ICameraDeploymentService cameraDeploymentService;

    /**
     * 查询红外相机布设管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:deployment:list')")
    @GetMapping("/list")
    public TableDataInfo list(CameraDeployment cameraDeployment,
                              @RequestParam(value = "deploymentStartMonth", required = false) String deploymentStartMonth)
    {
        // 处理年月搜索条件
        if (deploymentStartMonth != null && !deploymentStartMonth.isEmpty()) {
            // 将年月转换为该月的第一天和最后一天
            String startDate = deploymentStartMonth + "-01";
            String endDate = getLastDayOfMonth(deploymentStartMonth);

            // 设置查询条件到params中，供Service层使用
            Map<String, Object> params = new HashMap<>();
            params.put("deploymentStartBegin", startDate);
            params.put("deploymentStartEnd", endDate);
            cameraDeployment.setParams(params);
        }

        startPage();
        List<CameraDeployment> list = cameraDeploymentService.selectCameraDeploymentList(cameraDeployment);
        return getDataTable(list);
    }

    /**
     * 导出红外相机布设管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:deployment:export')")
    @Log(title = "红外相机布设管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CameraDeployment cameraDeployment,
                       @RequestParam(value = "deploymentStartMonth", required = false) String deploymentStartMonth)
    {
        // 处理年月搜索条件（与list方法保持一致）
        if (deploymentStartMonth != null && !deploymentStartMonth.isEmpty()) {
            String startDate = deploymentStartMonth + "-01";
            String endDate = getLastDayOfMonth(deploymentStartMonth);

            Map<String, Object> params = new HashMap<>();
            params.put("deploymentStartBegin", startDate);
            params.put("deploymentStartEnd", endDate);
            cameraDeployment.setParams(params);
        }

        List<CameraDeployment> list = cameraDeploymentService.selectCameraDeploymentList(cameraDeployment);
        ExcelUtil<CameraDeployment> util = new ExcelUtil<CameraDeployment>(CameraDeployment.class);
        util.exportExcel(response, list, "红外相机布设管理数据");
    }

    /**
     * 获取红外相机布设管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:deployment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(cameraDeploymentService.selectCameraDeploymentById(id));
    }

    /**
     * 新增红外相机布设管理
     */
    @PreAuthorize("@ss.hasPermi('system:deployment:add')")
    @Log(title = "红外相机布设管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CameraDeployment cameraDeployment)
    {
        return toAjax(cameraDeploymentService.insertCameraDeployment(cameraDeployment));
    }

    /**
     * 修改红外相机布设管理
     */
    @PreAuthorize("@ss.hasPermi('system:deployment:edit')")
    @Log(title = "红外相机布设管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CameraDeployment cameraDeployment)
    {
        return toAjax(cameraDeploymentService.updateCameraDeployment(cameraDeployment));
    }

    /**
     * 删除红外相机布设管理
     */
    @PreAuthorize("@ss.hasPermi('system:deployment:remove')")
    @Log(title = "红外相机布设管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cameraDeploymentService.deleteCameraDeploymentByIds(ids));
    }

    /**
     * 获取某个月的最后一天
     */
    private String getLastDayOfMonth(String yearMonth) {
        try {
            String[] parts = yearMonth.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month - 1, 1);
            int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            return String.format("%s-%02d", yearMonth, lastDay);
        } catch (Exception e) {
            // 如果解析失败，返回默认值
            return yearMonth + "-31";
        }
    }
}