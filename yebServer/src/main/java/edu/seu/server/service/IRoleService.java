package edu.seu.server.service;

import edu.seu.server.pojo.Role;
import edu.seu.server.service.cache.ICacheService;

import java.util.List;

/**
 * <p>
 *  角色服务类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
public interface IRoleService extends ICacheService<Role> {

    /**
     * 获得包含级联菜单的角色列表
     * @return 角色实体类列表
     */
    List<Role> getRoleWithMenus();

    /**
     * 获得角色id列表
     * @return 角色id列表
     */
    List<Integer> getRidList();
}
