package edu.seu.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.seu.server.pojo.JobLevel;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuyitjuseu
 * @since 2022-01-14
 */
public interface IJobLevelService extends IService<JobLevel> {

    /**
     * 基于缓存实现获取所有职级列表
     * @return 包含所有职级实体类的列表
     */
    List<JobLevel> getJobLevelList();

    /**
     * 清空缓存，增删改操作时调用，默认空实现
     */
    default void cleanUpCache(){}
}
