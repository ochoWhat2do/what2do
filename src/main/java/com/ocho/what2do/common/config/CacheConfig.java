package com.ocho.what2do.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j(topic = "캐시 메모리")
@EnableCaching
@EnableScheduling
@Configuration
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setAllowNullValues(false);
        cacheManager.setCacheNames(List.of("store"));
        return cacheManager;
    }

    @Scheduled(fixedDelay = 21600000)   // 6시간 간격으로 캐시메모리 정리 (1초는 fixedDelay = 1000)
    public void evictCache() {
        Cache cache = cacheManager().getCache("store");
        if (cache != null) {
            cache.clear();
        };
        log.info("캐시 메모리 삭제");
    }
}
