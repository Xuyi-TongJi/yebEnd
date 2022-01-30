package edu.seu.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.seu.server.common.pojo.MenuPojo;
import edu.seu.server.pojo.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据用户id查询对应的菜单列表，使用级联查询
     * @param id 用户id
     * @return 对应的菜单实体类列表
     */
    List<Menu> getMenusByAdminId(Integer id);

    /**
     * 根据角色查询该角色能够访问的菜单列表
     * @return 对应的菜单列表实体类
     */
    List<Menu> getMenusWithRole();

    /**
     * 查询所有菜单及其子菜单
     * @return 对应的菜单列表实体类
     */
    List<MenuPojo> getAllMenus();
}
