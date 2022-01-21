package edu.seu.server.service;

import com.google.common.io.ByteProcessor;
import edu.seu.server.pojo.Department;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  部门服务类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
public interface IDepartmentService extends IService<Department> {

    /**
     * 获得所有部门的列表
     * @return 包含所有部门实体类的列表
     */
    List<Department> getDepartmentList();

    /**
     * 新增部门
     * @param department 待新增的部门实体类
     * @return 存储新增结果信息的实体类，使用存储过程时，该对象与传入的实体类为同一对象
     */
    Department addDepartment(Department department);

    /**
     * 删除部门
     * @param id 待删除的部门id
     * @return 存储新增结果信息的实体类，使用存储过程时，该对象与传入的实体类为同一对象
     */
    Department deleteDepartment(Integer id);
    /**
     * 清空缓存
     */
    default void cleanUpCache() {
    }


}
