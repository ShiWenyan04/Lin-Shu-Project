package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.core.domain.model.LoginUser;
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
import com.ruoyi.system.domain.GroupMeetingRecord;
import com.ruoyi.system.service.IGroupMeetingRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * 大组会实施记录页Controller
 * @author ruoyi
 * @date 2026-01-10
 */
@RestController
@RequestMapping("/system/group_meeting_record")
public class GroupMeetingRecordController extends BaseController
{
    @Autowired
    private IGroupMeetingRecordService groupMeetingRecordService;

    /**
     * 查询大组会实施记录页列表
     */
    @PreAuthorize("@ss.hasPermi('system:group_meeting_record:list')")
    @GetMapping("/list")
    public TableDataInfo list(GroupMeetingRecord groupMeetingRecord)
    {
        // 获取请求参数中的 queryMode
        Map<String, Object> params = groupMeetingRecord.getParams();
        String queryMode = (String) params.get("queryMode");

        if (queryMode == null) {
            queryMode = "public";
        }

        // --- 核心权限过滤逻辑 ---
        if ("private".equals(queryMode)) {
            // 【我的提交】：只看自己创建的
            // 配合 XML 中的 create_by = #{createBy} 生效
            groupMeetingRecord.setCreateBy(SecurityUtils.getUsername());
        }
        else if ("audit".equals(queryMode)) {
            // 【审核管理】：只看待审核的 (状态为0)
            groupMeetingRecord.setAuditStatus("0");
            // ⚠️ 关键：审核模式不能设置 CreateBy，否则老师只能审自己的
            groupMeetingRecord.setCreateBy(null);
        }
        else {
            // 【全部记录 (public)】：所有人只能看到已通过的 (状态为1)
            groupMeetingRecord.setAuditStatus("1");
            groupMeetingRecord.setCreateBy(null);
        }

        startPage();
        List<GroupMeetingRecord> list = groupMeetingRecordService.selectGroupMeetingRecordList(groupMeetingRecord);
        return getDataTable(list);
    }

    /**
     * 导出
     */
    @PreAuthorize("@ss.hasPermi('system:group_meeting_record:export')")
    @Log(title = "大组会实施记录页", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GroupMeetingRecord groupMeetingRecord)
    {
        // 导出逻辑：如果是老师或负责人，可以导出全部；否则（学生）只能导出已通过的
        boolean isTeacher = SecurityUtils.hasRole("teacher") || SecurityUtils.hasRole("admin");
        boolean isManager = SecurityUtils.hasRole("meetingImplementation_manager");

        if (!isTeacher && !isManager) {
            groupMeetingRecord.setAuditStatus("1");
        }

        List<GroupMeetingRecord> list = groupMeetingRecordService.selectGroupMeetingRecordList(groupMeetingRecord);
        ExcelUtil<GroupMeetingRecord> util = new ExcelUtil<GroupMeetingRecord>(GroupMeetingRecord.class);
        util.exportExcel(response, list, "大组会实施记录页数据");
    }

    /**
     * 获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:group_meeting_record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(groupMeetingRecordService.selectGroupMeetingRecordById(id));
    }

    /**
     * 新增
     */
    @PreAuthorize("@ss.hasPermi('system:group_meeting_record:add')")
    @Log(title = "大组会实施记录页", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GroupMeetingRecord groupMeetingRecord)
    {
        // 设置创建人，方便"我的提交"查询
        groupMeetingRecord.setCreateBy(SecurityUtils.getUsername());
        return toAjax(groupMeetingRecordService.insertGroupMeetingRecord(groupMeetingRecord));
    }

    /**
     * 修改
     */
    @PreAuthorize("@ss.hasPermi('system:group_meeting_record:edit')")
    @Log(title = "大组会实施记录页", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GroupMeetingRecord groupMeetingRecord)
    {
        groupMeetingRecord.setUpdateBy(SecurityUtils.getUsername());
        // Service层已处理：如果当前是驳回状态，会自动重置为待审核
        return toAjax(groupMeetingRecordService.updateGroupMeetingRecord(groupMeetingRecord));
    }

    /**
     * 删除
     */
    @PreAuthorize("@ss.hasPermi('system:group_meeting_record:remove')")
    @Log(title = "大组会实施记录页", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(groupMeetingRecordService.deleteGroupMeetingRecordByIds(ids));
    }

    /**
     * 审核接口
     */
    @PreAuthorize("@ss.hasPermi('system:group_meeting_record:audit')")
    @Log(title = "大组会实施记录页审核", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody GroupMeetingRecord groupMeetingRecord)
    {
        // 审核只更新状态和原因
        groupMeetingRecord.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(groupMeetingRecordService.auditGroupMeetingRecord(groupMeetingRecord));
    }

    /**
     * 本地资源下载
     */
    @GetMapping("/common/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String localPath = RuoYiConfig.getUploadPath();
        String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
        String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, downloadName));
        FileUtils.writeBytes(downloadPath, response.getOutputStream());
    }
}