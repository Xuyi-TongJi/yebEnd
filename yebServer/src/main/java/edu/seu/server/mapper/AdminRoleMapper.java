package edu.seu.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.seu.server.pojo.AdminRole;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    /**
     * 批量增加记录
     * @param aid adminId
     * @param rIds rid数组
     * @return 受影响的行数
     */
    Integer insertBatch(@Param("aid") Integer aid, @Param("rIds") Integer ... rIds);
}
