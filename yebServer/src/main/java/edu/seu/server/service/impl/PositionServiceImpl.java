package edu.seu.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.server.mapper.PositionMapper;
import edu.seu.server.pojo.Position;
import edu.seu.server.service.IPositionService;
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
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements IPositionService {

    private final RedisTemplate<String, Object> redisTemplate;

    public PositionServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Position> getPositionList() {
        String keyName = RedisUtil.POSITION_LIST;
        List<Position> results = (List<Position>) redisTemplate.opsForValue().get(keyName);
        if (CollectionUtils.isEmpty(results)) {
            results = list(new QueryWrapper<Position>().eq("enabled", true));
            redisTemplate.opsForValue().set(keyName, results);
        }
        return results;
    }
}
