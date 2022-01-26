package edu.seu.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.server.mapper.RoleMapper;
import edu.seu.server.pojo.Role;
import edu.seu.server.service.IRoleService;
import edu.seu.server.util.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RoleServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Role> listInCache() {
        String keyName = RedisUtil.ROLE_LIST;
        return listInCache(keyName, redisTemplate);
    }

    @Override
    public List<Integer> getRidList() {
        String keyName = RedisUtil.ROLE_ID_LIST;
        List<Integer> ridList = (List<Integer>) redisTemplate.opsForValue().get(keyName);
        if (CollectionUtils.isEmpty(ridList)) {
            ridList = this.list().stream().map(Role::getId).collect(Collectors.toList());
            redisTemplate.opsForValue().set(keyName, ridList);
        }
        return ridList;
    }

    @Override
    public void cleanupCache() {
        redisTemplate.delete(RedisUtil.ROLE_LIST);
        redisTemplate.delete(RedisUtil.ROLE_ID_LIST);
    }

}
