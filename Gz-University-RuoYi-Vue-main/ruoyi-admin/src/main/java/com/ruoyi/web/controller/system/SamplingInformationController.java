package com.ruoyi.web.controller.system;
import java.text.SimpleDateFormat;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SampleUsageRecord;
import com.ruoyi.system.domain.SamplingInformation;
import com.ruoyi.system.service.ISampleUsageRecordService;
import com.ruoyi.system.service.ISamplingInformationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 采样信息Controller
 *
 * @author ruoyi
 * @date 2025-07-19
 */
@RestController
@RequestMapping("/system/information")
public class SamplingInformationController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(SamplingInformationController.class);

    @Autowired
    private ISamplingInformationService samplingInformationService;

    // 添加样本使用记录服务注入
    @Autowired
    private ISampleUsageRecordService sampleUsageRecordService;

    /**
     * 查询采样信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:information:list')")
    @GetMapping("/list")
    public TableDataInfo list(SamplingInformation samplingInformation) {
        startPage();
        List<SamplingInformation> list = samplingInformationService.selectSamplingInformationList(samplingInformation);
        return getDataTable(list);
    }

    /**
     * 导出采样信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:information:export')")
    @Log(title = "采样信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SamplingInformation samplingInformation) {
        List<SamplingInformation> list = samplingInformationService.selectSamplingInformationList(samplingInformation);
        ExcelUtil<SamplingInformation> util = new ExcelUtil<SamplingInformation>(SamplingInformation.class);
        util.exportExcel(response, list, "采样信息数据");
    }

    /**
     * 获取采样信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:information:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(samplingInformationService.selectSamplingInformationById(id));
    }

    /**
     * 新增采样信息
     */
    @PreAuthorize("@ss.hasPermi('system:information:add')")
    @Log(title = "采样信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody SamplingInformation samplingInformation) {
        // 设置创建人和创建时间
        samplingInformation.setCreateBy(SecurityUtils.getUsername());
        samplingInformation.setCreateTime(new Date());
        return toAjax(samplingInformationService.insertSamplingInformation(samplingInformation));
    }

    /**
     * 修改采样信息
     */
    @PreAuthorize("@ss.hasPermi('system:information:edit')")
    @Log(title = "采样信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Valid @RequestBody SamplingInformation samplingInformation) {
        // 检查样本编号是否唯一
        if (!samplingInformationService.checkSampleNameUnique(samplingInformation)) {
            return AjaxResult.error("修改失败，样本编号已存在");
        }

        // 设置更新人和更新时间
        samplingInformation.setUpdateBy(SecurityUtils.getUsername());
        samplingInformation.setUpdateTime(new Date());
        return toAjax(samplingInformationService.updateSamplingInformation(samplingInformation));
    }

    /**
     * 检查样本编号是否唯一
     */
    @PostMapping("/checkSampleNameUnique")
    public AjaxResult checkSampleNameUnique(@RequestBody Map<String, Object> requestData) {
        String sampleName = (String) requestData.get("sampleName");
        Long id = convertToLong(requestData.get("id"));

        if (StringUtils.isBlank(sampleName)) {
            return AjaxResult.success(false); // 空值认为不唯一
        }

        // 检查样本编号是否已存在（排除当前记录）
        SamplingInformation query = new SamplingInformation();
        query.setSampleName(sampleName);
        List<SamplingInformation> list = samplingInformationService.selectSamplingInformationList(query);

        boolean isUnique = true;
        if (!list.isEmpty()) {
            // 如果是编辑操作，需要排除当前记录
            if (id != null) {
                for (SamplingInformation info : list) {
                    if (!info.getId().equals(id)) {
                        isUnique = false;
                        break;
                    }
                }
            } else {
                // 新增操作，只要存在就说明不唯一
                isUnique = false;
            }
        }

        // 返回 true 表示不唯一，false 表示唯一
        return AjaxResult.success(!isUnique);
    }

    /**
     * 删除采样信息
     */
    @PreAuthorize("@ss.hasPermi('system:information:remove')")
    @Log(title = "采样信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(samplingInformationService.deleteSamplingInformationByIds(ids));
    }

    /**
     * 用样操作 - 适配 DATE 类型字段
     */
    @PreAuthorize("@ss.hasPermi('system:information:use')")
    @Log(title = "样本使用", businessType = BusinessType.UPDATE)
    @PostMapping("/useSample")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult useSample(@RequestBody Map<String, Object> requestData) {
        try {
            log.info("===== 开始处理用样请求 =====");
            log.info("请求数据: {}", requestData);

            // 提取请求数据
            Long sampleId = convertToLong(requestData.get("sampleId"));
            String purpose = (String) requestData.get("purpose");
            String usedTimeStr = (String) requestData.get("usedTime");

            // 验证必要参数
            if (sampleId == null) {
                log.error("缺少必要参数: sampleId");
                return AjaxResult.error("缺少样本ID");
            }
            if (purpose == null || purpose.trim().isEmpty()) {
                log.error("缺少必要参数: purpose");
                return AjaxResult.error("缺少用途说明");
            }

            // 获取当前登录用户
            String username = SecurityUtils.getUsername();
            log.info("当前操作用户: {}", username);

            // 查询样本信息
            SamplingInformation sample = samplingInformationService.selectSamplingInformationById(sampleId);
            if (sample == null) {
                log.error("样本不存在, ID: {}", sampleId);
                return AjaxResult.error("样本不存在");
            }

            // 创建使用记录
            SampleUsageRecord record = new SampleUsageRecord();
            record.setSampleId(sampleId);
            record.setUsedBy(username);

            // 处理用样时间 - 适配 DATE 类型
            Date usedTime = null;
            if (usedTimeStr != null && !usedTimeStr.trim().isEmpty()) {
                try {
                    // 解析前端传递的用样时间，只处理日期部分
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    usedTime = sdf.parse(usedTimeStr);
                    log.info("使用用户选择的用样时间: {}", usedTime);
                } catch (Exception e) {
                    log.warn("用样时间格式错误，使用当前日期: {}", usedTimeStr, e);
                    // 使用当前日期
                    usedTime = new Date();
                }
            } else {
                // 如果没有提供用样时间，使用当前日期
                usedTime = new Date();
                log.info("使用当前日期作为用样时间: {}", usedTime);
            }
            record.setUsedTime(usedTime);
            record.setPurpose(purpose);

            // 设置从采样信息表获取的基础信息
            record.setSampleCode(sample.getSampleName());
            record.setHost(sample.getHost());
            record.setSampleType(sample.getSampleType());
            record.setSamplingLocation(sample.getLocation());
            record.setHabitatType(sample.getHabitatType());
            record.setLongitude(sample.getLongitude());
            record.setLatitude(sample.getLatitude());
            record.setSamplingTime(sample.getSamplingTime()); // 这里直接从数据库获取
            record.setSampler(sample.getSamplingPerson());
            record.setStorageLocation(sample.getStorageLocation());

            log.info("创建使用记录: {}", record);
            int insertResult = sampleUsageRecordService.insertSampleUsageRecord(record);
            log.info("创建使用记录结果: {}", insertResult);

            if (insertResult <= 0) {
                log.error("创建使用记录失败");
                return AjaxResult.error("创建使用记录失败");
            }

            log.info("===== 用样处理成功完成 =====");
            return AjaxResult.success("用样记录创建成功");
        } catch (Exception e) {
            log.error("===== 用样处理失败 =====", e);
            return AjaxResult.error("用样失败: " + e.getMessage());
        }
    }

    /**
     * 批量用样操作 - 适配 DATE 类型字段
     */
    @PreAuthorize("@ss.hasPermi('system:information:batchUse')")
    @Log(title = "批量样本使用", businessType = BusinessType.UPDATE)
    @PostMapping("/batchUseSample")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult batchUseSample(@RequestBody Map<String, Object> requestData) {
        try {
            log.info("===== 开始处理批量用样请求 =====");
            log.info("请求数据: {}", requestData);

            // 提取请求数据
            Object sampleIdsObj = requestData.get("sampleIds");
            String purpose = (String) requestData.get("purpose");
            String usedTimeStr = (String) requestData.get("usedTime");

            // 验证必要参数
            if (sampleIdsObj == null) {
                log.error("缺少必要参数: sampleIds");
                return AjaxResult.error("缺少样本ID列表");
            }
            if (purpose == null || purpose.trim().isEmpty()) {
                log.error("缺少必要参数: purpose");
                return AjaxResult.error("缺少用途说明");
            }

            // 处理样本ID列表
            List<Long> sampleIds = new ArrayList<>();
            if (sampleIdsObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<Object> idList = (List<Object>) sampleIdsObj;
                for (Object idObj : idList) {
                    try {
                        if (idObj instanceof Integer) {
                            sampleIds.add(((Integer) idObj).longValue());
                        } else if (idObj instanceof Long) {
                            sampleIds.add((Long) idObj);
                        } else if (idObj instanceof String) {
                            sampleIds.add(Long.parseLong((String) idObj));
                        } else if (idObj instanceof Number) {
                            sampleIds.add(((Number) idObj).longValue());
                        }
                    } catch (NumberFormatException e) {
                        log.error("样本ID格式错误: {}", idObj, e);
                    }
                }
            } else {
                log.error("sampleIds 参数类型错误: {}", sampleIdsObj.getClass().getName());
                return AjaxResult.error("样本ID列表格式错误");
            }

            if (sampleIds.isEmpty()) {
                log.error("样本ID列表为空或格式错误");
                return AjaxResult.error("样本ID列表为空或格式错误");
            }

            // 获取当前登录用户
            String username = SecurityUtils.getUsername();
            log.info("当前操作用户: {}, 批量处理样本数量: {}", username, sampleIds.size());

            // 处理用样时间 - 适配 DATE 类型
            Date usedTime = null;
            if (usedTimeStr != null && !usedTimeStr.trim().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    usedTime = sdf.parse(usedTimeStr);
                    log.info("批量用样使用用户选择的时间: {}", usedTime);
                } catch (Exception e) {
                    log.warn("批量用样时间格式错误，使用当前日期: {}", usedTimeStr, e);
                    usedTime = new Date();
                }
            } else {
                usedTime = new Date();
                log.info("批量用样使用当前日期: {}", usedTime);
            }

            int successCount = 0;
            List<String> errorMessages = new ArrayList<>();

            // 批量处理每个样本
            for (Long sampleId : sampleIds) {
                try {
                    SamplingInformation sample = samplingInformationService.selectSamplingInformationById(sampleId);
                    if (sample == null) {
                        errorMessages.add("样本ID " + sampleId + " 不存在");
                        continue;
                    }

                    SampleUsageRecord record = new SampleUsageRecord();
                    record.setSampleId(sampleId);
                    record.setUsedBy(username);
                    record.setUsedTime(usedTime);
                    record.setPurpose(purpose);

                    // 设置从采样信息表获取的基础信息
                    record.setSampleCode(sample.getSampleName());
                    record.setHost(sample.getHost());
                    record.setSampleType(sample.getSampleType());
                    record.setSamplingLocation(sample.getLocation());
                    record.setHabitatType(sample.getHabitatType());
                    record.setLongitude(sample.getLongitude());
                    record.setLatitude(sample.getLatitude());
                    record.setSamplingTime(sample.getSamplingTime());
                    record.setSampler(sample.getSamplingPerson());
                    record.setStorageLocation(sample.getStorageLocation());

                    log.info("为样本 {} 创建使用记录", sample.getSampleName());
                    int insertResult = sampleUsageRecordService.insertSampleUsageRecord(record);

                    if (insertResult > 0) {
                        successCount++;
                    } else {
                        errorMessages.add("样本 " + sample.getSampleName() + " 创建使用记录失败");
                    }
                } catch (Exception e) {
                    log.error("处理样本ID {} 时发生错误: {}", sampleId, e.getMessage());
                    errorMessages.add("样本ID " + sampleId + " 处理失败: " + e.getMessage());
                }
            }

            log.info("===== 批量用样处理完成 =====");
            log.info("成功处理: {} 个, 失败: {} 个", successCount, errorMessages.size());

            if (!errorMessages.isEmpty()) {
                String errorMsg = "成功处理 " + successCount + " 个样本，失败 " + errorMessages.size() + " 个。失败原因：" +
                        String.join("; ", errorMessages.subList(0, Math.min(errorMessages.size(), 3)));
                if (errorMessages.size() > 3) {
                    errorMsg += " ...等";
                }
                return AjaxResult.error(errorMsg);
            }

            return AjaxResult.success("成功为 " + successCount + " 个样本添加用样记录");
        } catch (Exception e) {
            log.error("===== 批量用样处理失败 =====", e);
            return AjaxResult.error("批量用样失败: " + e.getMessage());
        }
    }


    /**
     * 导入采样信息数据
     */
    @PreAuthorize("@ss.hasPermi('system:information:import')")
    @Log(title = "采样信息", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SamplingInformation> util = new ExcelUtil<SamplingInformation>(SamplingInformation.class);
        List<SamplingInformation> samplingInformationList = util.importExcel(file.getInputStream());
        String operName = SecurityUtils.getUsername();
        String message = samplingInformationService.importSamplingInformation(samplingInformationList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    /**
     * 下载导入模板
     */
    @PreAuthorize("@ss.hasPermi('system:information:import')")
    @GetMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<SamplingInformation> util = new ExcelUtil<SamplingInformation>(SamplingInformation.class);
        util.importTemplateExcel(response, "采样信息数据");
    }


    /**
     * 获取样本的使用记录
     */
    @PreAuthorize("@ss.hasPermi('system:information:query')")
    @GetMapping("/usageRecords/{sampleId}")
    public AjaxResult getUsageRecords(@PathVariable("sampleId") Long sampleId) {
        try {
            SampleUsageRecord query = new SampleUsageRecord();
            query.setSampleId(sampleId);
            List<SampleUsageRecord> records = sampleUsageRecordService.selectSampleUsageRecordList(query);
            return success(records);
        } catch (Exception e) {
            log.error("获取样本使用记录失败: {}", sampleId, e);
            return error("获取使用记录失败");
        }
    }

    // 辅助方法：对象转Long
    private Long convertToLong(Object value) {
        if (value == null) return null;

        try {
            if (value instanceof Long) {
                return (Long) value;
            } else if (value instanceof Integer) {
                return ((Integer) value).longValue();
            } else if (value instanceof Double) {
                return ((Double) value).longValue();
            } else if (value instanceof Float) {
                return ((Float) value).longValue();
            } else if (value instanceof String) {
                return Long.parseLong((String) value);
            } else if (value instanceof Number) {
                return ((Number) value).longValue();
            } else {
                log.warn("无法转换的类型: {} - {}", value.getClass().getName(), value);
                return null;
            }
        } catch (NumberFormatException e) {
            log.warn("转换Long失败: {}", value, e);
            return null;
        }
    }
}