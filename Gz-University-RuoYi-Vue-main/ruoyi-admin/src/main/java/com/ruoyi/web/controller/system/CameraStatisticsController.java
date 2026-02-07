package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.WildlifeSpecies;
import com.ruoyi.system.service.IWildlifeSpeciesService;
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
import com.ruoyi.system.domain.CameraStatistics;
import com.ruoyi.system.service.ICameraStatisticsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 红外相机数据Controller
 * 
 * @author ruoyi
 * @date 2025-07-26
 */
@RestController
@RequestMapping("/system/statistics")
public class CameraStatisticsController extends BaseController
{
    @Autowired
    private ICameraStatisticsService cameraStatisticsService;
    @Autowired
    private IWildlifeSpeciesService wildlifeSpeciesService;

    /**
     * 查询红外相机数据列表
     */
    @PreAuthorize("@ss.hasPermi('system:statistics:list')")
    @GetMapping("/list")
    public TableDataInfo list(CameraStatistics cameraStatistics)
    {
        startPage();
        List<CameraStatistics> list = cameraStatisticsService.selectCameraStatisticsList(cameraStatistics);
        return getDataTable(list);
    }

    /**
     * 导出红外相机数据列表
     */
    @PreAuthorize("@ss.hasPermi('system:statistics:export')")
    @Log(title = "红外相机数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CameraStatistics cameraStatistics)
    {
        List<CameraStatistics> list = cameraStatisticsService.selectCameraStatisticsList(cameraStatistics);
        ExcelUtil<CameraStatistics> util = new ExcelUtil<CameraStatistics>(CameraStatistics.class);
        util.exportExcel(response, list, "红外相机数据数据");
    }

    /**
     * 获取红外相机数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:statistics:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(cameraStatisticsService.selectCameraStatisticsById(id));
    }

    /**
     * 新增红外相机数据
     */
    @PreAuthorize("@ss.hasPermi('system:statistics:add')")
    @Log(title = "红外相机数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CameraStatistics cameraStatistics) {
        // 添加物种校验逻辑
        if (StringUtils.isNotBlank(cameraStatistics.getSpeciesName())) {
            // 检查物种是否存在于野生动物表中
            WildlifeSpecies speciesQuery = new WildlifeSpecies();
            speciesQuery.setSpeciesName(cameraStatistics.getSpeciesName());
            List<WildlifeSpecies> speciesList = wildlifeSpeciesService.selectWildlifeSpeciesList(speciesQuery);

            if (speciesList == null || speciesList.isEmpty()) {
                // 返回特殊错误码 600 表示物种不存在
                return AjaxResult.error(600, "物种不存在，请先添加").put("speciesName", cameraStatistics.getSpeciesName());
            }
        }
        return toAjax(cameraStatisticsService.insertCameraStatistics(cameraStatistics));
    }

    /**
     * 修改红外相机数据
     */
    @PreAuthorize("@ss.hasPermi('system:statistics:edit')")
    @Log(title = "红外相机数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CameraStatistics cameraStatistics)
    {
        return toAjax(cameraStatisticsService.updateCameraStatistics(cameraStatistics));
    }

    /**
     * 删除红外相机数据
     */
    @PreAuthorize("@ss.hasPermi('system:statistics:remove')")
    @Log(title = "红外相机数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cameraStatisticsService.deleteCameraStatisticsByIds(ids));
    }

    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<CameraStatistics> util = new ExcelUtil<>(CameraStatistics.class);
        List<CameraStatistics> statisticsList = util.importExcel(file.getInputStream());
        String message = cameraStatisticsService.importCameraStatistics(statisticsList, updateSupport);
        return AjaxResult.success(message);
    }

    /**
     * 下载导入模板
     */
    @GetMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<CameraStatistics> util = new ExcelUtil<>(CameraStatistics.class);
        util.importTemplateExcel(response, "红外相机数据");
    }

}
