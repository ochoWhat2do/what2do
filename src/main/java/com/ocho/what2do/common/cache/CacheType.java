package com.ocho.what2do.common.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {
    STORE_ALL("store_all", 12, 10000),
    STORE_CATEGORY("store_category", 12, 10000);

    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;
}
