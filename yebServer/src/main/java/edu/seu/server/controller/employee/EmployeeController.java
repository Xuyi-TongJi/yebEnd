package edu.seu.server.controller.employee;


import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.seu.server.common.lang.ResponseBean;
import edu.seu.server.pojo.*;
import edu.seu.server.service.*;
import edu.seu.server.util.EngageFormUtil;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  员工前端控制器
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
@RestController
@RequestMapping("/employee/basic/")
public class EmployeeController {

    private final IEmployeeService employeeService;
    private final IPoliticsStatusService politicsStatusService;
    private final INationService nationService;
    private final IPositionService positionService;
    private final IJobLevelService jobLevelService;
    private final IDepartmentService departmentService;

    public EmployeeController(IEmployeeService employeeService,
                              IPoliticsStatusService politicsStatusService,
                              INationService nationService,
                              IPositionService positionService,
                              IJobLevelService jobLevelService,
                              IDepartmentService departmentService) {
        this.employeeService = employeeService;
        this.politicsStatusService = politicsStatusService;
        this.nationService = nationService;
        this.positionService = positionService;
        this.jobLevelService = jobLevelService;
        this.departmentService = departmentService;
    }


    @ApiModelProperty("员工列表分页模糊查询，关键字封装在实体类的姓名属性中")
    @GetMapping("/")
    public IPage<Employee> getEmployeeByPage(@RequestParam(defaultValue = "1") Integer currentPage,
                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                             Employee employee,
                                             LocalDate[] beginDateScopes) {
        return employeeService.getEmployeeByPage(currentPage, pageSize, employee, beginDateScopes);
    }

    @ApiModelProperty("获取所有政治面貌")
    @GetMapping("/politicStatus")
    public List<PoliticsStatus> getPoliticsStatusList() {
        return politicsStatusService.getPoliticsStatusList();
    }

    @ApiModelProperty("获取所有民族列表")
    @GetMapping("/nation")
    public List<Nation> getNationList() {
        return nationService.getNationList();
    }

    @ApiModelProperty("获取所有职位")
    @GetMapping("/position")
    public List<Position> getPositionList() {
        return positionService.getPositionList();
    }

    @ApiModelProperty("获取所有职级")
    @GetMapping("/jobLevel")
    public List<JobLevel> getJobLevelList() {
        return jobLevelService.getJobLevelList();
    }

    @ApiModelProperty("获取所有部门")
    @GetMapping("/department")
    public List<Department> getDepartmentLevelList() {
        return departmentService.getDepartmentList();
    }

    @ApiModelProperty("/获取所有合同类型")
    @GetMapping("/engageForm")
    public List<String> getEngageFormList() {
        return EngageFormUtil.getEngageFormList();
    }

    @ApiModelProperty("添加员工")
    @PostMapping("/")
    public ResponseBean addEmployee(@Validated @RequestBody Employee employee) {
        if (employeeService.addEmployee(employee) == 1) {
            return ResponseBean.success("添加成功！", null);
        } else {
            return ResponseBean.error(500, "添加失败!", null);
        }
    }
}