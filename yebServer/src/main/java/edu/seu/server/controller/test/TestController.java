package edu.seu.server.controller.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试接口
 * @author xuyitjuseu
 */
@RestController
public class TestController {

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/employee/basic/hello")
    public String testRoleUrl() {
        return "/employee/basic/hello";
    }

    @GetMapping("/employee/advanced/hello")
    public String testRoleUrl2() {
        return "/employee/advanced/hello";
    }
}
