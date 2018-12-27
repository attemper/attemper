package com.thor.config.conf;

import com.thor.common.constant.CommonConstants;
import com.thor.common.property.CommonProperty;
import com.thor.config.constant.ConfigStrConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger2 配置
 * @author ldang
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Value("${swagger2.contact.name:ldang}")
    private String contactName;

    @Value("${swagger2.contact.url:www.sse.com.cn}")
    private String contactUrl;

    @Value("${swagger2.contact.email:ldang@sse.com.cn}")
    private String contactEmail;

    @Bean
    public Docket apiV1() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .contact(new Contact(contactName, contactUrl, contactEmail))
                .title(CommonProperty.getValue(ConfigStrConstants.SWAGGER2_TITLE))
                .description(CommonProperty.getValue(ConfigStrConstants.SWAGGER2_DESCRIPTION))
                .version("v1")
                .build();
        Parameter tokenParameter = new ParameterBuilder()
                .parameterType(CommonConstants.header)
                .name(CommonConstants.token)
                .modelRef(new ModelRef("String"))
                .description(CommonProperty.getValue(ConfigStrConstants.SWAGGER2_ACCESS_TOKEN_DESCRIPTION))
                .build();
        List<Parameter> parameterList = new ArrayList<>();
        parameterList.add(tokenParameter);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .globalOperationParameters(parameterList)
                .groupName(apiInfo.getVersion())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }

}
