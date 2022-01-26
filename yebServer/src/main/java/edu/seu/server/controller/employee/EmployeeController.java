package edu.seu.server.controller.employee;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.seu.server.common.lang.ResponseBean;
import edu.seu.server.pojo.*;
import edu.seu.server.service.*;
import edu.seu.server.util.enumUtil.EngageFormUtil;
import edu.seu.server.util.enumUtil.TiptopDegreeUtil;
import edu.seu.server.util.enumUtil.WedlockUtil;
import edu.seu.server.util.enumUtil.WorkStateUtil;
import io.swagger.annotations.ApiModelProperty;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 员工前端控制器
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
        return politicsStatusService.listInCache();
    }

    @ApiModelProperty("获取所有民族列表")
    @GetMapping("/nation")
    public List<Nation> getNationList() {
        return nationService.listInCache();
    }

    @ApiModelProperty("获取所有职位")
    @GetMapping("/position")
    public List<Position> getPositionList() {
        return positionService.listEnabled();
    }

    @ApiModelProperty("获取所有职级")
    @GetMapping("/jobLevel")
    public List<JobLevel> getJobLevelList() {
        return jobLevelService.listEnabled();
    }

    @ApiModelProperty("获取所有部门")
    @GetMapping("/department")
    public List<Department> getDepartmentLevelList() {
        return departmentService.listInCache();
    }

    @ApiModelProperty("获取所有合同类型")
    @GetMapping("/engageForm")
    public List<String> getEngageFormList() {
        return EngageFormUtil.getEngageFormList();
    }

    @ApiModelProperty("获取所有在职类型")
    @GetMapping("/workState")
    public List<String> getWorkStateList() {
        return WorkStateUtil.getEngageFormList();
    }

    @ApiModelProperty("获取所有婚姻状况类型")
    @GetMapping("/wedlock")
    public List<String> getWedlockList() {
        return WedlockUtil.getList();
    }

    @ApiModelProperty("获取所有最高学历类型")
    @GetMapping("/tiptopDegree")
    public List<String> getTiptopDegreeList() {
        return TiptopDegreeUtil.getList();
    }

    @ApiModelProperty("添加员工")
    @PostMapping("/")
    public ResponseBean addEmployee(@Validated @RequestBody Employee employee) {
        int result = employeeService.addEmployee(employee);
        if (result == 1) {
            return ResponseBean.success("添加成功！", null);
        } else if (result == -1) {
            return ResponseBean.error(500, "输入参数不合法!", null);
        } else {
            return ResponseBean.error(500, "添加失败", null);
        }
    }

    @ApiModelProperty("更新员工")
    @PutMapping("/")
    public ResponseBean updateEmployee(@Validated @RequestBody Employee employee) {
        int result = employeeService.updateEmployee(employee);
        if (result == 1) {
            return ResponseBean.success("添加成功！", null);
        } else if (result == -1) {
            return ResponseBean.error(500, "输入参数不合法!", null);
        } else {
            return ResponseBean.error(500, "添加失败", null);
        }
    }

    @ApiModelProperty("删除员工")
    @DeleteMapping("/{id}")
    public ResponseBean deleteEmployee(@PathVariable Integer id) {
        if (employeeService.removeById(id)) {
            return ResponseBean.success("删除成功！", null);
        } else {
            return ResponseBean.error(500, "删除失败", null);
        }
    }

    @ApiModelProperty("导出员工数据")
    @GetMapping(value = "/export", produces = "application/octet-stream")
    public void exportEmployeeList(HttpServletResponse response) {
        List<Employee> list = employeeService.getEmployee(null);
        ExportParams params = new ExportParams("员工表", "员工表", ExcelType.HSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params, Employee.class, list);
        ServletOutputStream sos = null;
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("content-disposition", "attachment;filename="
                    + URLEncoder.encode("员工表.xls", "UTF-8"));
            sos = response.getOutputStream();
            workbook.write(sos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sos != null) {
                try {
                    sos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ApiModelProperty("导入员工数据")
    @PostMapping("/import")
    public ResponseBean importEmployeeList(MultipartFile file) {
        ImportParams params = new ImportParams();
        // 去掉标题行
        params.setTitleRows(1);
        try {
            List<Nation> nationList = nationService.listInCache();
            List<Position> positionList = positionService.listEnabled();
            List<JobLevel> jobLevelList = jobLevelService.listEnabled();
            List<Department> departmentList = departmentService.listInCache();
            List<PoliticsStatus> politicsStatusList = politicsStatusService.listInCache();
            List<Employee> list = ExcelImportUtil.importExcel(file.getInputStream(), Employee.class, params);
            list.forEach(employee -> {
                employee.setNationId(
                        nationList.get(nationList
                                .indexOf(new Nation(employee.getNation().getName()))).getId());
                employee.setPosId(
                        positionList.get(positionList
                                .indexOf(new Position(employee.getPosition().getName()))).getId());
                employee.setJobLevelId(
                        jobLevelList.get(jobLevelList
                                .indexOf(new JobLevel(employee.getJobLevel().getName()))).getId());
                employee.setDepartmentId(
                        departmentList.get(departmentList
                                .indexOf(new Department(employee.getDepartment().getName()))).getId());
                employee.setPoliticId(
                        politicsStatusList.get(politicsStatusList
                                .indexOf(new PoliticsStatus(employee.getPoliticsStatus().getName()))).getId());
            });
            if (employeeService.saveBatch(list)) {
                return ResponseBean.success("导入成功！", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseBean.error(500, "导入失败", null);
    }
}