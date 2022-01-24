package edu.seu.server.common.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 更新Admin时使用的Admin Vo类
 * @author xuyitjuseu
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(value = "AdminVo更新实体类", description = "用于接受前端传来的更新Admin时的信息")
public class AdminUpdateVo {

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "姓名")
    @NotBlank(message = "姓名不可为空")
    private String name;

    @ApiModelProperty(value = "手机号码")
    @NotBlank(message = "手机号码不可为空")
    private String phone;

    @ApiModelProperty(value = "住宅电话")
    @NotBlank(message = "电话号码不可为空")
    private String telephone;

    @ApiModelProperty(value = "联系地址")
    @NotBlank(message = "联系地址不可为空")
    private String address;

    @ApiModelProperty(value = "用户头像")
    @NotBlank(message = "用户名不可为空")
    private String userFace;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;
}
