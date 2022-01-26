package edu.seu.server.controller.position;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.seu.server.common.lang.ResponseBean;
import edu.seu.server.pojo.Position;
import edu.seu.server.service.IPositionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  职位前端控制器
 * </p>
 * 实现职位的增删改查操作
 * @author xuyitjuseu
 * @since 2022-01-14
 */
@RestController
@RequestMapping("/system/basic/position")
@Api(tags = "PositionController")
public class PositionController {

    private final IPositionService positionService;

    public PositionController(IPositionService positionService) {
        this.positionService = positionService;
    }

    @ApiOperation(value = "获得所有职称")
    @GetMapping("/")
    public List<Position> getPositionList() {
        // list可以直接转为json格式
        return positionService.list(new QueryWrapper<Position>().eq("enabled", true));
    }

    @ApiOperation(value = "更新职称")
    @PutMapping("/")
    public ResponseBean updatePosition(@RequestBody Position position) {
        if (positionService.updateById(position)) {
            return ResponseBean.success("更新成功", null);
        } else {
            return ResponseBean.error(500, "更新失败", null);
        }
    }

    @ApiOperation(value = "添加职称")
    @PostMapping("/")
    public ResponseBean addPosition(@RequestBody Position position) {
        position.setCreateDate(LocalDateTime.now());
        if (positionService.save(position)) {
            return ResponseBean.success("添加成功", null);
        } else {
            return ResponseBean.error(500, "添加失败", null);
        }
    }

    @ApiOperation(value = "删除职称")
    @DeleteMapping("/{id}")
    public ResponseBean deletePosition(@PathVariable Integer id) {
        if (positionService.removeById(id)) {
            return ResponseBean.success("删除成功", null);
        } else {
            return ResponseBean.error(500, "删除失败", null);
        }
    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping("/")
    public ResponseBean deletePositionBatch(@RequestBody Integer... ids) {
        if (positionService.removeByIds(Arrays.asList(ids))) {
            return ResponseBean.success("删除成功", null);
        } else {
            return ResponseBean.error(500, "删除失败", null);
        }
    }
}