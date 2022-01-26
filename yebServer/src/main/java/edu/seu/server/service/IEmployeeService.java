package edu.seu.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.seu.server.pojo.Employee;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

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

    /**
     * 更新员工
     * @param employee 封装必填信息的员工实体类
     * @return 受影响的行数
     */
    Integer updateEmployee(Employee employee);

    /**
     * 根据id查询员工，如果id == null，则查询所有员工
     * @param id 员工id
     * @return 包含员工实体类的List，如果id != null，则该List只有一条记录
     */
    List<Employee> getEmployee(Integer id);

    /**
     * 向消息队列发送消息，该消息必须可序列化
     * @param serializable 可序列化的消息
     */
    default void sendMessage(Serializable serializable){}

}
