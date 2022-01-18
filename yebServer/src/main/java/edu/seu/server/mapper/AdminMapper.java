package edu.seu.server.mapper;

import edu.seu.server.pojo.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.seu.server.pojo.Menu;
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
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 根据id查询该用户所具有的所有角色
     * <p>admin和role为多对多关系，需要进行多表查询</p>
     * @param adminId 用户id
     * @return 包含该用户所具有的所有角色实体类的列表
     */
    List<Role> getRoleListById(Integer adminId);
}
