package edu.seu.server.config.util;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 工具类配置
 * @author xuyitjuseu
 */
@Configuration
public class UtilConfiguration {

    /**
     * 注入一个映射器，该映射器可用于dto, pojo等实体类之间的映射
     * @return DozerBeanMapper实现类
     */
    @Bean
    public Mapper getMapper() {
        return new DozerBeanMapper();
    }
}
