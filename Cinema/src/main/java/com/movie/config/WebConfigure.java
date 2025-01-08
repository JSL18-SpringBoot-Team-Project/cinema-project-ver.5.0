package com.movie.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigure implements WebMvcConfigurer {

    @Value("${file.upload.poster-dir}")
    private String posterUploadDir;

    @Value("${file.upload.event-dir}")
    private String eventUploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/poster/**")
                .addResourceLocations("file:" + posterUploadDir + "/");
        registry.addResourceHandler("/event/**")
                .addResourceLocations("file:" + eventUploadDir + "/");
        // 필요한 경우 추가적인 핸들러 설정
    }
}
