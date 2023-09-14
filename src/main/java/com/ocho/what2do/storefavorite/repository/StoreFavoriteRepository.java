package com.ocho.what2do.storefavorite.repository;

import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.storefavorite.entity.StoreFavorite;
import com.ocho.what2do.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreFavoriteRepository extends JpaRepository<StoreFavorite, Long> {
    boolean existsByUserAndStore(User user, Store store);

    Optional<StoreFavorite> findByUserAndStore(User user, Store store);

    List<StoreFavorite> findAllByUserId(Long userId);

}
