package com.ocho.what2do.admin.service;

import com.ocho.what2do.admin.dto.AdminApiStoreListResponseDto;
import com.ocho.what2do.admin.dto.AdminApiStoreRequestDto;
import com.ocho.what2do.admin.dto.AdminApiStoreResponseDto;
import com.ocho.what2do.admin.dto.AdminApiStoreViewResponseDto;
import com.ocho.what2do.admin.dto.AdminStoreListResponseDto;
import com.ocho.what2do.admin.dto.AdminStoreRequestDto;
import com.ocho.what2do.admin.dto.AdminStoreResponseDto;
import com.ocho.what2do.admin.dto.AdminStoreViewResponseDto;
import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.file.FileUploader;
import com.ocho.what2do.common.file.S3FileDto;
import com.ocho.what2do.common.message.CustomErrorCode;
import com.ocho.what2do.review.dto.ReviewListResponseDto;
import com.ocho.what2do.review.dto.ReviewResponseDto;
import com.ocho.what2do.review.entity.Review;
import com.ocho.what2do.store.entity.ApiStore;
import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.store.repository.ApiStoreRepository;
import com.ocho.what2do.store.repository.StoreRepository;
import com.ocho.what2do.user.entity.User;
import com.ocho.what2do.user.entity.UserRoleEnum;
import com.ocho.what2do.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminStoreServiceImpl implements AdminStoreService {

    private final StoreRepository storeRepository;
    private final ApiStoreRepository apiStoreRepository;
    private final FileUploader fileUploader;
    private final UserRepository userRepository;

    @Value("${spring.profiles.active}")
    private String runType;


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
    public AdminApiStoreListResponseDto getStores(String keyword, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ApiStore> pageApiStores = apiStoreRepository.findAll(pageable);
        Long totalCount = ((PageImpl) pageApiStores).getTotalElements();
        int pageCount = ((PageImpl) pageApiStores).getTotalPages();
        List<AdminApiStoreResponseDto> storeList = pageApiStores.stream().map(AdminApiStoreResponseDto::new).toList();
        AdminApiStoreListResponseDto adminApiStoreListResponseDto = new AdminApiStoreListResponseDto(totalCount, pageCount, storeList);
        return adminApiStoreListResponseDto;
    }

    @Override
    @Transactional
    public AdminStoreViewResponseDto getStoreById(Long storeId, User user) {
        Store store = findStore(storeId);
        return new AdminStoreViewResponseDto(store, user);
    }

    @Override
    public AdminApiStoreViewResponseDto getApiStoreByStoreKey(String storeKey, User user) {
        Optional<ApiStore> opApiStore = apiStoreRepository.findByStoreKey(storeKey);
        if(opApiStore.isPresent()) {
            ApiStore apiStore = opApiStore.get();
            return new AdminApiStoreViewResponseDto(apiStore);
        }
        return null;
    }

    @Override
    @Transactional
    public AdminApiStoreResponseDto updateApiStore(String storeKey, AdminApiStoreRequestDto requestDto,
        User user, List<MultipartFile> files) {
        ApiStore apiStore= findApiStore(storeKey);
        List<S3FileDto> fileDtoList = null;
        // 폴더 저장경로 설정
        String s3SavedFolder = "";
        if (runType.equals("local")) {
            s3SavedFolder = "store_local";
        } else {
            s3SavedFolder = "store";
        }

        // 파일정보 불러오기
        List<S3FileDto> images = apiStore.getImages();

        // 기존 파일 삭제
        if (images != null && !images.isEmpty()) {
            for (S3FileDto s3FileDto : images) {
                fileUploader.deleteFile(s3FileDto.getUploadFilePath(),
                    s3FileDto.getUploadFileName());
            }
        }

        // 파일 등록
        if (!(files == null || (files.size() == 1 && files.get(0).isEmpty()))) {
            fileDtoList = fileUploader.uploadFiles(files, s3SavedFolder);
        }
        apiStore.update(fileDtoList);
        Optional<Store> opStore = storeRepository.getStoreByStoreKey(storeKey);
        if(!opStore.isEmpty()) {
            Store selectedStore = opStore.get();
            if (fileDtoList != null) {
                selectedStore.updateImages(fileDtoList);
            }
        }

        return new AdminApiStoreResponseDto(apiStore);
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
    public ApiStore findApiStore(String storeKey) {
        return apiStoreRepository.findByStoreKey(storeKey)
            .orElseThrow(() -> new CustomException(CustomErrorCode.STORE_NOT_FOUND, null));
    }

    @Override
    public Store findStore(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.STORE_NOT_FOUND, null));
    }

    @Override
    public User findUser(User user) {
        return userRepository.findByEmail(user.getEmail()).filter(v -> v.getRole().equals(
                UserRoleEnum.ADMIN.getAuthority()))
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_ADMIN_AUTH));
    }
}
