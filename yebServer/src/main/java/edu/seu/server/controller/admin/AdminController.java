package edu.seu.server.controller.admin;


import edu.seu.server.common.lang.ResponseBean;
import edu.seu.server.common.vo.AdminUpdateVo;
import edu.seu.server.pojo.Admin;
import edu.seu.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dozer.Mapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * <p>
 *  操作员(管理员)前端控制器
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
    private final Mapper mapper;

    public AdminController(IAdminService adminService,
                           Mapper mapper) {
        this.adminService = adminService;
        this.mapper = mapper;
    }

    /**
     * 获取当前登录的用户的信息
     * @param principal Spring Security对于当前登录用户的用户名信息的封装类
     * @return 包含当前登录用户信息的公共返回对象，如果null == principal，则返回错误状态码500
     */
    @ApiOperation("获取当前登录的用户信息")
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

    @ApiOperation("获取所有操作员")
    @GetMapping("/")
    public List<Admin> getAdminList(@RequestParam String keywords) {
        return adminService.getAdminListByKeywords(keywords);
    }

    /**
     * 更新操作员信息，用户名，密码必须在个人中心进行更新，管理员也没有权限修改
     * @param adminUpdateVo 更新操作员使用的vo类
     * @return 公共返回对象
     */
    @ApiOperation("更新操作员信息")
    @PutMapping("/")
    public ResponseBean updateAdmin(@Validated @RequestBody AdminUpdateVo adminUpdateVo) {
        Admin admin = mapper.map(adminUpdateVo, Admin.class);
        admin.setEnabled(adminUpdateVo.getEnabled());
        if (adminService.updateById(admin)) {
            return ResponseBean.success("更新成功！", null);
        } else {
            return ResponseBean.error(500, "更新失败", null);
        }
    }

    @ApiOperation("根据id删除操作员")
    @DeleteMapping("/{id}")
    public ResponseBean deleteAdmin(@PathVariable Integer id) {
        if (adminService.removeById(id)) {
            return ResponseBean.success("删除成功！", null);
        } else {
            return ResponseBean.error(500, "删除失败！", null);
        }
    }

    @ApiOperation("更新操作员角色")
    @PutMapping("/{aid}")
    public ResponseBean updateAdminRole(@PathVariable Integer aid, @RequestBody Integer ... rIds) {
        int aidIllegal = -2;
        int ridIllegal = -1;
        if (adminService.updateAdminRole(aid, rIds) == aidIllegal) {
            return ResponseBean.error(500, "非法参数！", null);
        } else if (adminService.updateAdminRole(aid, rIds) == ridIllegal) {
            return ResponseBean.error(500, "更新失败！", null);
        } else {
            return ResponseBean.success("更新成功！", null);
        }
    }
}