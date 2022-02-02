package edu.seu.server.service;

import edu.seu.server.common.lang.ResponseBean;
import edu.seu.server.pojo.MenuRole;
import edu.seu.server.service.cache.ICacheService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
public interface IMenuRoleService extends ICacheService<MenuRole> {

    /**
     * 根据当前角色rid更新对应的菜单id列表，只能添加末级菜单
     * 如果开发该方法的控制器接口可能导致非法数据入库，建议使用addMenuRole和deleteMenuRole方法
     * @param rid 角色id
     * @param mIds 需要更新的当前角色id对应的菜单id数组(不定参数)
     * @return 更新是否成功的公共返回对象
     */
    @Deprecated
    ResponseBean updateMenuRole(Integer rid, Integer ... mIds);

    /**
     * 根据当前角色rid添加菜单，要求该菜单必须为末级菜单，使用存储过程实现
     * @param rid 角色id
     * @param mid 待添加的菜单id
     * @return 公共返回对象
     */
    ResponseBean addMenuRole(Integer rid, Integer mid);

    /**
     * 根据当前角色rid删除菜单，要求该菜单必须为末级菜单，如果该角色已经没有被删除菜单的父菜单下所有子菜单的权限，则删除该角色对该父菜单的权限
     * @param rid 角色id
     * @param mid 待删除的菜单id
     * @return 公共返回对象
     */
    ResponseBean deleteMenuRole(Integer rid, Integer mid);
}
