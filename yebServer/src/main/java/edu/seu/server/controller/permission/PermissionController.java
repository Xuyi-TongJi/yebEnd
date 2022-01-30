package edu.seu.server.controller.permission;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.seu.server.common.lang.ResponseBean;
import edu.seu.server.common.pojo.MenuPojo;
import edu.seu.server.pojo.MenuRole;
import edu.seu.server.pojo.Role;
import edu.seu.server.service.IMenuRoleService;
import edu.seu.server.service.IMenuService;
import edu.seu.server.service.IRoleService;
import edu.seu.server.util.FunctionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限控制器,用于对角色表进行操作，角色名称必须符合Spring Security规范
 * @author xuyitjuseu
 */
@Slf4j
@RestController
@RequestMapping("/system/basic/permission")
@Api(tags = "PermissionController")
public class PermissionController {

    private final IRoleService roleService;
    private final IMenuService menuService;
    private final IMenuRoleService menuRoleService;

    public PermissionController(IRoleService roleService,
                                IMenuService menuService,
                                IMenuRoleService menuRoleService) {
        this.roleService = roleService;
        this.menuService = menuService;
        this.menuRoleService = menuRoleService;
    }

    @ApiOperation("获取角色列表")
    @GetMapping("/")
    public List<Role> getRoleList() {
        return roleService.listInCache();
    }

    @ApiOperation("查询所有角色及其可以访问的菜单级联列表")
    @GetMapping("/roles")
    public List<Role> getRoleListWithMenus() {
        return roleService.getRoleWithMenus();
    }

    @ApiOperation("查询所有菜单及其子菜单")
    @GetMapping("/menus")
    public List<MenuPojo> getAllMenus() {
        return menuService.getAllMenus();
    }

    @ApiOperation("根据角色id查询菜单id")
    @GetMapping("/mid/{rid}")
    public List<Integer> getMidListWithRid(@PathVariable String rid) {
        return menuRoleService.list(new QueryWrapper<MenuRole>().eq("rid", rid))
                .stream().map(MenuRole::getMid).collect(Collectors.toList());
    }

    @ApiOperation("添加角色")
    @PostMapping("/")
    public ResponseBean addRole(@RequestBody @Validated Role role) {
        ResponseBean responseBean;
        if ((responseBean = FunctionUtil.validRoleName(role)) != null) {
            return responseBean;
        }
        FunctionUtil.addRoleNamePrefix(role);
        if (roleService.save(role)) {
            return ResponseBean.success("添加成功！", null);
        } else {
            return ResponseBean.error(500, "添加失败", null);
        }
    }

    @ApiOperation("删除角色")
    @DeleteMapping("/{rid}")
    public ResponseBean deleteRole(@PathVariable Integer rid) {
        if (roleService.removeById(rid)) {
            return ResponseBean.success("删除成功！", null);
        } else {
            return ResponseBean.error(500, "删除失败", null);
        }
    }

    @ApiOperation("批量删除")
    @DeleteMapping("/")
    public ResponseBean deleteRoleBatch(@RequestBody Integer ... rids) {
        if (roleService.removeByIds(Arrays.asList(rids))) {
            return ResponseBean.success("批量删除成功！", null);
        } else {
            return ResponseBean.error(500, "批量删除失败", null);
        }
    }

    @ApiOperation("更新角色")
    @PutMapping("/")
    public ResponseBean updateRole(@RequestBody @Validated Role role) {
        ResponseBean responseBean;
        if ((responseBean = FunctionUtil.validRoleName(role)) != null) {
            return responseBean;
        }
        FunctionUtil.addRoleNamePrefix(role);
        if (roleService.updateById(role)) {
            return ResponseBean.success("更新成功！", null);
        } else {
            return ResponseBean.error(500, "更新失败", null);
        }
    }

    @ApiOperation("根据角色rid更新角色菜单")
    @PutMapping("/{rid}")
    public ResponseBean updateMenuRole(@PathVariable Integer rid, @RequestBody Integer... mIds) {
        return menuRoleService.updateMenuRole(rid, mIds);
    }
}
