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
import com.ruoyi.system.domain.GroupMeeting;
import com.ruoyi.system.service.IGroupMeetingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 大组会安排Controller
 * 
 * @author ruoyi
 * @date 2026-01-12
 */
@RestController
@RequestMapping("/system/groupmeeting")
public class GroupMeetingController extends BaseController
{
    @Autowired
    private IGroupMeetingService groupMeetingService;

    /**
     * 查询大组会安排列表
     */
    @PreAuthorize("@ss.hasPermi('system:groupmeeting:list')")
    @GetMapping("/list")
    public TableDataInfo list(GroupMeeting groupMeeting)
    {
        startPage();
        List<GroupMeeting> list = groupMeetingService.selectGroupMeetingList(groupMeeting);
        return getDataTable(list);
    }

    /**
     * 导出大组会安排列表
     */
    @PreAuthorize("@ss.hasPermi('system:groupmeeting:export')")
    @Log(title = "大组会安排", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GroupMeeting groupMeeting)
    {
        List<GroupMeeting> list = groupMeetingService.selectGroupMeetingList(groupMeeting);
        ExcelUtil<GroupMeeting> util = new ExcelUtil<GroupMeeting>(GroupMeeting.class);
        util.exportExcel(response, list, "大组会安排数据");
    }

    /**
     * 获取大组会安排详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:groupmeeting:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(groupMeetingService.selectGroupMeetingById(id));
    }

    /**
     * 新增大组会安排
     */
    @PreAuthorize("@ss.hasPermi('system:groupmeeting:add')")
    @Log(title = "大组会安排", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GroupMeeting groupMeeting)
    {
        return toAjax(groupMeetingService.insertGroupMeeting(groupMeeting));
    }

    /**
     * 修改大组会安排
     */
    @PreAuthorize("@ss.hasPermi('system:groupmeeting:edit')")
    @Log(title = "大组会安排", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GroupMeeting groupMeeting)
    {
        return toAjax(groupMeetingService.updateGroupMeeting(groupMeeting));
    }

    /**
     * 删除大组会安排
     */
    @PreAuthorize("@ss.hasPermi('system:groupmeeting:remove')")
    @Log(title = "大组会安排", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(groupMeetingService.deleteGroupMeetingByIds(ids));
    }
}
