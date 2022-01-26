package edu.seu.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.seu.server.pojo.Employee;
import edu.seu.server.pojo.Salary;
import edu.seu.server.service.cache.ICacheService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
public interface ISalaryService extends ICacheService<Salary> {

    /**
     * 获取员工的工资帐套
     * @param currentPage 当前页
     * @param pageSize 每页的记录条数
     * @return IPage分页对象
     */
    IPage<Employee> getEmployeeWithSalaryByPage(Integer currentPage, Integer pageSize);
}
