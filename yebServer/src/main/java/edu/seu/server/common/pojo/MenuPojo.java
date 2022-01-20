package edu.seu.server.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import edu.seu.server.pojo.Menu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

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
     * 使用@TableField注解标识当前属性不是t_menu表的某个字段
     */
    @ApiModelProperty(value = "子菜单")
    @TableField(exist = false)
    private List<Menu> children;
}
