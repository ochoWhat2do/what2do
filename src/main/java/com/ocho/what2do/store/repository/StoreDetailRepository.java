package com.ocho.what2do.store.repository;

import com.ocho.what2do.store.entity.StoreDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreDetailRepository extends JpaRepository<StoreDetail, Long> {
    boolean existsStoreDetailByStoreKey(String storeKey);
}
