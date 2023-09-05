package com.ocho.what2do.review.repository;

import com.ocho.what2do.review.entity.Review;
import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByContent(String content);
    List<Review> findByTitle(String title);
    Page<Review> findByUser(User user, Pageable pageable);

    List<Review> findByUser(User user);

    Page<Review> findAllByStore(Store store, Pageable pageable);

    void deleteAllByUserId(Long userId);

}
