package edu.seu.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.seu.server.pojo.Role;
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
public interface RoleMapper extends BaseMapper<Role> {

    /**
     *  获得角色列表及其可以访问的级联菜单列表
     *  @return 包含级联菜单列表属性的MenuPojo列表
     */
    List<Role> getRoleWithMenus();
}
