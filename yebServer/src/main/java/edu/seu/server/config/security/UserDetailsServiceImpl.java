package edu.seu.server.config.security;

import edu.seu.server.service.IAdminService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 自定义一个Spring Security的UserDetailsService登录服务类，并注入Spring容器中
 * 自定义UserDetailsService，需要重写loadByUsername方法
 * @author xuyitjuseu
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IAdminService adminService;

    public UserDetailsServiceImpl(IAdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminService.getAdminByUsername(username);
    }
}
