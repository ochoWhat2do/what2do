package com.ocho.what2do.store.repository;

import com.ocho.what2do.store.entity.ApiStore;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiStoreRepository extends JpaRepository<ApiStore, Long> {
    boolean existsApiStoreByStoreKey(String storeKey);

    //List<ApiStore> findAllByStoreKey(String storeKey);

    @Cacheable(cacheNames = "store_category", key = "#category")
    Page<ApiStore> findByCategoryContains(String category, Pageable pageable);

    Page<ApiStore> findAll(Pageable pageable);

    Page<ApiStore> findAllByTitleLike(String title, Pageable pageable);

    List<ApiStore> findAllByCategoryContains(String category);

    Optional<ApiStore> findByStoreKey(String storeKey);
}