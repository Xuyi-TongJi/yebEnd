package edu.seu.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.seu.server.common.lang.ResponseBean;
import edu.seu.server.common.vo.JobLevelVo;
import edu.seu.server.pojo.JobLevel;
import edu.seu.server.service.IJobLevelService;
import edu.seu.server.util.LevelTitleUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dozer.Mapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
@RestController
@RequestMapping("/system/basic/jobLevel")
@Api(tags = "JobLevelController")
public class JobLevelController {

    private final IJobLevelService jobLevelService;
    private final Mapper mapper;

    public JobLevelController(IJobLevelService jobLevelService, Mapper mapper) {
        this.jobLevelService = jobLevelService;
        this.mapper = mapper;
    }

    @ApiOperation("查询所有可用职称")
    @GetMapping("/")
    public List<JobLevel> getJobLevelList() {
        return jobLevelService.list(new QueryWrapper<JobLevel>().eq("enabled", true));
    }

    @ApiOperation("增加职称")
    @PutMapping("/")
    public ResponseBean addJobLevel(@RequestBody JobLevelVo jobLevelVo) {
        if (LevelTitleUtil.levelTitleIncluded(jobLevelVo.getTitleLevel())) {
            JobLevel jobLevel = mapper.map(jobLevelVo, JobLevel.class);
            jobLevel.setCreateDate(LocalDateTime.now());
            if (jobLevelService.save(jobLevel)) {
                return ResponseBean.success("添加成功！", null);
            }
            return ResponseBean.error(500, "添加失败！", null);
        }
        return ResponseBean.error(500, "不支持的职称级别名称，请重试！", null);
    }

    @ApiOperation("删除职称")
    @DeleteMapping("/{id}")
    public ResponseBean deleteJobLevel(@PathVariable Integer id) {
        if (jobLevelService.removeById(id)) {
            return ResponseBean.success("删除成功!", null);
        } else {
            return ResponseBean.error(500, "删除失败！", null);
        }
    }

    @ApiOperation("批量删除职称")
    @DeleteMapping("/")
    public ResponseBean deleteJobLevelBatch(@RequestBody Integer... ids) {
        if (jobLevelService.removeByIds(Arrays.asList(ids))) {
            return ResponseBean.success("删除成功!", null);
        } else {
            return ResponseBean.error(500, "删除失败！", null);
        }
    }

    @ApiOperation("更改职称")
    @PostMapping("/")
    public ResponseBean updateJobLevel(@RequestBody JobLevelVo jobLevelVo) {
        if (LevelTitleUtil.levelTitleIncluded(jobLevelVo.getTitleLevel())) {
            JobLevel jobLevel = mapper.map(jobLevelVo, JobLevel.class);
            if (jobLevelService.updateById(jobLevel)) {
                return ResponseBean.success("更新成功！", null);
            }
            return ResponseBean.error(500, "更新失败！", null);
        }
        return ResponseBean.error(500, "不支持的职称级别名称，请重试！", null);
    }

    @ApiOperation("获取所有职称等级名称")
    @GetMapping("/levelTitle")
    public List<String> getLevelTitle() {
        return LevelTitleUtil.getLevelTitleNameList();
    }
}