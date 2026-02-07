package com.ruoyi.web.controller.system;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.KmlUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BusiCameraPoint;
import com.ruoyi.system.domain.BusiSpeciesData; // 必须引入
import com.ruoyi.system.service.IBusiCameraPointService;
import com.ruoyi.system.service.IBusiSpeciesDataService; // 必须引入
import org.apache.poi.ss.usermodel.*; // Excel相关
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.BusiCameraInstall;
import com.ruoyi.system.service.IBusiCameraInstallService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 红外相机安装项目Controller
 * * @author ruoyi
 * @date 2026-01-12
 */
@RestController
@RequestMapping("/system/CameraInstall")
public class BusiCameraInstallController extends BaseController
{
    @Autowired
    private IBusiCameraInstallService busiCameraInstallService;

    @Autowired
    private IBusiCameraPointService cameraPointService;

    @Autowired
    private IBusiSpeciesDataService busiSpeciesDataService; // 【新增】注入物种数据Service

    /**
     * 查询红外相机安装项目列表
     */
    @PreAuthorize("@ss.hasPermi('system:CameraInstall:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusiCameraInstall busiCameraInstall)
    {
        startPage();
        List<BusiCameraInstall> list = busiCameraInstallService.selectBusiCameraInstallList(busiCameraInstall);
        return getDataTable(list);
    }

    /**
     * 导出红外相机安装项目列表
     */
    @PreAuthorize("@ss.hasPermi('system:CameraInstall:export')")
    @Log(title = "红外相机安装项目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusiCameraInstall busiCameraInstall)
    {
        List<BusiCameraInstall> list = busiCameraInstallService.selectBusiCameraInstallList(busiCameraInstall);
        ExcelUtil<BusiCameraInstall> util = new ExcelUtil<BusiCameraInstall>(BusiCameraInstall.class);
        util.exportExcel(response, list, "红外相机安装项目数据");
    }

    /**
     * 获取红外相机安装项目详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:CameraInstall:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(busiCameraInstallService.selectBusiCameraInstallById(id));
    }

    /**
     * 新增红外相机安装项目
     */
    @PreAuthorize("@ss.hasPermi('system:CameraInstall:add')")
    @Log(title = "红外相机安装项目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusiCameraInstall busiCameraInstall)
    {
        busiCameraInstallService.insertBusiCameraInstall(busiCameraInstall);
        return toAjax(busiCameraInstallService.insertBusiCameraInstall(busiCameraInstall));
    }

    /**
     * 修改红外相机安装项目
     */
    @PreAuthorize("@ss.hasPermi('system:CameraInstall:edit')")
    @Log(title = "红外相机安装项目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusiCameraInstall busiCameraInstall)
    {
        return toAjax(busiCameraInstallService.updateBusiCameraInstall(busiCameraInstall));
    }

    /**
     * 删除红外相机安装项目
     */
    @PreAuthorize("@ss.hasPermi('system:CameraInstall:remove')")
    @Log(title = "红外相机安装项目", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(busiCameraInstallService.deleteBusiCameraInstallByIds(ids));
    }

    /**
     * 上传并解析KML文件接口 (支持模式区分)
     */
    @Log(title = "导入KML", businessType = BusinessType.IMPORT)
    @PostMapping("/importKml")
    public AjaxResult importKml(@RequestParam("file") MultipartFile file,
                                @RequestParam("installId") Long installId,
                                @RequestParam(value = "mode", defaultValue = "point") String mode) {
        try {
            Map<String, Object> parsedData = KmlUtils.parseKml(file);
            List<Map<String, Object>> points = (List<Map<String, Object>>) parsedData.get("points");
            String boundary = (String) parsedData.get("boundary");

            StringBuilder msg = new StringBuilder();

            // 准备更新对象
            BusiCameraInstall updateInstall = new BusiCameraInstall();
            updateInstall.setId(installId);

            if ("point".equals(mode)) {
                if (points.isEmpty()) {
                    return AjaxResult.error("导入失败：文件中未找到点位(Placemark/Point)信息");
                }
                for (Map<String, Object> map : points) {
                    BusiCameraPoint point = new BusiCameraPoint();
                    point.setInstallId(installId);
                    point.setSiteCode((String) map.get("siteCodeRaw"));
                    point.setLongitude((BigDecimal) map.get("longitude"));
                    point.setLatitude((BigDecimal) map.get("latitude"));
                    point.setAltitude(BigDecimal.ZERO);
                    cameraPointService.insertBusiCameraPoint(point);
                }

                // 【新增修改】保存点位文件名
                updateInstall.setPointFileName(file.getOriginalFilename());

                // 执行更新
                busiCameraInstallService.updateBusiCameraInstall(updateInstall);

                msg.append("成功导入 ").append(points.size()).append(" 个点位。");
            }
            else if ("boundary".equals(mode)) {
                if (StringUtils.isEmpty(boundary)) {
                    return AjaxResult.error("导入失败：文件中未找到多边形区域(Polygon)信息");
                }

                updateInstall.setBoundaryJson(boundary);

                // 【新增修改】保存边界文件名
                updateInstall.setBoundaryFileName(file.getOriginalFilename());

                // 执行更新
                busiCameraInstallService.updateBusiCameraInstall(updateInstall);

                msg.append("成功更新项目边界区域。");
            }
            updateInstall.setAuditStatus("0");
            updateInstall.setAuditReason("");

            busiCameraInstallService.updateBusiCameraInstall(updateInstall);
            return AjaxResult.success(msg.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 【新增】导入物种监测数据 Excel
     */
    @Log(title = "导入物种数据", businessType = BusinessType.IMPORT)
    @PostMapping("/importExcel")
    public AjaxResult importSpeciesExcel(@RequestParam("file") MultipartFile file, @RequestParam("installId") Long installId) {
        try {
            InputStream is = file.getInputStream();
            Workbook wb = WorkbookFactory.create(is);
            Sheet sheet = wb.getSheetAt(0);

            // 1. 查出当前项目所有点位 (例如：L17-1, L17-2, H01-1)
            BusiCameraPoint query = new BusiCameraPoint();
            query.setInstallId(installId);
            List<BusiCameraPoint> points = cameraPointService.selectBusiCameraPointList(query);

            if (points == null || points.isEmpty()) {
                return AjaxResult.error("该项目下没有点位数据，请先导入KML点位！");
            }

            int successCount = 0;
            // 假设第一行是表头，从 i=1 开始
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // 读取Excel列 (根据你的Excel模板调整列索引)
                String excelCode = getCellValue(row.getCell(0));     // 第1列：L17
                String category = getCellValue(row.getCell(1));      // 第2列：兽类
                String species = getCellValue(row.getCell(2));       // 第3列：豹猫

                // 简单的非空校验
                if (StringUtils.isEmpty(excelCode) || StringUtils.isEmpty(species)) continue;

                // 【关键修改】在现有点位中寻找匹配项
                // 逻辑：如果数据库点位是 "L17" 或 "L17-1"，都能被 Excel中的 "L17" 匹配到
                Long matchedPointId = null;

                for (BusiCameraPoint p : points) {
                    String dbCode = p.getSiteCode(); // 数据库里的 L17-1
                    if (dbCode != null) {
                        // 情况A: 完全相等 (Excel: L17-1, DB: L17-1)
                        if (dbCode.equals(excelCode)) {
                            matchedPointId = p.getId();
                            break;
                        }
                        // 情况B: 前缀匹配 (Excel: L17, DB: L17-1) -> 必须加上 "-" 防止 L17 匹配到 L170
                        if (dbCode.startsWith(excelCode + "-") || dbCode.startsWith(excelCode + "_")) {
                            matchedPointId = p.getId();
                            break; // 找到第一个匹配的就退出 (通常一个点位编号对应一个物理位置)
                        }
                    }
                }

                // 只有匹配到了点位，才插入数据
                if (matchedPointId != null) {
                    BusiSpeciesData data = new BusiSpeciesData();
                    data.setInstallId(installId);
                    data.setPointId(matchedPointId);
                    data.setSiteCodeRef(excelCode); // 记录Excel里的原始编号 L17
                    data.setClassName(category);
                    data.setSpeciesName(species);

                    busiSpeciesDataService.insertBusiSpeciesData(data);
                    successCount++;
                }
            }
            return AjaxResult.success("导入完成！共匹配并导入 " + successCount + " 条记录。");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 【核心修改】手动添加物种监测数据
     * 保持与Excel导入一致的匹配逻辑
     */
    @Log(title = "手动添加物种数据", businessType = BusinessType.INSERT)
    @PostMapping("/addManual")
    public AjaxResult addManualData(@RequestBody Map<String, Object> body) {
        try {
            Long installId = Long.valueOf(body.get("installId").toString());
            String siteCodeInput = (String) body.get("siteCode");   // 用户输入 L17
            String speciesName = (String) body.get("speciesName"); // 用户输入 豹猫

            if (StringUtils.isEmpty(siteCodeInput) || StringUtils.isEmpty(speciesName)) {
                return AjaxResult.error("点位编号和物种名称不能为空");
            }

            // 1. 查找匹配的点位
            BusiCameraPoint query = new BusiCameraPoint();
            query.setInstallId(installId);
            List<BusiCameraPoint> points = cameraPointService.selectBusiCameraPointList(query);

            Long matchedPointId = null;
            for (BusiCameraPoint p : points) {
                String dbCode = p.getSiteCode();
                // 同样的匹配逻辑：相等 或 前缀匹配
                if (dbCode.equals(siteCodeInput) || dbCode.startsWith(siteCodeInput + "-")) {
                    matchedPointId = p.getId();
                    break;
                }
            }

            if (matchedPointId == null) {
                return AjaxResult.error("未找到编号为 " + siteCodeInput + " (或 " + siteCodeInput + "-x) 的点位，请检查KML是否已导入。");
            }

            // 2. 存入数据库
            BusiSpeciesData data = new BusiSpeciesData();
            data.setInstallId(installId);
            data.setPointId(matchedPointId);
            data.setSiteCodeRef(siteCodeInput);
            data.setClassName("手动录入");
            data.setSpeciesName(speciesName);

            busiSpeciesDataService.insertBusiSpeciesData(data);

            return AjaxResult.success("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("添加失败：" + e.getMessage());
        }
    }

    // 辅助方法：获取单元格字符串
    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }

    /**
     * 【新增】审核红外相机安装项目
     */
    @PreAuthorize("@ss.hasPermi('system:CameraInstall:audit')") // 需要在菜单里配置这个权限字符
    @Log(title = "红外相机安装项目审核", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody BusiCameraInstall busiCameraInstall)
    {
        // 校验：必须包含ID和审核状态
        if (busiCameraInstall.getId() == null || busiCameraInstall.getAuditStatus() == null) {
            return AjaxResult.error("参数错误");
        }
        return toAjax(busiCameraInstallService.auditBusiCameraInstall(busiCameraInstall));
    }
}
