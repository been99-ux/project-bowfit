package com.example.finaln.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 업로드된 파일 접근 가능하도록 설정
        registry.addResourceHandler("/upload/**") // ✅ URL 패턴 유지
                .addResourceLocations("file:///C:/upload/");
    }

}
