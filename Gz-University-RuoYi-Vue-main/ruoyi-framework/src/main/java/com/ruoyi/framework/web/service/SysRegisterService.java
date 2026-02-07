package com.ruoyi.framework.web.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.CaptchaExpireException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.mapper.SysUserRoleMapper;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;

/**
 * 注册校验方法
 * * @author ruoyi
 */
@Component
public class SysRegisterService
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private RedisCache redisCache;

    // 【新增】注入角色关联Mapper，用于给新用户赋予“待审核”角色
    @Autowired
    private SysUserRoleMapper userRoleMapper;

    /**
     * 注册
     */
    public String register(RegisterBody registerBody)
    {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);

        // 验证码开关
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled)
        {
            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        }

        if (StringUtils.isEmpty(username))
        {
            msg = "用户名不能为空";
        }
        else if (StringUtils.isEmpty(password))
        {
            msg = "用户密码不能为空";
        }
        else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            msg = "账户长度必须在2到20个字符之间";
        }
        else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            msg = "密码长度必须在5到20个字符之间";
        }
        else if (!userService.checkUserNameUnique(sysUser))
        {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        }
        else
        {
            // 基础信息
            sysUser.setNickName(registerBody.getNickName()); // 使用真实姓名
            sysUser.setPwdUpdateDate(DateUtils.getNowDate());
            sysUser.setPassword(SecurityUtils.encryptPassword(password));

            // 【关键修改 1】设置状态为 0 (正常)，允许登录
            sysUser.setStatus("0");
            // 设置审核状态为 0 (待审核)
            sysUser.setAuditStatus("0");

            // 【保存扩展字段】
            sysUser.setSex(registerBody.getSex());
            sysUser.setEthnicity(registerBody.getEthnicity());
            sysUser.setPhonenumber(registerBody.getPhonenumber());
            sysUser.setEmail(registerBody.getEmail());
            sysUser.setAddress(registerBody.getAddress());
            sysUser.setStudentId(registerBody.getStudentId());
            sysUser.setEnrollmentYear(registerBody.getEnrollmentYear());
            sysUser.setDegreeType(registerBody.getDegreeType());
            sysUser.setMajor(registerBody.getMajor());
            sysUser.setCurrentStatus(registerBody.getCurrentStatus());
            sysUser.setUndergradUniv(registerBody.getUndergradUniv());
            sysUser.setMasterUniv(registerBody.getMasterUniv());
            sysUser.setEmployer(registerBody.getEmployer());
            sysUser.setEmergencyContact(registerBody.getEmergencyContact());
            sysUser.setEmergencyRelation(registerBody.getEmergencyRelation());
            sysUser.setEmergencyPhone(registerBody.getEmergencyPhone());

            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag)
            {
                msg = "注册失败,请联系系统管理人员";
            }
            else
            {
                // 【关键修改 2】给新用户赋予“待审核用户”角色
                // 假设“待审核用户”的角色ID是 100 (请务必去数据库确认这个ID)
                Long defaultRoleId = 106L;

                List<SysUserRole> list = new ArrayList<>();
                SysUserRole ur = new SysUserRole();
                ur.setUserId(sysUser.getUserId());
                ur.setRoleId(defaultRoleId);
                list.add(ur);
                userRoleMapper.batchUserRole(list);

                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success")));
            }
        }
        return msg;
    }

    /**
     * 校验验证码
     */
    public void validateCaptcha(String username, String code, String uuid)
    {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null)
        {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException();
        }
    }
}