package edu.seu.server.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatisPlus配置类
 * @author xuyitjuseu
 */
@Configuration
public class MybatisPlusConfiguration {

    /**
     * 注入MyBatisPlus分页插件
     * @return MyBatisPlus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
