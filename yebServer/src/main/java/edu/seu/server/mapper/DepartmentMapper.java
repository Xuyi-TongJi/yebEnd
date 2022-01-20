package edu.seu.server.mapper;

import edu.seu.server.pojo.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * 获得所有部门，采用分级封装
     * @param parentId MyBatis递归sql,查询所有菜单时，该值为-1
     * @return 顶级部门的列表，其子部门封装在children属性内
     */
    List<Department> getDepartmentList(Integer parentId);

    /**
     * 增加一个部门，使用存储过程实现
     * @param department 待添加的部门实体类
     * @return 受影响的行数
     */
    Department addDepartment(Department department);
}
