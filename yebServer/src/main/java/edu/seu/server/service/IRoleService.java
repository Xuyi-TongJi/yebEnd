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
     * 获得角色id列表
     * @return 角色id列表
     */
    List<Integer> getRidList();
}
