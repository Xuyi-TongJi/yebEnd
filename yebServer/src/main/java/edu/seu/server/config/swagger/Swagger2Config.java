package edu.seu.server.config.swagger;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger2注解
 * @author xuyitjuseu
 */
@Configuration
@EnableSwagger2
@Getter
public class Swagger2Config {

    @Value("${url}")
    private String contactAddress;

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("edu.seu.server.controller"))
                .paths(PathSelectors.any())
                .build()
                // 使用swagger-ui进行接口测试时的配置
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes());
    }

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("yeb接口文档")
                .description("yeb接口文档")
                .contact(new Contact("xuyitjuseu", contactAddress + "/doc.html", "xxx@xx.com"))
                .version("1.0")
                .build();
    }

    private List<ApiKey> securitySchemes() {
        List<ApiKey> result = new ArrayList<>();
        ApiKey apiKey = new ApiKey("Authorization", "Authorization", "Header");
        result.add(apiKey);
        return result;
    }

    private List<springfox.documentation.spi.service.contexts.SecurityContext> securityContexts() {
        List<SecurityContext> result = new ArrayList<>();
        result.add(getContextByPath());
        return result;
    }

    private SecurityContext getContextByPath() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEach");
        AuthorizationScope[] scopes = new AuthorizationScope[1];
        scopes[0] = authorizationScope;
        List<SecurityReference> list = new ArrayList<>();
        list.add(new SecurityReference("Authorization", scopes));
        // 测试接口为/test/.*
        return SecurityContext.builder().securityReferences(list).forPaths(PathSelectors.regex("/test/.*")).build();
    }
}
