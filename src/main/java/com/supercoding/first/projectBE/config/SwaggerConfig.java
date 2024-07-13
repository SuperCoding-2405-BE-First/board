package com.supercoding.first.projectBE.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI(){
    Info info = new Info()
        .title("2405 백엔드 1차 프로젝트")
        .description("Spring Boot를 이용한 게시판 만들기")
        .contact(new Contact().name("슈퍼코딩"));
    return new OpenAPI()
        .info(info);
  }
}