package edu.seu.server.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

/**
 * Menu PoJo类
 * @author xuyitjuseu
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_menu")
@ApiModel(value = "MenuPojo实体类", description = "用于将数据库中查得的数据返回给前端")
public class MenuPojo {

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "菜单名")
    private String name;

    /**
     * 子菜单为当前菜单实体类的子菜单，用于前端多级表的生成
     */
    @ApiModelProperty(value = "子菜单")
    @TableField(exist = false)
    private List<MenuPojo> children;

    /**
     * 有访问这个菜单的权限的角色id列表
     */
    @ApiModelProperty(value = "角色id列表")
    @TableField(exist = false)
    private Set<Integer> rIds;
}
