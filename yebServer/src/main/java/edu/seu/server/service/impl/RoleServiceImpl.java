package edu.seu.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.server.mapper.RoleMapper;
import edu.seu.server.pojo.Role;
import edu.seu.server.service.IRoleService;
import edu.seu.server.util.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

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

    @Resource
    private RoleMapper roleMapper;

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
    public List<Role> getRoleWithMenus() {
        String keyName = RedisUtil.ROLE_LIST_WITH_MENU;
        List<Role> results = (List<Role>) redisTemplate.opsForValue().get(keyName);
        if (CollectionUtils.isEmpty(results)) {
            results = roleMapper.getRoleWithMenus();
            redisTemplate.opsForValue().set(keyName, results);
        }
        return results;
    }

    @Override
    public void cleanupCache() {
        redisTemplate.delete(RedisUtil.ROLE_LIST);
        redisTemplate.delete(RedisUtil.ROLE_LIST_WITH_MENU);
        redisTemplate.delete(RedisUtil.MENU_WITH_ROLE);
    }
}
