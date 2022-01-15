package edu.seu.server.controller;


import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/test")
    public String test() {
        return "hello, world";
    }

    /**
     * 需要abc角色才能访问
     * @return 测试字符串
     */
    @Secured("/ROLE_abc")
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/toMain")
    public String toMain() {
        return "toMain";
    }
}