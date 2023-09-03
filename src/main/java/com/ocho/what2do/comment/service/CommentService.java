package com.ocho.what2do.comment.service;

import com.ocho.what2do.comment.dto.CommentCreateRequestDto;
import com.ocho.what2do.comment.dto.CommentEditRequestDto;
import com.ocho.what2do.comment.dto.CommentLikeResponseDto;
import com.ocho.what2do.comment.dto.CommentResponseDto;
import com.ocho.what2do.user.entity.User;

import java.util.List;


public interface CommentService {

    /*
     * 댓글 목록 조회
     * @param reviewId 조회할 리뷰 ID
     * @return 댓글 목록
     */
    List<CommentResponseDto> getCommentList(Long reviewId, int page, int size, String sortBy, boolean isAsc);

    /*
     * 댓글 생성
     * @param requestDto 댓글 생성 정보
     * @param user 작성자 정보
     * @return 생성된 댓글 정보
     */
    String createComment(Long reviewId, CommentCreateRequestDto requestDto, User user);

    /*
     * 댓글 수정
     * @param commentId 수정할 댓글 ID
     * @param requestDto 댓글 수정 정보
     * @param user 작성자 정보
     * @return 수정된 댓글 정보
     */
    String editComment(Long commentId, CommentEditRequestDto requestDto, User user);

    /*
     * 댓글 삭제
     * @param commentId 삭제할 댓글 ID
     * @param user 작성자 정보
     */
    String deleteComment(Long commentId, User user);

    /*
     * 댓글 좋아요
     * @param commentId 좋아요할 댓글 ID
     * @param user 사용자 정보
     * @return 업데이트된 댓글 정보
     */
    CommentLikeResponseDto likeComment(Long commentId, User user);

    /*
     * 댓글 좋아요 취소
     * @param commentId 좋아요 취소할 댓글 ID
     * @param user 사용자 정보
     * @return 업데이트된 댓글 정보
     */
    void unlikeComment(Long commentId, User user);
}