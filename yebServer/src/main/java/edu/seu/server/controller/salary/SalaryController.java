package edu.seu.server.controller.salary;


import edu.seu.server.common.lang.ResponseBean;
import edu.seu.server.pojo.Salary;
import edu.seu.server.service.ISalaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
@RestController
@RequestMapping("/salary/sob")
public class SalaryController {

    private final ISalaryService salaryService;

    public SalaryController(ISalaryService salaryService) {
        this.salaryService = salaryService;
    }

    @ApiOperation("添加帐套")
    @PostMapping("/")
    public ResponseBean addSalary(@RequestBody Salary salary) {
        salary.setCreateDate(LocalDateTime.now());
        if (salaryService.save(salary)) {
            return ResponseBean.success("添加成功", null);
        } else {
            return ResponseBean.error(500, "添加失败", null);
        }
    }

    @ApiOperation("删除帐套")
    @DeleteMapping("/{id}")
    public ResponseBean deleteSalary(@PathVariable Integer id) {
        if (salaryService.removeById(id)) {
            return ResponseBean.success("删除成功", null);
        } else {
            return ResponseBean.error(500, "删除失败", null);
        }
    }

    @ApiOperation("更新帐套")
    @PutMapping("/")
    public ResponseBean updateSalary(@RequestBody Salary salary) {
        if (salaryService.updateById(salary)) {
            return ResponseBean.success("更新成功", null);
        } else {
            return ResponseBean.error(500, "更新失败", null);
        }
    }
}
