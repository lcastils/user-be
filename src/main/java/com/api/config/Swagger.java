package com.api.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger {                                    
    @Bean
    public Docket api() { 
        List<ResponseMessage> responsesGet = new ArrayList<>();
        List<ResponseMessage> responsesPost = new ArrayList<>();
        List<ResponseMessage> responsesPut = new ArrayList<>();
        List<ResponseMessage> responsesDelete = new ArrayList<>();
        ResponseMessage code500 = new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(HttpStatus.INTERNAL_SERVER_ERROR.name()).build();

        ResponseMessage code404 = new ResponseMessageBuilder().code(HttpStatus.NOT_FOUND.value())
                .message(HttpStatus.NOT_FOUND.name()).build();

        ResponseMessage code200 = new ResponseMessageBuilder().code(HttpStatus.OK.value()).message(HttpStatus.OK.name())
                .build();

        ResponseMessage code201 = new ResponseMessageBuilder().code(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.name()).build();

        ResponseMessage code401 = new ResponseMessageBuilder().code(HttpStatus.UNAUTHORIZED.value())
                .message(HttpStatus.UNAUTHORIZED.name()).build();

        ResponseMessage code403 = new ResponseMessageBuilder().code(HttpStatus.FORBIDDEN.value())
                .message(HttpStatus.FORBIDDEN.name()).build();
        responsesGet.add(code200);
        responsesGet.add(code401);
        responsesGet.add(code403);
        responsesGet.add(code404);
        responsesGet.add(code500);

        responsesPost.add(code201);
        responsesPost.add(code401);
        responsesPost.add(code403);
        responsesPost.add(code500);

        responsesPut.add(code200);
        responsesPut.add(code401);
        responsesPut.add(code403);
        responsesPut.add(code404);
        responsesPut.add(code500);

        responsesDelete.add(code200);
        responsesDelete.add(code401);
        responsesDelete.add(code403);
        responsesDelete.add(code404);
        responsesDelete.add(code500);

        ApiInfo apiInfo = new ApiInfoBuilder().title("Crud Users springboot ").version("1.0")
                .description("Api Documentation").termsOfServiceUrl("urn:tos").build();

        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo).select()
                .apis(RequestHandlerSelectors.basePackage("com.api")).paths(PathSelectors.any()).build()
                .useDefaultResponseMessages(true).globalResponseMessage(RequestMethod.GET, responsesGet)
                .globalResponseMessage(RequestMethod.POST, responsesPost)
                .globalResponseMessage(RequestMethod.PUT, responsesPut)
                .globalResponseMessage(RequestMethod.DELETE, responsesDelete);                                         
    }
}