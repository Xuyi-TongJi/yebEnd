package edu.seu.server.config.util;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

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

    /**
     * 注入全局日期转换对象
     * @return Converter实现类
     */
    @Bean
    public Converter<String, LocalDate> getDateConverter() {
        return new DateConverter();
    }
}
