package com.ruoyi.web.controller.common;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.ChineseCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.code.kaptcha.Producer;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.sign.Base64;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.system.service.ISysConfigService;

/**
 * 验证码操作处理
 *
 * @author ruoyi
 */
@RestController
public class CaptchaController
{
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysConfigService configService;
    /**
     * 生成验证码
     */
    @GetMapping("/captchaImage")
    public AjaxResult getCode(HttpServletResponse response) throws IOException, FontFormatException {
        AjaxResult ajax = AjaxResult.success();
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        ajax.put("captchaEnabled", captchaEnabled);
        if (!captchaEnabled)
        {
            return ajax;
        }

        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;

        //新验证码
        //图形验证码
        //SpecCaptcha captcha = new SpecCaptcha(130, 48, 4);
        //算术验证码
        ArithmeticCaptcha captcha;
        String code;

        captcha = new ArithmeticCaptcha(130, 48);
        //得到验证码的值
        code = captcha.text();
        //若为负数则重新生成
        int i = Integer.parseInt(code);
        while (i < 0){
            System.out.println("code = " + code + "，负数，重新生成！！！！！！！！");
            captcha = new ArithmeticCaptcha(130, 48);
            code = captcha.text();
            i = Integer.parseInt(code);
            System.out.println("i = " + i + "，新值！！！");
        }
        // 设置内置字体
        captcha.setFont(Captcha.FONT_1);
        //captcha.setLen(2);  // 几位数运算，默认是两位
        //captcha.getArithmeticString();  // 获取运算的公式
        //存入redis
        System.out.println("准备存入Redis的验证码文本: " + code);
        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);

        //旧验证码
        //String capStr = null, code = null;
        //BufferedImage image = null;
        //
        //// 生成验证码
        //String captchaType = RuoYiConfig.getCaptchaType();
        //if ("math".equals(captchaType))
        //{
        //    String capText = captchaProducerMath.createText();
        //    capStr = capText.substring(0, capText.lastIndexOf("@"));
        //    code = capText.substring(capText.lastIndexOf("@") + 1);
        //    image = captchaProducerMath.createImage(capStr);
        //}
        //else if ("char".equals(captchaType))
        //{
        //    capStr = code = captchaProducer.createText();
        //    image = captchaProducer.createImage(capStr);
        //}
        //
        //redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        //// 转换流信息写出
        //FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        //try
        //{
        //    ImageIO.write(image, "jpg", os);
        //}
        //catch (IOException e)
        //{
        //    return AjaxResult.error(e.getMessage());
        //}
        ajax.put("uuid", uuid);
        ajax.put("img", captcha.toBase64());
        return ajax;
    }
}
