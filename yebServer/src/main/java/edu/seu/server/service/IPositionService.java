package edu.seu.server.service;

import edu.seu.server.pojo.Position;
import edu.seu.server.service.cache.ICacheService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
public interface IPositionService extends ICacheService<Position> {

    /**
     * 查询所有可用的Position集合
     * @return 包含所有可用position实体类的集合
     */
    List<Position> listEnabled();
}
