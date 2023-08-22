package com.ocho.what2do.storeuser.repository;

import com.ocho.what2do.storeuser.entity.StoreUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreUserRepository extends JpaRepository<StoreUser, Long> {
}
