package edu.seu.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.server.common.pojo.MenuPojo;
import edu.seu.server.mapper.MenuMapper;
import edu.seu.server.pojo.Admin;
import edu.seu.server.pojo.Menu;
import edu.seu.server.service.IMenuService;
import edu.seu.server.util.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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
        String keyNamePrefix = RedisUtil.MENU_LIST_ADMIN_PREFIX;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer adminId = ((Admin) authentication.getPrincipal()).getId();
        List<Menu> menuList = (List<Menu>) redisTemplate.opsForValue().get(keyNamePrefix + adminId);
        if (CollectionUtils.isEmpty(menuList)) {
            menuList = menuMapper.getMenusByAdminId(adminId);
            redisTemplate.opsForValue().set("menu_" + adminId, menuList );
        }
        return menuList;
    }

    @Override
    public List<Menu> getMenusWithRole() {
        String key = RedisUtil.MENU_WITH_ROLE;
        List<Menu> menuListWithRole = (List<Menu>) redisTemplate.opsForValue().get(key);
        if (CollectionUtils.isEmpty(menuListWithRole)) {
            menuListWithRole = menuMapper.getMenusWithRole();
            redisTemplate.opsForValue().set(key, menuListWithRole);
        }
        return menuListWithRole;
    }

    @Override
    public List<MenuPojo> getAllMenus() {
        String key = RedisUtil.MENU_LIST_WITH_CHILDREN;
        List<MenuPojo> result = (List<MenuPojo>) redisTemplate.opsForValue().get(key);
        if (CollectionUtils.isEmpty(result)) {
            result = menuMapper.getAllMenus();
            redisTemplate.opsForValue().set(key, result);
        }
        return result;
    }

    @Override
    public List<Integer> getMidList() {
        String keyName = RedisUtil.MENU_ID_LIST;
        List<Integer> midList = (List<Integer>) redisTemplate.opsForValue().get(keyName);
        if (CollectionUtils.isEmpty(midList)) {
            midList = super.list().stream().filter(menu -> menu.getParentId() != null && menu.getParentId() != 1)
                    .map(Menu::getId).collect(Collectors.toList());
            redisTemplate.opsForValue().set(keyName, midList);
        }
        return midList;
    }

    @Override
    public void cleanupCache() {
        redisTemplate.delete(RedisUtil.MENU_ID_LIST);
        redisTemplate.delete(RedisUtil.MENU_LIST_ADMIN_PREFIX);
        redisTemplate.delete(RedisUtil.MENU_WITH_ROLE);
        redisTemplate.delete(RedisUtil.MENU_LIST_WITH_CHILDREN);
    }
}
