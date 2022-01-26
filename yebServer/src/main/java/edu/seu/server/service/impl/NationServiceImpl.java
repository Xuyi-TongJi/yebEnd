package edu.seu.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.server.mapper.NationMapper;
import edu.seu.server.pojo.Nation;
import edu.seu.server.service.INationService;
import edu.seu.server.util.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
public class NationServiceImpl extends ServiceImpl<NationMapper, Nation> implements INationService {

    private final RedisTemplate<String, Object> redisTemplate;

    public NationServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Nation> listInCache() {
        String keyName = RedisUtil.NATION_LIST;
        return listInCache(keyName, redisTemplate);
    }

    @Override
    public void cleanupCache() {

    }
}
