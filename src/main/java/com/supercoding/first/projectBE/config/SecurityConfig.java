package com.supercoding.first.projectBE.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public CorsConfigurationSource corsConfigurationSource(){
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    CorsConfiguration configuration = new CorsConfiguration();
    // 서버가 응답할 때 json을 자바스크립트에서 처리할 수 있게 할지 설정
    configuration.setAllowCredentials(true);
    // 모든 ip에 응답을 허용
    configuration.setAllowedOrigins(List.of("*"));
    // 모든 header에 응답을 허용
    configuration.setAllowedHeaders(List.of("*"));
    // 모든 method(GET, POST, PUT, DELETE)에 응답을 허용
    configuration.setAllowedMethods(List.of("*"));

    source.registerCorsConfiguration("/api/**", configuration);
    return source;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http
        .cors(configurer ->
            configurer.configurationSource(corsConfigurationSource())
        )
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/api/**").permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(formLogin -> formLogin
            .loginPage("/login")
            .permitAll()
        )
        .rememberMe(Customizer.withDefaults());

    return http.build();
  }
}

