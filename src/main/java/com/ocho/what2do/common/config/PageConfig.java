package com.ocho.what2do.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

public class PageConfig {

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customizer() {
        return p -> {
            p.setOneIndexedParameters(true);    // 1 페이지 부터 시작
        };
    }
}
