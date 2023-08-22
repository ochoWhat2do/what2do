package com.ocho.what2do.review.controller;

import com.ocho.what2do.common.dto.ApiResponseDto;
import com.ocho.what2do.common.security.UserDetailsImpl;
import com.ocho.what2do.review.dto.ReviewRequestDto;
import com.ocho.what2do.review.dto.ReviewResponseDto;
import com.ocho.what2do.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "리뷰 API", description = "리뷰 기능과 관련된 API 정보를 담고 있습니다.")

public class ReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "리뷰 조회", description = "현재 선택한 리뷰의 내부를 조회합니다.")
    @GetMapping("/reviews/{review_Id}")
    public ResponseEntity<ReviewResponseDto> showReview(@PathVariable("review_Id") Long review_Id) {
        ReviewResponseDto responseDto = reviewService.showReview(review_Id);
        return ResponseEntity.ok().body(responseDto);
    }

    @Operation(summary = "전체 리뷰 조회", description = "모든 리뷰 정보를 조회합니다.")
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews() {
        List<ReviewResponseDto> responseDtoList = reviewService.getAllReviews();
        return ResponseEntity.ok(responseDtoList);
    }
    @Operation(summary = "전체 리뷰 페이징 조회", description = "전체 리뷰를 페이징하여 조회합니다.")
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getAllReviewsPaged(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc)
    {
        List<ReviewResponseDto> responseDto = reviewService.getAllReviewsPaged(page -1, size, sortBy, isAsc);
        return ResponseEntity.ok().body(responseDto);

    }

    @Operation(summary = "리뷰 등록", description = "새로운 리뷰를 등록합니다.")
    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(
            @Valid @RequestBody ReviewRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReviewResponseDto responseDto = reviewService.createReview(requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "리뷰 수정", description = "선택한 리뷰의 내용을 수정합니다.")
    @PutMapping("/reviews/{review_Id}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable("review_Id") Long reviewId,
            @Valid @RequestBody ReviewRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReviewResponseDto responseDto = reviewService.updateReview(reviewId, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(responseDto);

    }

    @Operation(summary = "리뷰 삭제", description = "선택한 리뷰를 삭제합니다.")
    @DeleteMapping("/reviews/{review_Id}")
    public ResponseEntity<ApiResponseDto> deleteReview(
            @PathVariable("review_Id") Long reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewService.deleteReview(reviewId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "리뷰 삭제 성공"));
    }


    @Operation(summary = "리뷰 상세 조회", description = "선택한 리뷰의 상세 정보를 조회합니다.")
    @GetMapping("/reviews/{review_Id}")
    public ResponseEntity<ReviewResponseDto> getReviewDetail(
            @PathVariable("review_Id") Long reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReviewResponseDto responseDto = reviewService.getReviewDetail(reviewId, userDetails.getUser());
        return ResponseEntity.ok().body(responseDto);
    }
    @Operation(summary = "리뷰 좋아요", description = "리뷰에 좋아요를 표시합니다.")
    @PostMapping("/reviews/{review_Id}/likes")
    public ResponseEntity<ReviewResponseDto> likeReview(
            @PathVariable("review_Id") Long reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReviewResponseDto responseDto = reviewService.likeReview(reviewId, userDetails.getUser());
        return ResponseEntity.ok().body(responseDto);
    }

    @Operation(summary = "리뷰 좋아요 취소", description = "리뷰의 좋아요를 취소합니다.")
    @DeleteMapping("/reviews/{review_Id}/likes")
    public ResponseEntity<ReviewResponseDto> unlikeReview(
            @PathVariable("review_Id") Long reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReviewResponseDto responseDto = reviewService.unlikeReview(reviewId, userDetails.getUser());
        return ResponseEntity.ok().body(responseDto);
    }

}