package edu.seu.server.service.impl;

import edu.seu.server.pojo.Department;
import edu.seu.server.mapper.DepartmentMapper;
import edu.seu.server.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.server.util.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    private final DepartmentMapper departmentMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    public DepartmentServiceImpl(DepartmentMapper departmentMapper,
                                 RedisTemplate<String, Object> redisTemplate) {
        this.departmentMapper = departmentMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Department> getDepartmentList() {
        String keyName = RedisUtil.DEPARTMENT_LIST;
        List<Department> departmentList = (List<Department>)redisTemplate.opsForValue().get(keyName);
        if (CollectionUtils.isEmpty(departmentList)) {
            departmentList = departmentMapper.getDepartmentList(-1);
            redisTemplate.opsForValue().set(keyName, departmentList);
        }
        return departmentList;
    }
}
