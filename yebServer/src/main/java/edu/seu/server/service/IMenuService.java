package edu.seu.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.seu.server.common.pojo.MenuPojo;
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

    /**
     * 获取菜单表中所有菜单及其子菜单，需要进行自关联
     * @return 一级菜单列表，范型为Menu实体类，其中children属性为该Menu的子菜单
     */
    List<MenuPojo> getAllMenus();

    /**
     * 获取所有菜单的id列表，该菜单列表中的id只包含最后一级菜单，可用于对应于角色的权限管理
     * @return 菜单id列表
     */
    List<Integer> getMidList();
}
