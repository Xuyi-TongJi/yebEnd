package edu.seu.server.service;

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
}
