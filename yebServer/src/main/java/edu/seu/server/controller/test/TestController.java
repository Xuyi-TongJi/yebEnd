package edu.seu.server.controller.test;

import edu.seu.server.util.OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 测试接口
 * @author xuyitjuseu
 */
@Slf4j
@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello";
    }

    @RequestMapping("/")
    public String index() {
        return "fileUpload";
    }

    @PostMapping("/submit")
    public String fileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String fileName = OssUtil.uploadImages(file, "test");
        request.setAttribute("fileName", fileName);
        return "success";
    }
}
