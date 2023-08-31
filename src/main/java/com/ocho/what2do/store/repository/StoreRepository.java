package com.ocho.what2do.store.repository;

import com.ocho.what2do.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsStoreByAddress(String address);

    Optional<Store> findByStoreKey(String storeKey);
}
