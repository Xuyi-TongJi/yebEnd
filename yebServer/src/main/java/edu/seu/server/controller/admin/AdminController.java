package edu.seu.server.controller.admin;


import edu.seu.server.pojo.Admin;
import edu.seu.server.common.lang.ResponseBean;
import edu.seu.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spi.service.contexts.SecurityContext;

import java.security.Principal;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
@RestController
@RequestMapping("/system/admin")
@Api(tags = "AdminController")
public class AdminController {

    private final IAdminService adminService;

    public AdminController(IAdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * 获取当前登录的用户的信息
     * @param principal Spring Security对于当前登录用户的用户名信息的封装类
     * @return 包含当前登录用户信息的公共返回对象，如果null == principal，则返回错误状态码500
     */
    @ApiOperation(value = "获取当前登录的用户信息")
    @GetMapping("/info")
    public ResponseBean getAdminInfo(Principal principal) {
        if (null == principal) {
            return ResponseBean.error(500, "用户未登录", null);
        } else {
            String username = principal.getName();
            Admin admin = adminService.getAdminByUsername(username);
            // 密码不能返回给前端
            admin.setPassword(null);
            admin.setRoleList(adminService.getRoleListByAdminId(admin.getId()));
            return ResponseBean.success(null, admin);
        }
    }

    @ApiOperation(value = "获取所有操作员")
    @GetMapping("/")
    public List<Admin> getAdminList(@RequestParam String keywords) {
        return adminService.getAdminListByKeywords(keywords);
    }
}