package edu.seu.server.controller.department;


import edu.seu.server.common.lang.ResponseBean;
import edu.seu.server.common.vo.DepartmentVo;
import edu.seu.server.pojo.Department;
import edu.seu.server.service.IDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dozer.Mapper;
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
    private final Mapper mapper;

    public DepartmentController(IDepartmentService departmentService,
                                Mapper mapper) {
        this.departmentService = departmentService;
        this.mapper = mapper;
    }

    @ApiOperation("获取部门列表")
    @GetMapping("/")
    public List<Department> getDepartmentList() {
        return departmentService.getDepartmentList();
    }

    @ApiOperation("新增部门")
    @PostMapping("/")
    public ResponseBean addDepartment(@RequestBody DepartmentVo departmentVo) {
        Department department = mapper.map(departmentVo, Department.class);
        if (departmentService.addDepartment(department).getResult() > 0) {
            departmentService.cleanUpCache();
            return ResponseBean.success("添加成功！", null);
        } else {
            return ResponseBean.error(500,"添加失败!", null);
        }
    }

    @ApiOperation("删除部门")
    @DeleteMapping("/{id}")
    public ResponseBean deleteDepartment(@PathVariable Integer id) {
        int result = departmentService.deleteDepartment(id).getResult();
        if (result > 0) {
            departmentService.cleanUpCache();
            return ResponseBean.success("删除成功!", null);
        } else if (result == -1) {
            return ResponseBean.error(500, "当前部门下含有员工，不能删除", null);
        } else {
            return ResponseBean.error(500, "当前部门不存在或含有子部门，不能删除", null);
        }
    }
}
