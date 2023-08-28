package com.ocho.what2do.review.entity;

import com.ocho.what2do.comment.entity.Comment;
import com.ocho.what2do.common.entity.Timestamped;
import com.ocho.what2do.user.entity.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.Id;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Review extends Timestamped {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "order_no")
    private Long orderNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name="order_no")
    private Long orderNo;

    @OneToMany(mappedBy = "review", orphanRemoval = true)
    private List<ReviewLike> reviewLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @OrderBy("id asc") // 댓글 정렬
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Review(String title, String content, Long orderNo, User user) {
        this.title = title;
        this.content = content;
        this.orderNo = orderNo;
        this.user = user;
    }

    public void updateReview(String content) {
        this.content = content;
    }

    public Collection<ReviewLike> getLikes() {
        return reviewLikeList;
    }

    public void removeLike(ReviewLike likeToRemove) {
        reviewLikeList.remove(likeToRemove);
    }

    public void addLike(ReviewLike newLike) {
        reviewLikeList.add(newLike);
    }
}

