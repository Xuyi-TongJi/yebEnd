package edu.seu.server.mapper;

import edu.seu.server.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
}
