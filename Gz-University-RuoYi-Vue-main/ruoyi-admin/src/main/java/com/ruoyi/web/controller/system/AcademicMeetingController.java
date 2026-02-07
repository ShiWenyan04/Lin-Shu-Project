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
import com.ruoyi.system.domain.AcademicMeeting;
import com.ruoyi.system.service.IAcademicMeetingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 学术会议信息Controller
 * * @author ruoyi
 * @date 2025-12-23
 */
@RestController
@RequestMapping("/system/meeting")
public class AcademicMeetingController extends BaseController {
    @Autowired
    private IAcademicMeetingService academicMeetingService;

    /**
     * 查询学术会议信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:list')")
    @GetMapping("/list")
    public TableDataInfo list(AcademicMeeting academicMeeting) {
        startPage();
        List<AcademicMeeting> list = academicMeetingService.selectAcademicMeetingList(academicMeeting);
        return getDataTable(list);
    }

    /**
     * 导出学术会议信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:export')")
    @Log(title = "学术会议信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AcademicMeeting academicMeeting) {
        List<AcademicMeeting> list = academicMeetingService.selectAcademicMeetingList(academicMeeting);
        ExcelUtil<AcademicMeeting> util = new ExcelUtil<AcademicMeeting>(AcademicMeeting.class);
        util.exportExcel(response, list, "学术会议信息数据");
    }

    /**
     * 获取学术会议信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(academicMeetingService.selectAcademicMeetingById(id));
    }

    /**
     * 新增学术会议信息
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:add')")
    @Log(title = "学术会议信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AcademicMeeting academicMeeting) {
        try {
            return toAjax(academicMeetingService.insertAcademicMeeting(academicMeeting));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 修改学术会议信息
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:edit')")
    @Log(title = "学术会议信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AcademicMeeting academicMeeting) {
        try {
            return toAjax(academicMeetingService.updateAcademicMeeting(academicMeeting));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 删除学术会议信息
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:remove')")
    @Log(title = "学术会议信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(academicMeetingService.deleteAcademicMeetingByIds(ids));
    }

    // ====================== 新增接口 ======================

    /**
     * 报名
     * 修改点：manager -> academicmeeting_manager
     */
    @PreAuthorize("@ss.hasRole('student') || @ss.hasRole('academicmeeting_manager') || @ss.hasRole('admin')")
    @Log(title = "学术会议报名", businessType = BusinessType.INSERT)
    @PostMapping("/signup/{meetingId}")
    public AjaxResult studentSignup(@PathVariable("meetingId") Long meetingId) {
        return academicMeetingService.studentSignup(meetingId);
    }

    /**
     * 取消报名
     * 修改点：manager -> academicmeeting_manager
     */
    @PreAuthorize("@ss.hasRole('student') || @ss.hasRole('academicmeeting_manager') || @ss.hasRole('admin')")
    @Log(title = "学术会议取消报名", businessType = BusinessType.DELETE)
    @PostMapping("/cancelSignup/{meetingId}")
    public AjaxResult studentCancelSignup(@PathVariable("meetingId") Long meetingId) {
        return academicMeetingService.studentCancelSignup(meetingId);
    }
}