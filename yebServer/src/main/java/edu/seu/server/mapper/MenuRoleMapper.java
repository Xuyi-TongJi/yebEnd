package edu.seu.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.seu.server.pojo.MenuRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}
