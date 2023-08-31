package com.ocho.what2do.review.service;

import com.ocho.what2do.review.dto.ReviewLikeResponseDto;
import com.ocho.what2do.review.dto.ReviewRequestDto;
import com.ocho.what2do.review.dto.ReviewResponseDto;
import com.ocho.what2do.user.entity.User;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {

    /*
     * 전체 리뷰 조회
     * @return 전체 리뷰 목록
     */
    List<ReviewResponseDto> getAllReviews();

    /*
     * 전체 리뷰 페이징 조회
     * @param pageable 페이징 정보
     * @return 전체 리뷰 페이지
     */
    List<ReviewResponseDto> getAllReviewsPaged(int page, int size, String sorBy, boolean isAsc);

    /*
     * 내 리뷰 페이징 조회
     * @param pageable 페이징 정보
     * @return 내 리뷰 페이지
     */
    List<ReviewResponseDto> getUserReviews(User user, int page, int size, String sortBy, boolean isAsc);

    /*
     * 리뷰 등록
     * @param requestDto 리뷰 등록 정보
     * @param user 작성자 정보
     * @return 생성된 리뷰 정보
     * @param files 첨부파일 정보
     */
    ReviewResponseDto createReview(ReviewRequestDto requestDto, User user, List<MultipartFile> files) throws IOException;

    /*
     * 리뷰 수정
     * @param reviewId 수정할 리뷰 ID
     * @param requestDto 리뷰 수정 정보
     * @param user 작성자 정보
     * @return 수정된 리뷰 정보
     * @param files 첨부파일 정보
     */
    ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto requestDto, User user,  List<MultipartFile> files) throws IOException;

    /*
     * 리뷰 삭제
     * @param reviewId 삭제할 리뷰 ID
     * @param user 작성자 정보
     */
    void deleteReview(Long reviewId, User user);

    /*
     * 리뷰 상세 조회
     * @param reviewId 조회할 리뷰 ID
     * @param user 조회 사용자 정보
     * @return 리뷰 상세 정보
     */
    ReviewResponseDto getReview(Long reviewId, User user);

    /*
     * 리뷰 좋아요
     * @param reviewId 좋아요할 리뷰 ID
     * @param user 사용자 정보
     * @return 업데이트된 리뷰 정보
     */
    ReviewLikeResponseDto likeReview(Long reviewId, User user);

    /*
     * 리뷰 좋아요 취소
     * @param reviewId 좋아요 취소할 리뷰 ID
     * @param user 사용자 정보
     * @return 업데이트된 리뷰 정보
     */
    void unlikeReview(Long reviewId, User user);


}
