package com.ocho.what2do.comment.entity;

import com.ocho.what2do.common.entity.Timestamped;
import com.ocho.what2do.review.entity.Review;
import com.ocho.what2do.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

  @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
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

}

