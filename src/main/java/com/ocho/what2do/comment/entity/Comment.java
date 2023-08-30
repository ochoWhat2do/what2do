package com.ocho.what2do.comment.entity;

import com.ocho.what2do.comment.dto.CommentCreateRequestDto;
import com.ocho.what2do.comment.dto.CommentEditRequestDto;
import com.ocho.what2do.common.entity.Timestamped;
import com.ocho.what2do.review.entity.Review;
import com.ocho.what2do.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "comment")
public class Comment extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String content; // 댓글 내용

  @ManyToOne
  @JoinColumn(name = "review_id")
  private Review review;

  // 관리자 사용자 접근권한 때문에 작성자는 업데이트 하지 않음
  @ManyToOne
  @JoinColumn(name = "user_id", updatable = false)
  private User user; // 작성자

  @OneToMany(mappedBy = "comment", orphanRemoval = true)
  private List<CommentLike> commentLikes = new ArrayList<>();

  // 부모
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Comment parent;


  // 자식
  @Builder.Default
  @OneToMany(mappedBy = "parent", orphanRemoval = true)
  private List<Comment> children = new ArrayList<>();


  // 부모 댓글 수정
  public void updateParent(Comment parent) {
    this.parent = parent;
  }

  @Override
  public LocalDateTime getModifiedAt() {
    return super.getModifiedAt();
  }

  @Override
  public LocalDateTime getCreatedAt() {
    return super.getCreatedAt();
  }

  // Setter
  public void setContent(String content) {
    this.content = content;
  }

  public void setReview(Review review) {
    this.review = review;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Comment(CommentCreateRequestDto requestDto) {
    this.content = requestDto.getContent();
  }

  public void editComment(CommentEditRequestDto requestDto) {
    this.content = requestDto.getContent();
  }

  public void addLike(CommentLike newLike) {
    commentLikes.add(newLike);
  }

}

