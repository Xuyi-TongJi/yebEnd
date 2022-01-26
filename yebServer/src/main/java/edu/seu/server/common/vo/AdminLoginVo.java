package edu.seu.server.common.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户登录实体类
 * 用于接受前端传来的用户登录信息
 * @author xuyitjuseu
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(value = "AdminVo实体类", description = "用于接受前端传来的用户登录信息")
public class AdminLoginVo {
    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名不可为空")
    private String username;
    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不可为空")
    private String password;
    @ApiModelProperty(value = "验证码", required = true)
    @NotBlank(message = "验证码不可为空")
    private String code;
}
