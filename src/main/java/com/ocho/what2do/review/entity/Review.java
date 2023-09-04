package com.ocho.what2do.review.entity;

import com.ocho.what2do.comment.entity.Comment;
import com.ocho.what2do.common.entity.Timestamped;
import com.ocho.what2do.common.file.S3FileDto;
import com.ocho.what2do.review.dto.ReviewRequestDto;
import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.user.entity.User;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Type;
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

    @Column(nullable = false) // 별점 필드 추가
    private int rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    // 파일 첨부 컬럼(여러건 첨부 가능)
    @Convert(converter = S3FileDto.S3FileDtoConverter.class)
    @Type(JsonType.class)
    @Column(name="attachment",columnDefinition = "json")
    private List<S3FileDto> attachment;

    @OneToMany(mappedBy = "review", orphanRemoval = true)
    private List<ReviewLike> reviewLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @OrderBy("id asc") // 댓글 정렬
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Review(String title, String content, Long orderNo, User user, List<S3FileDto> attachment, Store store, int rate) {
        this.title = title;
        this.content = content;
        this.orderNo = orderNo;
        this.user = user;
        this.attachment = attachment;
        this.store = store;
        this.rate = rate;

    }

    public void updateReview(ReviewRequestDto reviewRequestDto) {
        this.content = reviewRequestDto.getContent();
        this.title = reviewRequestDto.getTitle();
        this.attachment = reviewRequestDto.getAttachment();
        this.orderNo = reviewRequestDto.getOrderNo();
        this.rate = reviewRequestDto.getRate();
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

