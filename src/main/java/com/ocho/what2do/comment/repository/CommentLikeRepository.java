package com.ocho.what2do.comment.repository;

import com.ocho.what2do.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends JpaRepository<Comment, Long> {


}

