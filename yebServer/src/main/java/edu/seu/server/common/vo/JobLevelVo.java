package edu.seu.server.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 职称等级vo类
 * @author xuyitjuseu
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(value = "JobLevelVo实体类", description = "用于接受前端传来的JobLevel增删查改信息")
public class JobLevelVo {
    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "职称名称")
    @NotBlank(message = "职称名称不能为空")
    private String name;

    @ApiModelProperty(value = "职称等级名称")
    @NotBlank(message = "职称等级名称不能为空")
    private String titleLevel;

    @ApiModelProperty(value = "是否启动")
    private Boolean enabled;
}
