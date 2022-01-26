package edu.seu.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.seu.server.mapper.JobLevelMapper;
import edu.seu.server.pojo.JobLevel;
import edu.seu.server.service.IJobLevelService;
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
public class JobLevelServiceImpl extends ServiceImpl<JobLevelMapper, JobLevel> implements IJobLevelService {

    private final RedisTemplate<String, Object> redisTemplate;

    public JobLevelServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<JobLevel> listEnabled() {
        String keyName = RedisUtil.JOB_LEVEL_LIST;
        List<JobLevel> results = (List<JobLevel>) redisTemplate.opsForValue().get(keyName);
        if (CollectionUtils.isEmpty(results)) {
            results = super.list(new QueryWrapper<JobLevel>().eq("enabled", true));
            redisTemplate.opsForValue().set(keyName, results);
        }
        return results;
    }

    @Override
    public void cleanupCache() {
        redisTemplate.delete(RedisUtil.JOB_LEVEL_LIST);
    }
}
