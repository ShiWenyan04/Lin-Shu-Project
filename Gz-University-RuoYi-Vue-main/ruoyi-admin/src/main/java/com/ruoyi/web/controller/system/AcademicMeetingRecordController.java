package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils; // 必须引入
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.AcademicMeetingRecord;
import com.ruoyi.system.service.IAcademicMeetingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 参会记录Controller
 *
 * @author ruoyi
 * @date 2025-12-23
 */
@RestController
@RequestMapping("/system/meeting_record")
public class AcademicMeetingRecordController extends BaseController {
    @Autowired
    private IAcademicMeetingRecordService academicMeetingRecordService;

    /**
     * 查询参会记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:meeting_record:list')")
    @GetMapping("/list")
    public TableDataInfo list(AcademicMeetingRecord academicMeetingRecord) {
        Map<String, Object> params = academicMeetingRecord.getParams();
        String queryMode = (String) params.get("queryMode");

        // 1. 私有模式 (我的提交)
        if ("private".equals(queryMode)) {
            academicMeetingRecord.setCreateBy(SecurityUtils.getUsername());
            params.put("currentUserNickName", SecurityUtils.getLoginUser().getUser().getNickName());
        }
        // 2. 【新增】审核/管理模式
        else if ("audit".equals(queryMode)) {
            // 权限校验：老师、参会记录负责人、管理员
            if (!SecurityUtils.hasRole("teacher")
                    && !SecurityUtils.hasRole("meetingrecord_manager")
                    && !SecurityUtils.hasRole("admin")) {
                return getDataTable(new java.util.ArrayList<>());
            }

            // 过滤逻辑：
            // 如果是 负责人 或 管理员，看所有状态
            // 如果仅是 老师，只看待审核 '0'
            if (!SecurityUtils.hasRole("meetingrecord_manager") && !SecurityUtils.hasRole("admin")) {
                academicMeetingRecord.setAuditStatus("0");
            }

            // 确保能看所有人
            academicMeetingRecord.setCreateBy(null);
        }

        startPage();
        List<AcademicMeetingRecord> list = academicMeetingRecordService.selectAcademicMeetingRecordList(academicMeetingRecord);
        return getDataTable(list);
    }

    /**
     * 导出参会记录列表
     * 同样也要应用 list 的过滤逻辑
     */
    @PreAuthorize("@ss.hasPermi('system:meeting_record:export')")
    @Log(title = "参会记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AcademicMeetingRecord academicMeetingRecord) {
        // 复用同样的逻辑
        Map<String, Object> params = academicMeetingRecord.getParams();
        String queryMode = (String) params.get("queryMode");
        if ("private".equals(queryMode)) {
            academicMeetingRecord.setCreateBy(SecurityUtils.getUsername());
        }

        List<AcademicMeetingRecord> list = academicMeetingRecordService.selectAcademicMeetingRecordList(academicMeetingRecord);
        ExcelUtil<AcademicMeetingRecord> util = new ExcelUtil<AcademicMeetingRecord>(AcademicMeetingRecord.class);
        util.exportExcel(response, list, "参会记录数据");
    }

    /**
     * 获取参会记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:meeting_record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(academicMeetingRecordService.selectAcademicMeetingRecordById(id));
    }

    /**
     * 新增参会记录
     */
    @PreAuthorize("@ss.hasPermi('system:meeting_record:add')")
    @Log(title = "参会记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AcademicMeetingRecord academicMeetingRecord) {
        // Service层会自动处理 createBy 和 createTime
        return toAjax(academicMeetingRecordService.insertAcademicMeetingRecord(academicMeetingRecord));
    }

    /**
     * 修改参会记录
     */
    @PreAuthorize("@ss.hasPermi('system:meeting_record:edit')")
    @Log(title = "参会记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AcademicMeetingRecord academicMeetingRecord) {
        // Service层会自动处理 updateBy 和 updateTime
        return toAjax(academicMeetingRecordService.updateAcademicMeetingRecord(academicMeetingRecord));
    }

    /**
     * 删除参会记录
     */
    @PreAuthorize("@ss.hasPermi('system:meeting_record:remove')")
    @Log(title = "参会记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(academicMeetingRecordService.deleteAcademicMeetingRecordByIds(ids));
    }

    /**
     * 审核参会记录 (单个)
     * 修改：去掉了复杂的参数解析，直接用 Map 传给 Service
     */
    @PreAuthorize("@ss.hasPermi('system:meeting_record:audit')")
    @Log(title = "参会记录", businessType = BusinessType.UPDATE)
    @PostMapping("/audit") // 注意：建议使用 POST，或者保持前端一致
    public AjaxResult audit(@RequestBody Map<String, Object> params) {
        if (params.get("id") == null || params.get("auditStatus") == null) {
            return AjaxResult.error("参数缺失");
        }

        // 校验驳回原因
        String status = (String) params.get("auditStatus");
        String reason = (String) params.get("auditReason");
        if ("2".equals(status) && (reason == null || reason.trim().isEmpty())) {
            return AjaxResult.error("驳回时必须填写驳回原因");
        }

        return toAjax(academicMeetingRecordService.auditAcademicMeetingRecord(params));
    }

    // 已删除 batchAudit 方法
}