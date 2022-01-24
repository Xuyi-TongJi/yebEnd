package edu.seu.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.seu.server.pojo.Employee;

import java.time.LocalDate;

/**
 * <p>
 *  职工服务类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
public interface IEmployeeService extends IService<Employee> {

    /**
     * 分页模糊查询员工信息，模糊查询关键字封装在employee实体类姓名属性中
     * @param currentPage 当前页
     * @param pageSize 每页存放的记录数
     * @param employee employee实体类，其中封装了可有可无的查询信息
     * @param beginDateScopes 开始工作日期范围，为一个大小为2的数组
     * @return MyBatisPlus提供的IPage分页接口
     */
    IPage<Employee> getEmployeeByPage(Integer currentPage, Integer pageSize,
                                      Employee employee, LocalDate[] beginDateScopes);


    /**
     * 添加员工
     * @param employee 封装必填信息的员工实体类
     * @return 受影响的行数
     */
    Integer addEmployee(Employee employee);
}
