package edu.seu.server.controller;


import edu.seu.server.common.lang.ResponseBean;
import edu.seu.server.pojo.Department;
import edu.seu.server.service.IDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  部门前端控制器
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
@RestController
@RequestMapping("/system/basic/department")
@Api(tags = "DepartmentController")
public class DepartmentController {

    private final IDepartmentService departmentService;

    public DepartmentController(IDepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @ApiOperation("获取部门列表")
    @GetMapping("/")
    public List<Department> getDepartmentList() {
        return departmentService.getDepartmentList();
    }

    @ApiOperation("新增部门")
    @PostMapping("/")
    public ResponseBean addDepartment(@RequestBody Department department) {
        return null;
    }
}
