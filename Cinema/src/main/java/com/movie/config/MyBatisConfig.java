package com.movie.config;

import com.movie.handler.LocalDateTimeTypeHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@MapperScan("com.movie.mapper")
public class MyBatisConfig {
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.getTypeHandlerRegistry().register(LocalDateTime.class, new LocalDateTimeTypeHandler());
    }
}
