package cn.bdqn.his.common.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.Api;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Value("${swagger2.title}")
    private String swagger2Title;
    @Value("${swagger2.enable}")
    private boolean swagger2Enable;
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                //有@Api注解的类才生成文档
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .apis(RequestHandlerSelectors.basePackage("cn.bdqn.his"))
                .paths(PathSelectors.any())
                .build()
                //生产环境可以配置成false禁用
                .enable(swagger2Enable)
                .apiInfo(new ApiInfoBuilder()
                        .version("1.0")
                        .title(swagger2Title)
                        .contact(new Contact("天天向上","https://gitee.com/ttxs008","john.yi@qq.com"))
                        .build());
    }
}
