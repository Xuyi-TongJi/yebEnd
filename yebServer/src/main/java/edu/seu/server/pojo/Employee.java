package edu.seu.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_employee")
@ApiModel(value="Employee对象", description="")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "员工编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "员工姓名")
    @NotBlank(message = "姓名不能为空")
    private String name;

    @ApiModelProperty(value = "性别")
    @NotBlank(message = "性别不能为空")
    private String gender;

    @ApiModelProperty(value = "出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @NotNull(message = "出生日期不能为空")
    private LocalDate birthday;

    @ApiModelProperty(value = "身份证号")
    @NotBlank(message = "身份证号不能为空")
    private String idCard;

    @ApiModelProperty(value = "婚姻状况")
    @NotBlank(message = "婚姻不能为空")
    private String wedlock;

    @ApiModelProperty(value = "民族")
    @NotNull(message = "民族不能为空")
    private Integer nationId;

    @ApiModelProperty(value = "籍贯")
    @NotBlank(message = "籍贯不能为空")
    private String nativePlace;

    @ApiModelProperty(value = "政治面貌")
    @NotNull(message = "政治面貌不能为空")
    private Integer politicId;

    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty(value = "电话号码")
    @NotBlank(message = "电话号码不能为空")
    private String phone;

    @ApiModelProperty(value = "联系地址")
    @NotBlank(message = "联系地址不能为空")
    private String address;

    @ApiModelProperty(value = "所属部门")
    @NotNull(message = "所属部门不能为空")
    private Integer departmentId;

    @ApiModelProperty(value = "职称ID")
    @NotNull(message = "职称不能为空")
    private Integer jobLevelId;

    @ApiModelProperty(value = "职位ID")
    @NotNull(message = "职位不能为空")
    private Integer posId;

    @ApiModelProperty(value = "聘用形式")
    @NotBlank(message = "聘用形式不能为空")
    private String engageForm;

    @ApiModelProperty(value = "最高学历")
    @NotBlank(message = "最高学历不能为空")
    private String tiptopDegree;

    @ApiModelProperty(value = "所属专业")
    @NotBlank(message = "所属专业不能为空")
    private String specialty;

    @ApiModelProperty(value = "毕业院校")
    @NotBlank(message = "毕业院校不能为空")
    private String school;

    @ApiModelProperty(value = "入职日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @NotNull(message = "入职日期不能为空")
    private LocalDate beginDate;

    @ApiModelProperty(value = "在职状态")
    @NotBlank(message = "在职状态不能为空")
    private String workState;

    @ApiModelProperty(value = "工号")
    private String workID;

    @ApiModelProperty(value = "合同期限")
    private Double contractTerm;

    @ApiModelProperty(value = "转正日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @NotNull(message = "转正日期不能为空")
    private LocalDate conversionTime;

    @ApiModelProperty(value = "离职日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate notWorkDate;

    @ApiModelProperty(value = "合同起始日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @NotNull(message = "合同起始日期不能为空")
    private LocalDate beginContract;

    @ApiModelProperty(value = "合同终止日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @NotNull(message = "合同终止不能为空")
    private LocalDate endContract;

    @ApiModelProperty(value = "工龄")
    private Integer workAge;

    @ApiModelProperty(value = "工资账套ID")
    @NotNull(message = "工资涨套Id不能为空")
    private Integer salaryId;

    @ApiModelProperty(value = "民族")
    @TableField(exist = false)
    private Nation nation;

    @ApiModelProperty(value = "政治面貌")
    @TableField(exist = false)
    private PoliticsStatus politicsStatus;

    @ApiModelProperty(value = "部门")
    @TableField(exist = false)
    private Department department;

    @ApiModelProperty(value = "职称")
    @TableField(exist = false)
    private JobLevel jobLevel;

    @ApiModelProperty(value = "职位")
    @TableField(exist = false)
    private Position position;
}
