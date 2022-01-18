package edu.seu.server.service;

import edu.seu.server.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
