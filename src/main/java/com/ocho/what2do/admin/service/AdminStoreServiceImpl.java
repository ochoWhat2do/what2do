package com.ocho.what2do.admin.service;

import com.ocho.what2do.admin.dto.AdminStoreListResponseDto;
import com.ocho.what2do.admin.dto.AdminStoreRequestDto;
import com.ocho.what2do.admin.dto.AdminStoreResponseDto;
import com.ocho.what2do.admin.dto.AdminStoreViewResponseDto;
import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.file.FileUploader;
import com.ocho.what2do.common.file.S3FileDto;
import com.ocho.what2do.common.message.CustomErrorCode;
import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.store.repository.StoreRepository;
import com.ocho.what2do.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminStoreServiceImpl implements AdminStoreService {

    private final StoreRepository storeRepository;
    private final FileUploader fileUploader;

    @Override
    @Transactional
    public AdminStoreResponseDto createStore(AdminStoreRequestDto requestDto, User user, List<MultipartFile> files) {
        //이미지 등록
        List<S3FileDto> fileDtoList = null;
        if (!(files == null || (files.size() == 1 && files.get(0).isEmpty()))) {
            fileDtoList = fileUploader.uploadFiles(files, "files");
            requestDto.setImages(fileDtoList);
        }

        Store store = Store.builder().title(requestDto.getTitle())
                .homePageLink(requestDto.getHomePageLink())
                .category(requestDto.getCategory())
                .address(requestDto.getAddress())
                .roadAddress(requestDto.getRoadAddress())
                .latitude(requestDto.getLatitude())
                .longitude(requestDto.getLongitude())
                .images(requestDto.getImages())
                .storeKey(requestDto.getStoreKey())
                .build();
        return new AdminStoreResponseDto(storeRepository.save(store));

    }

    @Override
    @Transactional
    public AdminStoreListResponseDto getStores() {
        List<AdminStoreResponseDto> storeList = storeRepository.findAll().stream()
                .map(AdminStoreResponseDto::new).collect(
                        Collectors.toList());
        return new AdminStoreListResponseDto(storeList);
    }

    @Override
    @Transactional
    public AdminStoreViewResponseDto getStoreById(Long storeId, User user) {
        Store store = findStore(storeId);
        return new AdminStoreViewResponseDto(store, user);
    }

    @Override
    @Transactional
    public AdminStoreResponseDto updateStore(Long storeId, AdminStoreRequestDto requestDto,
                                             User user, List<MultipartFile> files) {
        Store store = findStore(storeId);
        List<S3FileDto> fileDtoList = null;
        // 파일정보 불러오기
        List<S3FileDto> images = store.getImages();

        // 기존 파일 삭제
        if (images != null && !images.isEmpty()) {
            for (S3FileDto s3FileDto : images) {
                fileUploader.deleteFile(s3FileDto.getUploadFilePath(),
                        s3FileDto.getUploadFileName());
            }
        }

        // 파일 등록
        if (!(files == null || (files.size() == 1 && files.get(0).isEmpty()))) {
            fileDtoList = fileUploader.uploadFiles(files, "files");
            requestDto.setImages(fileDtoList);
        }
        store.updateAdmin(requestDto);
        return new AdminStoreResponseDto(store);
    }

    @Override
    @Transactional
    public void deleteStore(Long storeId, User user) {
        storeRepository.deleteById(storeId);
    }

    @Override
    public Store findStore(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.STORE_NOT_FOUND, null));
    }
}
