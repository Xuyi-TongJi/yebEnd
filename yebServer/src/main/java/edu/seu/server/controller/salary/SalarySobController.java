package edu.seu.server.controller.salary;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.seu.server.common.lang.ResponseBean;
import edu.seu.server.pojo.Employee;
import edu.seu.server.pojo.Salary;
import edu.seu.server.service.IEmployeeService;
import edu.seu.server.service.ISalaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工工资帐套前端控制器
 * @author xuyitjuseu
 */
@RestController
@RequestMapping("/salary/sobcfg")
public class SalarySobController {

    private final ISalaryService salaryService;
    private final IEmployeeService employeeService;

    public SalarySobController(ISalaryService salaryService,
                               IEmployeeService employeeService) {
        this.salaryService = salaryService;
        this.employeeService = employeeService;
    }

    @ApiOperation("获取所有工资帐套列表")
    @GetMapping("/salaries")
    public List<Salary> getSalaryList() {
        return salaryService.listInCache();
    }

    @ApiOperation("获取所有员工及其帐套信息")
    @GetMapping("/")
    public ResponseBean getEmployeeWithSalaryByPage(@RequestParam(defaultValue = "1")Integer currentPage,
                                                 @RequestParam(defaultValue = "10")Integer pageSize) {
        IPage<Employee> result = salaryService.getEmployeeWithSalaryByPage(currentPage, pageSize);
        return ResponseBean.success("查询成功！", result);
    }

    @ApiOperation("更新员工帐套信息")
    @PutMapping("/")
    public ResponseBean updateEmployeeSalary(@RequestParam Integer eid, @RequestParam Integer sid) {
        String columnEid = "eid";
        String columnSid = "sid";
        if (employeeService.update(new UpdateWrapper<Employee>().set(columnSid, sid).eq(columnEid, eid))) {
            return ResponseBean.success("更新成功", null);
        } else {
            return ResponseBean.error(500, "更新失败", null);
        }
    }
}
