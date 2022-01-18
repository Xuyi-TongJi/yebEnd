package edu.seu.server.service.impl;

import edu.seu.server.pojo.Admin;
import edu.seu.server.pojo.Menu;
import edu.seu.server.mapper.MenuMapper;
import edu.seu.server.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  菜单服务实现类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Resource
    private MenuMapper menuMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    public MenuServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 根据用户id获得菜单列表，菜单表和用户表为多对多关系，故需要使用级联查询,
     * 该方法为私有方法，用户id应从Spring Security安全框架中查得
     * <p>添加Redis缓存：以k-v形式存储</p>
     * @return 对应用户id的菜单实体类Menu列表
     */
    @Override
    public List<Menu> getMenusByAdminId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer adminId = ((Admin) authentication.getPrincipal()).getId();
        List<Menu> menuList
                = (List<Menu>) redisTemplate.opsForValue().get("menu_" + adminId);
        if (CollectionUtils.isEmpty(menuList)) {
            menuList = menuMapper.getMenusByAdminId(adminId);
            redisTemplate.opsForValue().set("menu_" + adminId, menuList );
        }
        return menuList;
    }

    @Override
    public List<Menu> getMenusWithRole() {
        return menuMapper.getMenusWithRole();
    }
}
