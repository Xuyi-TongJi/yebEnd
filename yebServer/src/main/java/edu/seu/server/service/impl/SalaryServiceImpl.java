package edu.seu.server.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.server.mapper.EmployeeMapper;
import edu.seu.server.mapper.SalaryMapper;
import edu.seu.server.pojo.Employee;
import edu.seu.server.pojo.Salary;
import edu.seu.server.service.ISalaryService;
import edu.seu.server.util.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
@Service
public class SalaryServiceImpl extends ServiceImpl<SalaryMapper, Salary> implements ISalaryService {

    @Resource
    private EmployeeMapper employeeMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    public SalaryServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Salary> listInCache() {
        String keyName = RedisUtil.SALARY_LIST;
        return listInCache(keyName, redisTemplate);
    }

    @Override
    public IPage<Employee> getEmployeeWithSalaryByPage(Integer currentPage, Integer pageSize) {
        Page<Employee> page = new Page<>(currentPage, pageSize);
        return employeeMapper.getEmployeeWithSalaryByPage(page);
    }

    @Override
    public void cleanupCache() {
        redisTemplate.delete(RedisUtil.SALARY_LIST);
    }
}
