package com.ocho.what2do.review.service;

import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.file.FileUploader;
import com.ocho.what2do.common.file.S3FileDto;
import com.ocho.what2do.common.message.CustomErrorCode;
import com.ocho.what2do.review.dto.ReviewLikeResponseDto;
import com.ocho.what2do.review.dto.ReviewRequestDto;
import com.ocho.what2do.review.dto.ReviewResponseDto;
import com.ocho.what2do.review.entity.Review;
import com.ocho.what2do.review.entity.ReviewLike;
import com.ocho.what2do.review.repository.ReviewLikeRepository;
import com.ocho.what2do.review.repository.ReviewRepository;
import com.ocho.what2do.store.dto.StoreResponseDto;
import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.store.repository.StoreRepository;
import com.ocho.what2do.user.entity.User;
import com.ocho.what2do.user.entity.UserRoleEnum;
import com.ocho.what2do.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final FileUploader fileUploader;
    private final StoreRepository storeRepository;


    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getAllReviews(Long storeId, int page, int size, String sortBy, boolean isAsc) {
        Store store = findStore(storeId);
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        List<ReviewResponseDto> stoerList = reviewRepository.findAllByStore(store, pageable).stream().map(ReviewResponseDto::new).toList();

        return stoerList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getUserReviews(User user, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return reviewRepository.findByUser(user, pageable)
                .stream()
                .map(ReviewResponseDto::new)
                .toList();
    }

    @Override
    @Transactional
    public ReviewResponseDto createReview(Long storeId, ReviewRequestDto requestDto, User user, List<MultipartFile> files)
            throws IOException {
        user = findUser(user.getId());
        Store store = findStore(storeId);

        //파일 등록
        List<S3FileDto> fileDtoList = null;
        if (!(files == null || (files.size() == 1 && files.get(0).isEmpty()))) {
            fileDtoList = fileUploader.uploadFiles(files, "files");
            requestDto.setAttachment(fileDtoList);
        }

        Review review = Review.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .user(user)
                .attachment(fileDtoList)
                .store(store)
                .rate(requestDto.getRate())
                .build();

        reviewRepository.save(review);

        return new ReviewResponseDto(review);
    }

    @Override
    @Transactional
    public ReviewResponseDto updateReview(Long storeId, Long reviewId, ReviewRequestDto requestDto, User user, List<MultipartFile> files)
            throws IOException {

        // 파일첨부
        List<S3FileDto> fileDtoList = null;
        findStore(storeId);
        Review review = findReview(reviewId);


        // 파일정보 불러오기
        List<S3FileDto> attachment = review.getAttachment();

        // 기존 파일 삭제
        if (attachment != null && !attachment.isEmpty()) {
            for (S3FileDto s3FileDto : attachment) {
                fileUploader.deleteFile(s3FileDto.getUploadFilePath(),
                        s3FileDto.getUploadFileName());
            }
        }

        // 파일 등록
        if (!(files == null || (files.size() == 1 && files.get(0).isEmpty()))) {
            fileDtoList = fileUploader.uploadFiles(files, "files");
            requestDto.setAttachment(fileDtoList);
        }


        confirmUser(review, user);

        review.updateReview(requestDto);

        return new ReviewResponseDto(review);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId, User user) {
        Review review = findReview(reviewId);
        confirmUser(review, user);

        // 파일정보 불러오기
        List<S3FileDto> attachment = review.getAttachment();

        // 기존 파일 삭제
        if (attachment != null && !attachment.isEmpty()) {
            for (S3FileDto s3FileDto : attachment) {
                fileUploader.deleteFile(s3FileDto.getUploadFilePath(),
                        s3FileDto.getUploadFileName());
            }
        }

        reviewRepository.deleteById(reviewId);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponseDto getReview(Long reviewId, User user) {
        Review review = findReview(reviewId);

        return new ReviewResponseDto(review, user);
    }

    @Override
    @Transactional
    public ReviewLikeResponseDto likeReview(Long reviewId, User user) {
        Review review = findReview(reviewId);

        // 좋아요 처리 로직 추가
        Optional<ReviewLike> reviewLike = reviewLikeRepository.findByUserAndReview(user, review);
        if (reviewLike.isPresent()) {
            throw new CustomException(CustomErrorCode.REVIEW_ALREADY_LIKED, null);
        }

        // 새로운 좋아요 엔티티 생성 및 추가
        ReviewLike newLike = new ReviewLike(user, review);
        review.addLike(newLike);
        ReviewLike savedLike = reviewLikeRepository.save(newLike);

        return new ReviewLikeResponseDto(savedLike);
    }

    @Override
    @Transactional
    public void unlikeReview(Long reviewId, User user) {
        Review review = findReview(reviewId);

        // 좋아요 취소 처리 로직 추가
        ReviewLike reviewLike = reviewLikeRepository.findByUserAndReview(user, review)
                .orElseThrow(() -> new CustomException(CustomErrorCode.REVIEW_NOT_LIKED, null));

        // 해당 좋아요 엔티티 제거
        reviewLikeRepository.delete(reviewLike);

    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND, null));
    }

    private Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.REVIEW_NOT_FOUND, null));
    }

    public Store findStore(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new CustomException(CustomErrorCode.STORE_NOT_FOUND));
    }

    private void confirmUser(Review review, User user) {
        if (!review.getUser().getId().equals(user.getId())
                && !user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED_REQUEST, null);
        }
    }
}

