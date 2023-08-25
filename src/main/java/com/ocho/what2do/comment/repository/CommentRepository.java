package com.ocho.what2do.comment.repository;

import com.ocho.what2do.comment.entity.Comment;
import com.ocho.what2do.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByReview(Review review, Pageable pageable);

}
