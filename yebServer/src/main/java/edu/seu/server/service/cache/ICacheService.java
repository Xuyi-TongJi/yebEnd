package edu.seu.server.service.cache;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Redis缓存服务接口，实现该接口的Service实现类必须在实现增删查操作时清除缓存
 * 该接口同时实现MybatisPlus的IService类
 * @author xuyitjuseu
 */
public interface ICacheService<T> extends IService<T> {

    /**
     * 清除缓存，必须实现
     */
    void cleanupCache();

    /**
     * 默认重写的save方法，用于添加操作
     * @param entity 待添加的实体类
     * @return 是否添加成功的布尔值
     */
    @Override
    default boolean save(T entity) {
        cleanupCache();
        return IService.super.save(entity);
    }

    /**
     * 默认重写的updateById方法，用于更新操作
     * @param entity 待更新的实体类
     * @return 是否更新成功的布尔值
     */
    @Override
    default boolean updateById(T entity) {
        cleanupCache();
        return IService.super.updateById(entity);
    }

    /**
     * 默认重写的removeById方法，用于删除操作
     * @param id 待删除的id
     * @return 是否删除成功的布尔值
     */
    @Override
    default boolean removeById(Serializable id) {
        cleanupCache();
        return IService.super.removeById(id);
    }

    /**
     * 默认重写的removeByIds方法，用于批量删除操作
     * @param idList 批量删除的id集合
     * @return 是否批量删除成功
     */
    @Override
    default boolean removeByIds(Collection<? extends Serializable> idList) {
        cleanupCache();
        return IService.super.removeByIds(idList);
    }

    /**
     * 根据keyName尝试从缓存中获取所有记录实体类集合
     * @param keyName 键
     * @param redisTemplate redis数据访问类
     * @return 包含数据库所有实体类的集合
     */
    default List<T> listInCache(String keyName, RedisTemplate<String, Object> redisTemplate) {
        List<T> result = (List<T>) redisTemplate.opsForValue().get(keyName);
        if (CollectionUtils.isEmpty(result)) {
            result = IService.super.list();
            redisTemplate.opsForValue().set(keyName, result);
        }
        return result;
    }

    /**
     * 将列表置于缓存中，默认不放入缓存中
     * @return 包含实体类的列表
     */
    default List<T> listInCache(){
        return list();
    }
}
