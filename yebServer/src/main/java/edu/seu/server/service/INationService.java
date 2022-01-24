package edu.seu.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.seu.server.pojo.Nation;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
public interface INationService extends IService<Nation> {

    /**
     * 通过缓存实现获取所有民族列表
     * @return 包含所有民族实体类的列表
     */
    List<Nation> getNationList();

    /**
     * 清空缓存，默认空实现
     */
    default void cleanUpCache(){}
}
