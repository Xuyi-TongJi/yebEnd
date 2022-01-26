package edu.seu.server.service;

import edu.seu.server.pojo.Admin;
import edu.seu.server.pojo.Role;
import edu.seu.server.service.cache.ICacheService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
public interface IAdminService extends ICacheService<Admin> {

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
     * 更新管理员密码，需要检验当前密码
     * @param currentPassword 当前密码
     * @param newPassword 新密码
     * @param aid 管理员id
     * @return 如果密码错误直接返回-1, 如果数据库更新失败返回0，返回1则表示更新成功(受影响的行数)
     */
    Integer updatePassword(String currentPassword, String newPassword, Integer aid);
}
