package edu.seu.server.service;

import edu.seu.server.common.lang.ResponseBean;
import edu.seu.server.pojo.MenuRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
public interface IMenuRoleService extends IService<MenuRole> {

    /**
     * 根据当前角色rid更新对应的菜单id列表
     * @param rid 角色id
     * @param mIds 需要更新的当前角色id对应的菜单id数组(不定参数)
     * @return 更新是否成功的公共返回对象
     */
    ResponseBean updateMenuRole(Integer rid, Integer ... mIds);

    /**
     * 在增删查改操作后清空缓存层中的缓存
     */
    default void cleanupCache(){}
}
