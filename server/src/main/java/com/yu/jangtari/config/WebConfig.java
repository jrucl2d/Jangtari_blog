package com.yu.jangtari.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * MockMvc 테스트 한글 깨지는 현상 해결
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.stream()
            .filter(MappingJackson2HttpMessageConverter.class::isInstance)
            .findFirst()
            .ifPresent(converter -> ((MappingJackson2HttpMessageConverter) converter)
                .setDefaultCharset(StandardCharsets.UTF_8));
    }
}
