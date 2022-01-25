package edu.seu.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.seu.server.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
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
public interface EmployeeMapper extends BaseMapper<Employee> {

    /**
     * 分页模糊查询员工信息
     * @param page MyBatisPlus分页查询开启类
     * @param employee 封装模糊查询信息的实体类
     * @param beginDateScopes 开始工作时间范围的数组
     * @return IPage接口类
     */
    IPage<Employee> getEmployeeByPage(Page<Employee> page,
                                      @Param("employee") Employee employee,
                                      @Param("beginDateScopes") LocalDate[] beginDateScopes);

    /**
     * 根据id查询员工，如果id == null，则查询所有员工
     * @param id 员工id
     * @return 包含员工实体类的List，如果id != null，则该List只有一条记录
     */
    List<Employee> getEmployee(Integer id);
}
