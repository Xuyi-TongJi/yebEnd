package edu.seu.server.common.lang;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页公共返回对象
 * @author xuyitjuseu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Api("分页公共返回对象")
public class ResponsePageBean {
    @ApiModelProperty("总条数")
    private Long total;
    @ApiModelProperty("分页数据")
    private List<?> data;
}
