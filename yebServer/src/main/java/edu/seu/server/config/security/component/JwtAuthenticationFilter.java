package edu.seu.server.config.security.component;

import edu.seu.server.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Jwt登录授权过滤器，注入到SecurityConfig中并配置在HttpSecurity中
 * @author xuyitjuseu
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil,
                                   @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * 过滤器的拦截方法，用于判断是否存在请求题中有有效token但在后端的Spring Security权限对象未更新的情况，如果有，则更新权限对象
     * @param httpServletRequest http请求对象
     * @param httpServletResponse http响应对象
     * @param filterChain 过滤器链
     * @throws ServletException Servlet异常类，由doFilter方法抛出
     * @throws IOException IO异常类，由doFilter异常抛出
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authentication = httpServletRequest.getHeader(jwtTokenUtil.getTokenHeader());
        if (null != authentication && authentication.startsWith(jwtTokenUtil.getTokenHead())) {
            // 获取token字符串
            String token = authentication.substring(jwtTokenUtil.getTokenHead().length());
            String username = jwtTokenUtil.getUsernameByToken(token);
            // token存在用户名但没有登录
            if (null != username && null == SecurityContextHolder.getContext().getAuthentication()) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // 如果token有效，就更新spring security登录用户对象
                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                            = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
