package com.ocho.what2do.store.repository;

import com.ocho.what2do.store.entity.Store;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    @Cacheable("store_one")
    boolean existsStoreByStoreKey(String storeKey);

    List<Store> getStoreListByStoreKey(String storeKey);

    Optional<Store> getStoreByStoreKey(String storeKey);

}
