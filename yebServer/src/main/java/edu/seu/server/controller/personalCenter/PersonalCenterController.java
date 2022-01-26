package edu.seu.server.controller.personalCenter;

import edu.seu.server.common.lang.ResponseBean;
import edu.seu.server.common.vo.AdminUpdateVo;
import edu.seu.server.pojo.Admin;
import edu.seu.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dozer.Mapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 个人中心前端控制器
 * @author xuyitjuseu
 */
@RestController
@RequestMapping("/personalCenter")
@Api(tags = "personalCenterController")
public class PersonalCenterController {

    private final IAdminService adminService;
    private final Mapper mapper;

    public PersonalCenterController(IAdminService adminService,
                                    Mapper mapper) {
        this.adminService = adminService;
        this.mapper = mapper;
    }

    @ApiOperation("更新除用户名密码以外的账号信息")
    @PutMapping("/info")
    public ResponseBean updateAdminInfo(@Validated @RequestBody AdminUpdateVo adminVo, Authentication authentication) {
        // 校验是否操作自己的账号
        Integer loginId = ((Admin)authentication.getPrincipal()).getId();
        if (!loginId.equals(adminVo.getId())) {
            return ResponseBean.error(403, "非法操作", null);
        }
        Admin admin = mapper.map(adminVo, Admin.class);
        admin.setEnabled(adminVo.getEnabled());
        admin.getAuthorities();
        if (adminService.updateById(admin)) {
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(admin, null, authentication.getAuthorities()));
            return ResponseBean.success("更新成功！", adminVo);
        } else {
            return ResponseBean.error(500, "更新失败", adminVo);
        }
    }

    /**
     * 更新用户密码，需要验证当前密码
     * @param passwordMap 旧密码键必须为current-password, 新密码键必须为new-password，用户id键为aid
     * @return 公共返回对象
     */
    @ApiOperation("更新用户密码")
    @PutMapping("/password")
    public ResponseBean updatePassword(@RequestBody Map<String, String> passwordMap, Authentication authentication) {
        Integer loginAid = ((Admin)authentication.getPrincipal()).getId();
        Integer aid = Integer.parseInt(passwordMap.get("aid"));
        if (!loginAid.equals(aid)) {
            return ResponseBean.error(403, "非法操作！", null);
        }
        String currentPassword = passwordMap.get("current-password");
        String newPassword = passwordMap.get("new-password");
        Integer result = adminService.updatePassword(currentPassword, newPassword, aid);
        if (result == 1) {
            return ResponseBean.success("更新成功,请重新登录!", null);
        } else if (result == -1){
            return ResponseBean.error(500, "密码错误！", null);
        } else {
            return ResponseBean.error(500, "更新失败！", null);
        }
    }

}
