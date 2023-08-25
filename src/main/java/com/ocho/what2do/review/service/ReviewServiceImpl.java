package com.ocho.what2do.review.service;

import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.message.CustomErrorCode;
import com.ocho.what2do.review.dto.ReviewRequestDto;
import com.ocho.what2do.review.dto.ReviewResponseDto;
import com.ocho.what2do.review.entity.Review;
import com.ocho.what2do.review.entity.ReviewLike;
import com.ocho.what2do.review.repository.ReviewLikeRepository;
import com.ocho.what2do.review.repository.ReviewRepository;
import com.ocho.what2do.user.entity.User;
import com.ocho.what2do.user.entity.UserRoleEnum;
import com.ocho.what2do.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReviewLikeRepository reviewLikeRepository;

    @Override
    @Transactional(readOnly = true)
    public ReviewResponseDto showReview(Long reviewId) {
        Review review = findReview(reviewId);
        return new ReviewResponseDto(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getAllReviewsPaged(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return reviewRepository.findAll(pageable).stream().map(ReviewResponseDto::new).toList();
    }

    @Override
    @Transactional
    public ReviewResponseDto createReview(ReviewRequestDto requestDto, User user) {
        user = findUser(user.getId());


        if (reviewRepository.findByContent(requestDto.getContent()).isPresent()) {
            throw new CustomException(CustomErrorCode.REVIEW_ALREADY_EXIST, null);
        }

        Review review = Review.builder()
                .content(requestDto.getContent())
                .user(user)
                .build();

        reviewRepository.save(review);

        return new ReviewResponseDto(review);
    }

    @Override
    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto requestDto, User user) {
        Review review = findReview(reviewId);
        confirmUser(review, user);

        review.updateReview(requestDto.getContent());
        return new ReviewResponseDto(review);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId, User user) {
        Review review = findReview(reviewId);
        confirmUser(review, user);

        reviewRepository.deleteById(reviewId);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponseDto getReviewDetail(Long reviewId, User user) {
        Review review = findReview(reviewId);
        return new ReviewResponseDto(review);
    }

    @Override
    @Transactional
    public ReviewResponseDto likeReview(Long reviewId, User user) {
        Review review = findReview(reviewId);

        // 좋아요 처리 로직 추가
        Optional<ReviewLike> existingLike = reviewLikeRepository.findByUserAndReview(user, review);
        if (existingLike.isPresent()) {
            throw new CustomException(CustomErrorCode.REVIEW_ALREADY_LIKED, null);
        }

        // 새로운 좋아요 엔티티 생성 및 추가
        ReviewLike newLike = new ReviewLike(user, review);
        review.addLike(newLike);

        return new ReviewResponseDto(reviewRepository.save(review));
    }

    @Override
    @Transactional
    public ReviewResponseDto unlikeReview(Long reviewId, User user) {
        Review review = findReview(reviewId);

        // 좋아요 취소 처리 로직 추가
        ReviewLike likeToRemove = reviewLikeRepository.findByUserAndReview(user, review)
                .orElseThrow(() -> new CustomException(CustomErrorCode.REVIEW_NOT_LIKED, null));

        // 해당 좋아요 엔티티 제거
        review.removeLike(likeToRemove);

        return new ReviewResponseDto(reviewRepository.save(review));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND, null));
    }

    private Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.REVIEW_NOT_FOUND, null));
    }

    private void confirmUser(Review review, User user) {
        if (!review.getUser().getId().equals(user.getId())
                && !user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED_REQUEST, null);
        }
    }
}

