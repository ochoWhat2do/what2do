package com.ocho.what2do.comment.repository;

import com.ocho.what2do.comment.entity.Comment;
import com.ocho.what2do.comment.entity.CommentLike;
import com.ocho.what2do.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<Comment, Long> {

//    Optional<CommentLike> findByUserAndComment(User user, Comment comment);

}

