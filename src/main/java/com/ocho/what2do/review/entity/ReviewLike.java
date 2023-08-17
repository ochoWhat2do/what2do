package com.ocho.what2do.review.entity;

import com.ocho.what2do.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "review_like")
public class ReviewLike {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "review_id")
  private Review review;

  // 관리자 사용자 접근권한 때문에 작성자는 업데이트 하지 않음
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", updatable=false)
  private User user; // 작성자

  @Builder
  public ReviewLike(User user, Review review) {
    this.user = user;
    this.review = review;
  }
}
