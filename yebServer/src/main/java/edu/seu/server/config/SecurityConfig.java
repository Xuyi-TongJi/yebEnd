package edu.seu.server.config;

import edu.seu.server.handler.MyAccessDeniedHandler;
import edu.seu.server.handler.MyAuthenticationFailureHandler;
import edu.seu.server.handler.MyAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * Security配置类
 *
 * @author xuyitjuseu
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MyAccessDeniedHandler myAccessDeniedHandler;
    private final DataSource dataSource;
    private final PersistentTokenRepository persistentTokenRepository;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(MyAccessDeniedHandler myAccessDeniedHandler, DataSource dataSource,
                          PersistentTokenRepository persistentTokenRepository, UserDetailsService userDetailsService) {
        this.myAccessDeniedHandler = myAccessDeniedHandler;
        this.dataSource = dataSource;
        this.persistentTokenRepository = persistentTokenRepository;
        this.userDetailsService = userDetailsService;
    }

    /**
     * 密码加密类
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder getPw() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置httpSecurity
     *
     * @param http httpSecurity类
     * @throws Exception 异常
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 表单提交
        http.formLogin()
                // 当发现/login时，认为是登录，必须和表单提交的地址相同，去执行UserDetailsServiceImpl
                .loginProcessingUrl("/login")
                // 自定义登录页面
                .loginPage("/login.html")
                /*// 登录成功，失败时跳转的页面,必须是Post请求,直接跳转为get请求，因此需要通过Controller实现重定向
                .successForwardUrl("/toMain")
                .failureForwardUrl("/toError");*/
                // successForwardUrl和successHandler不能共存，后者常用于前后端分离站外跳转
                .successHandler(new MyAuthenticationSuccessHandler("127.0.0.1:8080/main"))
                .failureHandler(new MyAuthenticationFailureHandler("127.0.0.1:8080/login"));
        // 授权认证
        http.authorizeRequests()
                // login.html和error.html不需要被认证
                .antMatchers("/error.html").permitAll()
                .antMatchers("/login.html").permitAll()
                // 放行所有静态资源
                .antMatchers("/js/**", "/css/**", "/images/**").permitAll()
                // 放行所有png
                .antMatchers("/**/*.png").permitAll()
                .antMatchers("/main.html").hasAuthority("admin")
                .antMatchers("/main1.html").hasAnyAuthority("admin", "normal")
                .antMatchers("/main2.html").hasAnyRole("a", "b")
                .antMatchers("/main.html").hasIpAddress("127.0.0.1")
                // 所有请求都必须被认证
                .anyRequest().authenticated();

        // 关闭csrf(跨站请求访问)保护
        http.csrf().disable();

        // 异常处理， 自定义403(权限不足)处理方式
        http.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);

        // 设置RememberMe功能
        http.rememberMe()
                // 自定义登录逻辑
                .userDetailsService(userDetailsService)
                // 持久层对象
                .tokenRepository(persistentTokenRepository);

        // 退出登录,也可以自定义logoutSuccessHandler,一般使用默认
        http.logout()
                .logoutUrl("/logout")
                // 退出登录跳转页面
                .logoutSuccessUrl("/login.html");
    }

    @Bean
    public PersistentTokenRepository getPersistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        // 自动建表存储用户信息，第一次启动时需要，第二次启动一定要注释
        jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }
}
