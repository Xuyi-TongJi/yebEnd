package edu.seu.server.service;

import edu.seu.server.pojo.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  角色服务类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
public interface IRoleService extends IService<Role> {

    /**
     * 获得角色列表
     * @return 角色列表
     */
    List<Role> getRoleList();

    /**
     * 清空缓冲层数据
     */
    void cleanUpCache();

    /**
     * 获得角色id列表
     * @return 角色id列表
     */
    List<Integer> getRidList();
}
