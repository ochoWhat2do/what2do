package com.ocho.what2do.comment.service;

import com.ocho.what2do.comment.dto.CommentCreateRequestDto;
import com.ocho.what2do.comment.dto.CommentEditRequestDto;
import com.ocho.what2do.comment.dto.CommentResponseDto;
import com.ocho.what2do.comment.entity.Comment;
import com.ocho.what2do.comment.entity.CommentLike;
import com.ocho.what2do.comment.repository.CommentLikeRepository;
import com.ocho.what2do.comment.repository.CommentRepository;
import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.message.CustomErrorCode;
import com.ocho.what2do.review.entity.Review;
import com.ocho.what2do.review.repository.ReviewRepository;
import com.ocho.what2do.user.entity.User;
import com.ocho.what2do.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentList(Long reviewId, int page, int size, String sortBy, boolean isAsc
    ) {
        Review review = findReview(reviewId);
        Direction direction = isAsc ? Direction.ASC : Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        List<CommentResponseDto> commentList = commentRepository.findAllByReview(review, pageable).stream()
                .map(CommentResponseDto::new).toList();
        return commentList;

    }

    @Override
    @Transactional
    public String createComment(CommentCreateRequestDto requestDto, User user) {

        Comment comment = new Comment(requestDto);
        Review review = findReview(requestDto.getReviewId());
        comment.setReview(review);
        comment.setUser(user);
        commentRepository.save(comment);

        return "코멘트 생성 완료";
    }

    @Override
    @Transactional
    public String editComment(Long commentId, CommentEditRequestDto requestDto, User user) {

        Comment comment = findComment(commentId);

        checkUser(comment.getUser(), user);
        comment.editComment(requestDto);

        return "코멘트 수정 완료";
    }

    @Override
    @Transactional
    public String deleteComment(Long commentId, User user) {

        Comment comment = findComment(commentId);

        checkUser(comment.getUser(), user);
        commentRepository.delete(comment);

        return "코멘트 삭제 완료";
    }

    @Override
    @Transactional
    public CommentResponseDto likeComment(Long commentId, User user) {
        Comment comment = findComment(commentId);

        // 좋아요 처리 로직 추가
        if (comment.getCommentLikes().stream().anyMatch(like -> like.getUser().equals(user))) {
            throw new CustomException(CustomErrorCode.COMMENT_ALREADY_LIKED, null);
        }

        // 새로운 좋아요 엔티티 생성 및 추가
        CommentLike newLike = new CommentLike(user, comment);
        comment.addCommentLike(newLike);
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Override
    @Transactional
    public CommentResponseDto unlikeComment(Long commentId, User user) {
        Comment comment = findComment(commentId);

        // 좋아요 취소 처리 로직 추가
        CommentLike likeToRemove = comment.getCommentLikes().stream()
                .filter(like -> like.getUser().equals(user))
                .findFirst()
                .orElseThrow(() -> new CustomException(CustomErrorCode.COMMENT_NOT_LIKED, null));

        // 해당 좋아요 엔티티 제거
        comment.removeCommentLike(likeToRemove);
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }


    private void checkUser(User commentUser, User loginUser) {
        if (!commentUser.getId().equals(loginUser.getId())
                && !loginUser.getRole().equals(UserRoleEnum.ADMIN))
            throw new CustomException(CustomErrorCode.UNAUTHORIZED_REQUEST, null);
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(()
                -> new CustomException(CustomErrorCode.COMMENT_NOT_FOUND, null));
    }

    private Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(()
                -> new CustomException(CustomErrorCode.REVIEW_NOT_FOUND, null));
    }

}


