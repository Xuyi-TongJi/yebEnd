package edu.seu.server.mapper;

import edu.seu.server.pojo.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.seu.server.pojo.Menu;
import edu.seu.server.pojo.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  管理员Mapper接口
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 根据id查询该用户所具有的所有角色
     * <p>admin和role为多对多关系，需要进行多表查询</p>
     * @param adminId 用户id
     * @return 包含该用户所具有的所有角色实体类的列表
     */
    List<Role> getRoleListById(Integer adminId);

    /**
     * 根据关键词对Admin进行模糊查询，返回所有结果，并查询它们所具有的角色
     * <p>不查询当前登录的用户</p>
     * @param keywords 关键词
     * @param currentId 当前登录的用户id
     * @return Admin实体类列表，其中roleList属性为该Admin具有的角色列表
     */
    List<Admin> getAdminListByKeywords(String keywords, Integer currentId);
}
