package edu.seu.server.config.security.component;

import edu.seu.server.pojo.Menu;
import edu.seu.server.pojo.Role;
import edu.seu.server.service.IMenuService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * 该拦截器用于根据访问的url判断访问该url需要什么角色
 * @author xuyitjuseu
 */
@Component
public class RoleAuthorizationFilter implements FilterInvocationSecurityMetadataSource {

    private final IMenuService menuService;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public RoleAuthorizationFilter(IMenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * 根据请求url获得请求该url需要的角色，可用于进一步判断访问该url的用户（当前登录用户）是否有权限访问该url
     * @param object 该对象实际上为FilterInvocation类，可以用于获取请求url
     * @return Spring Security角色（ROLE_xxx）集合
     * @throws IllegalArgumentException 抛出不合法参数异常
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        List<Menu> menus = menuService.getMenusWithRole();
        // 遍历Menu列表，判断请求url与菜单角色是否匹配
        for (Menu menu: menus) {
            // 如果匹配，则可以确定该url需要进行访问的是该Menu
            if (antPathMatcher.match(menu.getUrl(), requestUrl)) {
                // 获得与url匹配的menu所具有的权限对象
                String[] strings = (String[])menu.getRoles().stream()
                        .map((Function<Role, Object>) Role::getName).toArray(Object[]::new);
                // createList方法：创建了一个集合，该集合为访问该url所需要的所有角色的集合，在UrlDecisionManager拦截器中用于判断登录用
                // 户是否具有该角色
                return SecurityConfig.createList(strings);
            }
        }
        // 没匹配的url默认登录即可访问, ROLE_LOGIN是登录后就有的角色
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}
