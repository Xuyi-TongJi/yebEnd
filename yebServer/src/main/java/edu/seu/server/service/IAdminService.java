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
}
