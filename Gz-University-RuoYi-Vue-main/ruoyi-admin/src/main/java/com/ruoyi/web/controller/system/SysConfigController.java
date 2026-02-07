package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
/**
 * 参数配置 信息操作处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController
{
    @Autowired
    private ISysConfigService configService;

    /**
     * 获取参数配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysConfig config)
    {
        startPage();
        List<SysConfig> list = configService.selectConfigList(config);
        return getDataTable(list);
    }

    @Log(title = "参数管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:config:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysConfig config)
    {
        List<SysConfig> list = configService.selectConfigList(config);
        ExcelUtil<SysConfig> util = new ExcelUtil<SysConfig>(SysConfig.class);
        util.exportExcel(response, list, "参数数据");
    }



    /**
     * 根据参数编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping(value = "/{configId}")
    public AjaxResult getInfo(@PathVariable Long configId)
    {
        return success(configService.selectConfigById(configId));
    }

    /**
     * 根据参数键名查询参数值
     */
    @GetMapping(value = "/configKey/{configKey}")
    public AjaxResult getConfigKey(@PathVariable String configKey)
    {
        return success(configService.selectConfigByKey(configKey));
    }

    /**
     * 新增参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysConfig config)
    {
        if (!configService.checkConfigKeyUnique(config))
        {
            return error("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setCreateBy(getUsername());
        return toAjax(configService.insertConfig(config));
    }

    /**
     * 修改参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysConfig config)
    {
        if (!configService.checkConfigKeyUnique(config))
        {
            return error("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setUpdateBy(getUsername());
        return toAjax(configService.updateConfig(config));
    }

    /**
     * 删除参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public AjaxResult remove(@PathVariable Long[] configIds)
    {
        configService.deleteConfigByIds(configIds);
        return success();
    }

    /**
     * 刷新参数缓存
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public AjaxResult refreshCache()
    {
        configService.resetConfigCache();
        return success();
    }

    private static final String LOGIN_BG_UPLOAD_PATH = "/profile/upload/login-bg/";

    /**
     * 上传登录背景图片
     */
    @PostMapping("/uploadLoginBg")
    public AjaxResult uploadLoginBg(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            // 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return AjaxResult.error("只能上传图片文件");
            }

            // 验证文件大小（2MB限制）
            long maxSize = 2 * 1024 * 1024; // 2MB
            if (file.getSize() > maxSize) {
                return AjaxResult.error("图片大小不能超过2MB");
            }

            // 验证文件扩展名
            String originalFilename = file.getOriginalFilename();
            String fileExt = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExt = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            }
            if (!fileExt.matches("^\\.(jpg|jpeg|png|gif|bmp)$")) {
                return AjaxResult.error("只支持 JPG、PNG、GIF 格式的图片");
            }

            // 生成文件名（使用日期+UUID避免重复）
            String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String fileName = uuid + fileExt;

            // 创建上传目录
            String uploadDir = System.getProperty("user.dir") + LOGIN_BG_UPLOAD_PATH + datePath;
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 保存文件
            File destFile = new File(dir, fileName);
            file.transferTo(destFile);

            // 返回相对路径
            String relativePath = LOGIN_BG_UPLOAD_PATH + datePath + "/" + fileName;

            Map<String, Object> data = new HashMap<>();
            data.put("fileName", fileName);
            data.put("filePath", relativePath);
            data.put("fullUrl", relativePath); // 相对路径，前端会拼接

            return AjaxResult.success("上传成功", data);

        } catch (IOException e) {
            logger.error("上传登录背景图失败", e);
            return AjaxResult.error("上传失败：" + e.getMessage());
        } catch (Exception e) {
            logger.error("上传登录背景图失败", e);
            return AjaxResult.error("上传失败");
        }
    }
}
