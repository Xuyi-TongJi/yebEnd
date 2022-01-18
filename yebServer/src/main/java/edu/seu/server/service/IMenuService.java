package edu.seu.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.seu.server.pojo.Menu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 根据用户id查询该用户id对应的菜单列表
     * @return 查询得到的Menu实体类列表
     */
    List<Menu> getMenusByAdminId();

    /**
     * 根据角色获取该角色能够访问的菜单列表
     * @return 查询得到的Menu实体类列表,该List中的Menu实体类重的roles属性即为具有访问该菜单权限的角色实体列表(List(Role))
     *         可以通过Menu::getRoles获得
     */
    List<Menu> getMenusWithRole();
}
