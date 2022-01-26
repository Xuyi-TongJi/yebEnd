package edu.seu.server.service;

import edu.seu.server.pojo.JobLevel;
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
public interface IJobLevelService extends ICacheService<JobLevel> {

    /**
     * 查询所有可用的JobLevel集合
     * @return 包含所有可用的JobLevel实体类的集合
     */
    List<JobLevel> listEnabled();
}
