package edu.seu.server.config.security.component;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 用户权限拦截器，主要用于判断用户的角色是否能够访问该url
 * @author xuyitjuseu
 */
@Component
public class UrlDecisionManager implements AccessDecisionManager {

    /**
     * 该权限控制器的核心方法，该方法可用于判断用户是否登录，该用户所具有的角色是否有访问该url的权限
     * @param authentication Spring Security权限对象，一旦用户登录，该权限对象可用于获得该用户所具有的角色，用户没有登录，则该类可以
     *                       转为(instance of)AnonymousAuthenticationToken
     * @param object 该业务逻辑无需用到此对象
     * @param configAttributes configAttributes为ConfigAttribute类集合，该类来自于SecurityConfig::createList方法，即来自于
     *                         RoleAuthorizationFilter拦截器
     * @throws AccessDeniedException 当访问拒绝时，抛出该异常，本业务逻辑中，当监测到用户未登录时，抛出该异常
     * @throws InsufficientAuthenticationException 当权限不足时，抛出该异常，如果用户所具有的角色不包含在访问该url所需要的角色中，
     *                                             抛出该异常
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        for (ConfigAttribute configAttribute : configAttributes) {
            // 当前url所需角色
            String needRole = configAttribute.getAttribute();
            // 判断needRole是否为登录即可访问的角色"ROLE_LOGIN"（在RoleAuthorizationFilter中设置）
            if ("ROLE_LOGIN".equals(needRole)) {
                // 判断是否登录
                if (authentication instanceof AnonymousAuthenticationToken) {
                    throw new AccessDeniedException("尚未登录，请登录！");
                } else {
                    return;
                }
            }
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority: authorities) {
                // 当前用户具有访问该url所需要的角色
                if (authority.getAuthority().equals(needRole)) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("您的权限不足！");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}
