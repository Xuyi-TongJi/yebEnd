package edu.seu.server.common.lang;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 公共返回对象Bean
 * @author xuyitjuseu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Api("公共返回对象")
@ToString
public class ResponseBean {
    @ApiModelProperty("状态码")
    private Integer status;
    @ApiModelProperty("响应信息")
    private String message;
    @ApiModelProperty("响应json格式obj")
    private Object obj;

    /**
     * 请求成功返回状态码为200的公共返回对象
     * @param message 返回信息String或null
     * @param obj 返回对象，通常是Json或null
     * @return ResponseBean公共返回对象
     */
    public static ResponseBean success(String message, Object obj) {
        return new ResponseBean(200, message, obj);
    }

    /**
     * 请求失败（后端没有抛出异常，实际的状态码还是200）时返回的公共返回对象，包含一个请求失败原因的状态码
     * @param status 返回失败的状态码，例如403，500等
     * @param message 返回信息String或null
     * @param obj 返回对象，通常是Json或null
     * @return ResponseBean公共返回对象
     */
    public static ResponseBean error(Integer status, String message, Object obj) {
        return new ResponseBean(status, message, obj);
    }
}
