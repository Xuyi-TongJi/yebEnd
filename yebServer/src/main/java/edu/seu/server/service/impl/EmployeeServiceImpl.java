package edu.seu.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.server.mapper.EmployeeMapper;
import edu.seu.server.pojo.Employee;
import edu.seu.server.service.IEmployeeService;
import edu.seu.server.util.EngageFormUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;

    @Override
    public IPage<Employee> getEmployeeByPage(Integer currentPage, Integer pageSize, Employee employee,
                                             LocalDate[] beginDateScopes) {
        Page<Employee> page = new Page<>(currentPage, pageSize);
        return employeeMapper.getEmployeeByPage(page, employee, beginDateScopes);
    }

    @Override
    public Integer addEmployee(Employee employee) {
        if (!EngageFormUtil.formNameIncluded(employee.getEngageForm())) {
            return 0;
        }
        LocalDate beginContract = employee.getBeginContract();
        LocalDate endContract = employee.getEndContract();
        long days = beginContract.until(endContract, ChronoUnit.DAYS);
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(days / 365.00)));
        LocalDate conversionTime = employee.getConversionTime();
        days = conversionTime.until(LocalDate.now(), ChronoUnit.DAYS);
        employee.setWorkAge(Integer.parseInt(String.valueOf(days / 365)));
        employee.setWorkID(getMaxWorkId());
        return employeeMapper.insert(employee);
    }

    private String getMaxWorkId() {
        List<Map<String, Object>> maps = employeeMapper.selectMaps(new QueryWrapper<Employee>().select("max(workID)"));
        return String.format("%08d", Integer.parseInt(maps.get(0).get("max(workID)").toString()) + 1);
    }
}
