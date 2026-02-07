package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BusiCameraInstallMapper;
import com.ruoyi.system.domain.BusiCameraInstall;
import com.ruoyi.system.service.IBusiCameraInstallService;

/**
 * 红外相机安装项目Service业务层处理
 * * @author ruoyi
 * @date 2026-01-12
 */
@Service
public class BusiCameraInstallServiceImpl implements IBusiCameraInstallService
{
    @Autowired
    private BusiCameraInstallMapper busiCameraInstallMapper;

    @Override
    public BusiCameraInstall selectBusiCameraInstallById(Long id)
    {
        return busiCameraInstallMapper.selectBusiCameraInstallById(id);
    }

    @Override
    public List<BusiCameraInstall> selectBusiCameraInstallList(BusiCameraInstall busiCameraInstall)
    {
        return busiCameraInstallMapper.selectBusiCameraInstallList(busiCameraInstall);
    }

    /**
     * 新增：默认状态设为 0 (待审核)
     */
    @Override
    public int insertBusiCameraInstall(BusiCameraInstall busiCameraInstall)
    {
        // 设置创建时间
        busiCameraInstall.setCreateTime(DateUtils.getNowDate());

        // 【2. 核心修复：设置创建人为当前登录用户】
        // 如果没有这一行，数据库里的 create_by 就是空的，"我的提交"就查不到
        busiCameraInstall.setCreateBy(SecurityUtils.getUsername());

        // 强制设置为待审核
        busiCameraInstall.setAuditStatus("0");

        return busiCameraInstallMapper.insertBusiCameraInstall(busiCameraInstall);
    }

    /**
     * 修改：如果用户修改了信息，通常应该重置为待审核，或者保持原样。
     * 这里根据你描述的逻辑：“修改后再次成为待审核状态”。
     */
    @Override
    public int updateBusiCameraInstall(BusiCameraInstall busiCameraInstall)
    {
        busiCameraInstall.setUpdateTime(DateUtils.getNowDate());

        // 【核心修复】
        // 不要判断 == null，因为前端回传表单时会带着旧的状态值（比如"2"）。
        // 只要调用了这个修改接口，就强制重置为“待审核”，并清空驳回原因。
        busiCameraInstall.setAuditStatus("0");
        busiCameraInstall.setAuditReason("");

        return busiCameraInstallMapper.updateBusiCameraInstall(busiCameraInstall);
    }

    /**
     * 【新增】审核专用方法
     */
    @Override
    public int auditBusiCameraInstall(BusiCameraInstall busiCameraInstall) {
        // 只更新状态、原因和更新时间
        busiCameraInstall.setUpdateTime(DateUtils.getNowDate());
        return busiCameraInstallMapper.updateBusiCameraInstall(busiCameraInstall);
    }

    @Override
    public int deleteBusiCameraInstallByIds(Long[] ids)
    {
        return busiCameraInstallMapper.deleteBusiCameraInstallByIds(ids);
    }

    @Override
    public int deleteBusiCameraInstallById(Long id)
    {
        return busiCameraInstallMapper.deleteBusiCameraInstallById(id);
    }
}