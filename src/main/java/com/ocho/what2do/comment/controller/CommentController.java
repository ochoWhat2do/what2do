package com.ocho.what2do.comment.controller;

import com.ocho.what2do.comment.dto.CommentCreateRequestDto;
import com.ocho.what2do.comment.dto.CommentEditRequestDto;
import com.ocho.what2do.comment.dto.CommentLikeResponseDto;
import com.ocho.what2do.comment.dto.CommentListResponseDto;
import com.ocho.what2do.comment.service.CommentService;
import com.ocho.what2do.common.dto.ApiResponseDto;
import com.ocho.what2do.common.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Comment API", description = "댓글 기능과 관련된 API 정보를 담고 있습니다.")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 목록 조회", description = "댓글 목록을 조회합니다.")
    @GetMapping("/stores/{storeId}/reviews/{reviewId}/comments")
    @ResponseBody
    public ResponseEntity<CommentListResponseDto> commentList(
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentListResponseDto responseDto = commentService.getCommentList(reviewId, page - 1, size, sortBy, isAsc, userDetails.getUser());
        return ResponseEntity.ok().body(responseDto);
    }

    @Operation(summary = "댓글 생성", description = "댓글을 생성합니다.")
    @PostMapping("/stores/{storeId}/reviews/{reviewId}/comments")
    public ResponseEntity<ApiResponseDto> createComment(
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @RequestBody CommentCreateRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String result = commentService.createComment(reviewId, requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto(HttpStatus.OK.value(), result));
    }

    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @PutMapping("/stores/{storeId}/reviews/{reviewId}/comments/{commentId}")
    public ResponseEntity<ApiResponseDto> editComment(
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @PathVariable Long commentId,
            @RequestBody CommentEditRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String result = commentService.editComment(commentId, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), result));
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @DeleteMapping("/stores/{storeId}/reviews/{reviewId}/comments/{commentId}")
    public ResponseEntity<ApiResponseDto> deleteComment(
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String result = commentService.deleteComment(commentId, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto(HttpStatus.OK.value(), result));
    }

    @Operation(summary = "댓글 좋아요", description = "댓글에 좋아요를 표시합니다.")
    @PostMapping("/stores/{storeId}/reviews/{reviewId}/comments/{commentId}/likes")
    public ResponseEntity<CommentLikeResponseDto> likeComment(
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentLikeResponseDto responseDto = commentService.likeComment(commentId, userDetails.getUser());
        return ResponseEntity.ok().body(responseDto);
    }

    @Operation(summary = "댓글 좋아요 취소", description = "댓글의 좋아요를 취소합니다.")
    @DeleteMapping("/stores/{storeId}/reviews/{reviewId}/comments/{commentId}/likes")
    public ResponseEntity<ApiResponseDto> unlikeComment(
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @PathVariable("commentId") Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.unlikeComment(commentId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "좋아요가 취소되었습니다."));
    }
}
