package edu.seu.server.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 职位Vo实体类
 * @author xuyitjuseu
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel("职位Vo实体类")
public class PositionVo {

    @ApiModelProperty("职位名称")
    @NotBlank(message = "职位名称不可为空")
    private String name;
}
