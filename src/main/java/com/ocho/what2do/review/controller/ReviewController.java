package com.ocho.what2do.review.controller;

import com.ocho.what2do.common.dto.ApiResponseDto;
import com.ocho.what2do.common.security.UserDetailsImpl;
import com.ocho.what2do.review.dto.ReviewLikeResponseDto;
import com.ocho.what2do.review.dto.ReviewRequestDto;
import com.ocho.what2do.review.dto.ReviewResponseDto;
import com.ocho.what2do.review.service.ReviewService;
import com.ocho.what2do.user.entity.User;
import com.ocho.what2do.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "리뷰 API", description = "리뷰 기능과 관련된 API 정보를 담고 있습니다.")

public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;


    @Operation(summary = "전체 리뷰 페이징 조회", description = "전체 리뷰를 페이징하여 조회합니다.")
    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews(
            @PathVariable("storeId") Long storeId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc) {
        List<ReviewResponseDto> responseDto = reviewService.getAllReviews(storeId, page - 1, size, sortBy, isAsc);
        return ResponseEntity.ok().body(responseDto);

    }

    @Operation(summary = "특정 사용자가 작성한 리뷰 페이징 조회", description = "특정 사용자가 작성한 리뷰를 페이징하여 조회합니다.")
    @GetMapping("/users/{userId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getUserReviews(
            @PathVariable("userId") Long userId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userService.findUserById(userId);
        List<ReviewResponseDto> responseDto = reviewService.getUserReviews(user, page - 1, size, sortBy, isAsc);
        return ResponseEntity.ok().body(responseDto);
    }


    @Operation(summary = "리뷰 등록", description = "새로운 리뷰를 등록합니다.")
    @PostMapping("/stores/{storeId}/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(
            @PathVariable("storeId") Long storeId,
            @RequestPart @Valid ReviewRequestDto requestDto,
            @RequestPart(required = false) List<MultipartFile> files,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        ReviewResponseDto responseDto = reviewService.createReview(storeId, requestDto, userDetails.getUser(), files);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "리뷰 수정", description = "선택한 리뷰의 내용을 수정합니다.")
    @PatchMapping("/stores/{storeId}/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable("storeId") Long storeId,
            @PathVariable("reviewId") Long reviewId,
            @Valid @RequestPart ReviewRequestDto requestDto,
            @RequestPart(required = false) List<MultipartFile> files,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        ReviewResponseDto responseDto = reviewService.updateReview(storeId, reviewId, requestDto, userDetails.getUser(), files);
        return ResponseEntity.ok().body(responseDto);

    }

    @Operation(summary = "리뷰 삭제", description = "선택한 리뷰를 삭제합니다.")
    @DeleteMapping("/stores/{storeId}/reviews/{reviewId}")
    public ResponseEntity<ApiResponseDto> deleteReview(
            @PathVariable("reviewId") Long reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewService.deleteReview(reviewId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "리뷰 삭제 성공"));
    }


    @Operation(summary = "리뷰 상세 조회", description = "선택한 리뷰의 상세 정보를 조회합니다.")
    @GetMapping("/stores/{storeId}/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getReview(
            @PathVariable("storeId") Long storeId,
            @PathVariable("reviewId") Long reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReviewResponseDto responseDto = reviewService.getReview(reviewId, userDetails.getUser());
        return ResponseEntity.ok().body(responseDto);
    }

    @Operation(summary = "리뷰 좋아요", description = "리뷰에 좋아요를 표시합니다.")
    @PostMapping("/stores/{storeId}/reviews/{reviewId}/likes")
    public ResponseEntity<ReviewLikeResponseDto> likeReview(
            @PathVariable("reviewId") Long reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReviewLikeResponseDto responseDto = reviewService.likeReview(reviewId, userDetails.getUser());
        return ResponseEntity.ok().body(responseDto);
    }

    @Operation(summary = "리뷰 좋아요 취소", description = "리뷰의 좋아요를 취소합니다.")
    @DeleteMapping("/stores/{storeId}/reviews/{reviewId}/likes")
    public ResponseEntity<ApiResponseDto> unlikeReview(
            @PathVariable("reviewId") Long reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewService.unlikeReview(reviewId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "좋아요가 취소되었습니다."));
    }

}