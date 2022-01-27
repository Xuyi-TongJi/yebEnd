package edu.seu.server.config.security;

import edu.seu.server.config.security.component.JwtAuthenticationFilter;
import edu.seu.server.config.security.component.RoleAuthorizationFilter;
import edu.seu.server.config.security.component.UrlDecisionManager;
import edu.seu.server.config.security.component.UserDetailsServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security配置类
 *
 * @author xuyitjuseu
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private final RoleAuthorizationFilter roleAuthorizationFilter;
    private final UrlDecisionManager urlDecisionManager;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService,
                                 JwtAuthenticationFilter jwtAuthenticationFilter,
                                 AuthenticationEntryPoint authenticationEntryPoint,
                                 AccessDeniedHandler accessDeniedHandler,
                                 RoleAuthorizationFilter roleAuthorizationFilter,
                                 UrlDecisionManager urlDecisionManager,
                                 PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.roleAuthorizationFilter = roleAuthorizationFilter;
        this.urlDecisionManager = urlDecisionManager;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 配置AuthenticationManagerBuilder，重写configure方法
     * 功能：在登录时使用自定义的UserDetailsService
     * @param auth auth
     * @throws Exception 所有可能抛出的异常
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    /**
     * Spring Security核心配置，重写configure方法
     * @param http HttpSecurity核心配置对象
     * @throws Exception 所有可能抛出的异常
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭跨域请求伪造防护，因为有jwt，所以不存在跨域问题, 同时，基于token，也不需要session
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 所有请求（除了web放行的请求）都要经过认证
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                // 动态权限配置
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setAccessDecisionManager(urlDecisionManager);
                        o.setSecurityMetadataSource(roleAuthorizationFilter);
                        return o;
                    }
                });
        // 关闭缓存
        http.headers().cacheControl();
        // 添加jwt登录授权拦截器
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加访问拒绝结果返回（未登录，未授权）
        http
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint);
    }

    /**
     * 配置WebSecurity，主要用于放行某些请求，例如Swagger2文档请求，重写Configure方法
     * @param web WebSecurity配置对象
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                // swagger相关接口
                .antMatchers("/doc.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs/**")
                // 登录，登出接口
                .antMatchers("/login", "/logout")
                // 验证码服务接口
                .antMatchers("/captcha")
                // websocket服务接口
                .antMatchers("/ws/**")
                // 测试接口
                .antMatchers("/test/**");
    }
}