package edu.seu.server.controller;

import edu.seu.server.common.dto.AdminLogin;
import edu.seu.server.common.lang.ResponseBean;
import edu.seu.server.util.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录控制器
 * @author xuyitjuseu
 */
@RestController
@Api(tags = "LoginController")
public class LoginController {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public LoginController(UserDetailsService userDetailsService,
                           PasswordEncoder passwordEncoder,
                           JwtTokenUtil jwtTokenUtil) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * 配合SpringSecurity的UserDetailsService的loadByUsername方法进行用户登录的服务，如果登录成功，则需要生成相应令牌并返回给前端
     * @param adminLogin admin的登录dto,@RequestBody注解必须使用JSON格式，不能使用x-www-form-urlencoded
     * @param request HttpServletRequest，用于验证验证码时获取Session中存储的验证码
     * @return ResponseBean：登录成功，返回带有tokenMap的公共返回对象；登录失败，返回失败的相应原因
     */
    @ApiOperation(value = "登录之后返回Token")
    @PostMapping("/login")
    public ResponseBean login(@RequestBody AdminLogin adminLogin, HttpServletRequest request) {
        // 校验验证码
        String codeText = (String) request.getSession().getAttribute("captcha");
        if (codeText == null || !codeText.equals(adminLogin.getCode())) {
            return ResponseBean.error(500, "验证码错误", null);
        }
        // 登录
        UserDetails userDetails = userDetailsService.loadUserByUsername(adminLogin.getUsername());
        if (null == userDetails || !passwordEncoder.matches(adminLogin.getPassword(), userDetails.getPassword())) {
            return ResponseBean.error(500, "用户名或密码错误!", null);
        } else if(!userDetails.isEnabled()) {
            return ResponseBean.error(500, "账号被禁用!", null);
        }
        // 更新spring security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 生成token,将token和tokenHead(yml中配置)放入map中作为obj返回
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, String> tokenMap = new HashMap<>(2);
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", jwtTokenUtil.getTokenHead());
        return ResponseBean.success("登录成功", tokenMap);
    }

    /**
     * 退出登录接口，一旦调用该接口，该接口直接返回200状态码，响应到前端后，前端将Cookie中的token删除，实现退出登录功能
     * @return 成功退出的状态码以及响应信息
     */
    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public ResponseBean logout() {
        return ResponseBean.success("注销成功！", null);
    }
}