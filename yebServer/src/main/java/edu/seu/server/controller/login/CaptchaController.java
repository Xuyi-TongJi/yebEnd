package edu.seu.server.controller.login;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 用于生成验证码的控制器
 * @author xuyitjuseu
 */
@RestController
public class CaptchaController {

    private final DefaultKaptcha defaultKaptcha;

    public CaptchaController(DefaultKaptcha defaultKaptcha) {
        this.defaultKaptcha = defaultKaptcha;
    }

    /**
     * 生成验证码的请求
     * @param request httpServletRequest对象，用以获得Session
     * @param response httpServletResponse对象，用以输出图片
     */
    @ApiOperation(value = "获取验证码", produces = "jpg")
    @GetMapping(value = "/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        // response输出image时的配置
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, mustrevalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        // 生成验证码
        String text = defaultKaptcha.createText();
        // 将验证码文本放入Session
        request.getSession().setAttribute("captcha", text);
        BufferedImage image = defaultKaptcha.createImage(text);
        // 使用response的输出流输出image
        ServletOutputStream sos = null;
        try {
            sos = response.getOutputStream();
            ImageIO.write(image, "jpg", sos);
            sos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != sos) {
                try {
                    sos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
