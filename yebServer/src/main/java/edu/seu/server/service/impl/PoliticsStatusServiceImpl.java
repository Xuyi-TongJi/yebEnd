package edu.seu.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.server.mapper.PoliticsStatusMapper;
import edu.seu.server.pojo.PoliticsStatus;
import edu.seu.server.service.IPoliticsStatusService;
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
public class PoliticsStatusServiceImpl extends ServiceImpl<PoliticsStatusMapper, PoliticsStatus> implements IPoliticsStatusService {

    private final RedisTemplate<String, Object> redisTemplate;

    public PoliticsStatusServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<PoliticsStatus> listInCache() {
        String keyName = RedisUtil.POLITIC_STATUS_LIST;
        return listInCache(keyName, redisTemplate);
    }

    @Override
    public void cleanupCache() {

    }
}
