package edu.seu.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.seu.server.pojo.Admin;
import edu.seu.server.pojo.Role;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 根据当前登录用户的用户名（通过Principal对象获得）获得完整的登录用户信息
     * @param username 用户名字符串
     * @return 包含当前登录用户的所有信息的pojo（实体类）
     */
    Admin getAdminByUsername(String username);

    /**
     * 根据用户id获取该用户所具有的角色
     * @param adminId 用户Id
     * @return 该用户所具有的所有角色实体类列表
     */
    List<Role> getRoleListByAdminId(Integer adminId);

    /**
     * 根据关键词对Admin进行模糊查询，不能查询当前登录的用户
     * @param keywords 模糊查询关键词
     * @return 查询得到的Admin实体类列表，其children属性为该Admin所具有的角色列表
     */
    List<Admin> getAdminListByKeywords(String keywords);


    /**
     * 更新操作员所具有的角色
     * @param aid 操作员ID
     * @param rIds 角色id，可以同时更新多个
     * @return 受影响的行数，用以判断是否更新成功
     */
    Integer updateAdminRole(Integer aid, Integer ... rIds);

    /**
     * 清空缓存
     */
    default void cleanUpCache() {
    }
}
