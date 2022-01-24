package edu.seu.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.seu.server.pojo.Position;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
public interface IPositionService extends IService<Position> {

    /**
     * 基于缓存实现获取所有职位列表
     * @return 包含所有职位列表实体类的列表
     */
    List<Position> getPositionList();

    /**
     * 清空缓存，增删查操作时调用，默认空实现
     */
    default void cleanUpCache(){}
}
