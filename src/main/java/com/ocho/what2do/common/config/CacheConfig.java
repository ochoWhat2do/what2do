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
    cacheManager.setAllowNullValues(true);
    cacheManager.setCacheNames(List.of("store_all", "store_one"));
    return cacheManager;
  }

  @Scheduled(fixedDelay = 21600000)   // 6시간 간격으로 캐시메모리 정리 (1초는 fixedDelay = 1000)
  public void evictCache() {
    Cache cache1 = cacheManager().getCache("store_all");
    if (cache1 != null) {
      cache1.clear();
    }
    ;
    Cache cache2 = cacheManager().getCache("store_one");
    if (cache2 != null) {
      cache2.clear();
    }
    ;
    log.info("캐시 메모리 삭제");
  }
}
