package com.example.roseeducation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = UploadStorage.uploadDir();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir.toAbsolutePath() + "/");
    }
}
