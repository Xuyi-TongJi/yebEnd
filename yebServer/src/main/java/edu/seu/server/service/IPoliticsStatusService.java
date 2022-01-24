package edu.seu.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.seu.server.pojo.PoliticsStatus;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
public interface IPoliticsStatusService extends IService<PoliticsStatus> {

    /**
     * 基于缓存实现获取所有政治面貌列表
     * @return 包含所有政治面貌实体类的数组
     */
    List<PoliticsStatus> getPoliticsStatusList();

    /**
     * 清空缓存，在增删改操作时调用，默认空实现
     */
    default void cleanUpCache() {}
}
