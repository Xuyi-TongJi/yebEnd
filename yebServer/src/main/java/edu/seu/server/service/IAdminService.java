package edu.seu.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.seu.server.pojo.Admin;
import edu.seu.server.pojo.Menu;
import edu.seu.server.pojo.ResponseBean;

import javax.servlet.http.HttpServletRequest;
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
}
