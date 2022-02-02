package edu.seu.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.seu.server.pojo.MenuRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
@Mapper
public interface MenuRoleMapper extends BaseMapper<MenuRole> {

    /**
     * 根据rid，mIds批量添加一个rid对应的mId
     * @param rid 角色id
     * @param mIds 菜单id数组
     * @return 受影响的条数
     */
    Integer insertBatch(@Param("rid") Integer rid, @Param("mIds") Integer[] mIds);

    /**
     * 根据rid，mid为对应角色添加菜单
     * @param map 包含mid,rid,result的map集合
     */
    void addMenuRole(Map<String, Integer> map);

    /**
     * 根据rid,mid为对应角色删除菜单
     * @param map 包含mid,rid,result的map集合
     */
    void deleteMenuRole(Map<String, Integer> map);
}
