package com.ocho.what2do.store.service;

import com.ocho.what2do.common.ConstVal;
import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.file.FileUploader;
import com.ocho.what2do.common.file.S3FileDto;
import com.ocho.what2do.common.message.CustomErrorCode;
import com.ocho.what2do.store.dto.StoreCategoryListResponseDto;
import com.ocho.what2do.store.dto.StoreListResponseDto;
import com.ocho.what2do.store.dto.StoreResponseDto;
import com.ocho.what2do.store.entity.ApiStore;
import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.store.repository.ApiStoreRepository;
import com.ocho.what2do.store.repository.StoreRepository;
import com.ocho.what2do.storefavorite.dto.StoreFavoriteListResponseDto;
import com.ocho.what2do.storefavorite.dto.StoreFavoriteResponseDto;
import com.ocho.what2do.storefavorite.entity.StoreFavorite;
import com.ocho.what2do.storefavorite.repository.StoreFavoriteRepository;
import com.ocho.what2do.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

  @Value("${spring.profiles.active}")
  private String runType;

  private final StoreRepository storeRepository;
  private final ApiStoreRepository apiStoreRepository;
  private final StoreFavoriteRepository storeFavoriteRepository;
  private final FileUploader fileUploader;
  private final int pageSize = ConstVal.API_STORE_PAGE_PER_COUNT;    // 한 페이지에 출력할 데이터 갯수(API 받아온 정보라면 고정)

  @Override
  @Transactional(readOnly = true)
  public StoreListResponseDto getStores(int page) {
    PageRequest pageRequest = pageable(page);
    List<StoreResponseDto> storeList = apiStoreRepository.findAll(pageRequest).stream()
        .map(StoreResponseDto::new).collect(Collectors.toList());
    Integer totalCnt = apiStoreRepository.findAll().size();
    Integer pageCnt = pageCnt(totalCnt);
    if (pageCnt < page) {
      throw new CustomException(CustomErrorCode.DATA_NOT_FOUND);
    }

    return new StoreListResponseDto(totalCnt, pageCnt, storeList);
  }

  @Override
  @Transactional
  public StoreResponseDto getStore(String storeKey, User user) {
    ApiStore findStore = findByStoreKey(storeKey);
    Store store = Store.builder().storeKey(findStore.getStoreKey())
        .title(findStore.getTitle())
        .homePageLink(findStore.getHomePageLink())
        .category(findStore.getCategory())
        .address(findStore.getAddress())
        .roadAddress(findStore.getRoadAddress())
        .latitude(findStore.getLatitude())
        .longitude(findStore.getLongitude())
        .images(findStore.getImages())
        .viewCount(0)
        .build();
    Optional<Store> savedStore = null;
    // 프론트에서는 db에 저장된 정보를 활용할 필요가 있다. storeKey 로 조회할 때는 ApiStore 테이블에서 조회, storeId가 필요할때는 Store 테이블에서 조회
    if (!storeRepository.existsStoreByStoreKey(store.getStoreKey())) {
      savedStore = Optional.of(storeRepository.save(store));
    } else {
      savedStore = storeRepository.getStoreByStoreKey(storeKey);
    }
    if (savedStore.isPresent()) {
      return new StoreResponseDto(savedStore.get(), user);
    } else {
      // API 정보에 이미지가 있고  STORE 테이블에 이미지가 없을경우 업데이트
      if (findStore.getImages() != null) {
        Store savedStoreEntity = savedStore.get();
        if (savedStoreEntity.getImages() == null) {
          savedStoreEntity.updateImages(findStore.getImages());
        }
      }
      return new StoreResponseDto(findStore);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public StoreCategoryListResponseDto getStoreCategory(String category, int page) {
    PageRequest pageRequest = pageable(page);
    List<StoreResponseDto> storeCategory = apiStoreRepository.findByCategoryContains(category,
        pageRequest).stream().map(StoreResponseDto::new).toList();
    Integer totalCnt = apiStoreRepository.findAllByCategoryContains(category).stream().toList()
        .size();
    Integer pageCnt = pageCnt(totalCnt);
    if (pageCnt < page || totalCnt == 0 || storeCategory.isEmpty()) {
      throw new CustomException(CustomErrorCode.DATA_NOT_FOUND);
    }
    return new StoreCategoryListResponseDto(totalCnt, pageCnt, storeCategory);
  }

  @Override
  @Transactional(readOnly = true)
  public StoreFavoriteListResponseDto getStoreFavorite(User user) {
    List<StoreFavoriteResponseDto> storeUserList = storeFavoriteRepository.findAllByUserId(
        user.getId()).stream().map(StoreFavoriteResponseDto::new).collect(Collectors.toList());
    return new StoreFavoriteListResponseDto(storeUserList);
  }

  @Override
  @Transactional
  public StoreFavoriteResponseDto addStoreFavorite(Long storeId, User user) {
    Store store = findStore(storeId);
    if (storeFavoriteRepository.existsByUserAndStore(user, store)) {
      throw new CustomException(CustomErrorCode.STORE_FAVORITE_ALREADY_EXIST);
    }
    StoreFavorite storeUser = new StoreFavorite(store, user);
    storeFavoriteRepository.save(storeUser);

    return new StoreFavoriteResponseDto(storeUser, user);
  }

  @Override
  @Transactional
  public void deleteStoreFavorite(Long storeId, User user) {
    Store store = findStore(storeId);
    Optional<StoreFavorite> storeUserOptional = storeFavoriteRepository.findByUserAndStore(user,
        store);
    if (storeUserOptional.isPresent()) {
      storeFavoriteRepository.delete(storeUserOptional.get());
    } else {
      throw new CustomException(CustomErrorCode.STORE_FAVORITE_NOT_FOUND, null);
    }
  }

  @Override
  public StoreResponseDto getStoresByAddress(String address, User user) {

    // 주소로 가게를 조회하는 로직을 구현
    List<Store> stores = storeRepository.findByAddress(address);

    if (stores.isEmpty()) {
      throw new CustomException(CustomErrorCode.STORE_NOT_FOUND);
    }

    Store findStore = stores.get(0);
    StoreResponseDto responseDto = new StoreResponseDto(findStore, user);

    return responseDto;
  }

  @Transactional(readOnly = true)
  @Override
  public List<StoreResponseDto> findStoresListReview(int page, int size, String sortBy,
      boolean isAsc) {
    Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
    Sort sort = Sort.by(direction, sortBy);
    Pageable pageable = PageRequest.of(page, size, sort);

    return storeRepository.findStoresListReview(pageable);
  }

  @Override
  public Store findStore(Long storeId) {
    return storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomException(CustomErrorCode.STORE_NOT_FOUND));
  }

  @Override
  public ApiStore findByStoreKey(String storeKey) {
    return apiStoreRepository.findByStoreKey(storeKey)
        .orElseThrow(() -> new CustomException(CustomErrorCode.STORE_NOT_FOUND));
  }

  // 스케줄러에서 실행되는 메소드 입니다.
  @Override
  public ApiStore getApiStoreNotCheck(String storeKey) {
    Optional<ApiStore> optionalApiStore = apiStoreRepository.findByStoreKey(storeKey);
    if (optionalApiStore.isPresent()) {
      return optionalApiStore.get();
    } else {
      return null;
    }
    // 가게없으면 실행을 안하면 된다..
    /*return apiStoreRepository.findByStoreKey(storeKey)
        .orElseThrow(() -> new CustomException(CustomErrorCode.STORE_NOT_FOUND));*/
  }

  @Override
  public PageRequest pageable(int page) {
    /*
     * pageRequest.of(page, size, sort)
     * page : page -1 -> PageConfig 에서 page 시작 값을 1로 잡았지만 내부적으로는 0부터 시작으로 -1 을 진행
     * size : 한 page 의 출력 갯수
     * Sort.by("") : 정렬 기준
     * ascending : 오름차순 (default)
     * descending : 내림차순
     */
    return PageRequest.of(page - 1, pageSize, Sort.by("id"));
  }

  @Override
  public int pageCnt(int totalCnt) {
    if (Math.floorMod(totalCnt, pageSize) == 0) {  // 전체 갯수를 출력할 데이터의 갯수로 나눴을 때 나머지가 0이면
      return Math.floorDiv(totalCnt,
          pageSize);   // 페이지는 몫을 반환함 ex) 10개의 데이터를 10개로 한 페이지에 표출하면 1페이지가 마지막 페이지
    }
    return Math.floorDiv(totalCnt, pageSize)
        + 1; // 나머지가 0이 아닌 경우는 몫 + 1 ex) 11개의 데이터를 한 페이지에 10개로 표출하면 2페이지가 마지막 페이지
  }

  // Transaction 외부에 설정되어있음
  @Override
  public int updateApiStore(List<String> storeKeyList, List<MultipartFile> files) {

    List<S3FileDto> fileDtoList = null;
    List<MultipartFile> apiStoreFileList = null;
    int result = 0;
    int fileSavedResult = 0;
    String s3SavedFolder = "";
    if (runType.equals("local")) {
      s3SavedFolder = "store_local";
    } else {
      s3SavedFolder = "store";
    }

    if (files.size() > 0) {
      for (int i = 0; i < files.size(); i++) {
        // 가게마다 FileList 한개씩
        fileDtoList = new ArrayList<>();
        apiStoreFileList = new ArrayList<>();

        // 정규 표현식 패턴 정의
        String storeKey = storeKeyList.get(i);
        ApiStore apiStore = getApiStoreNotCheck(storeKey);
        if (apiStore != null) {
          // 파일정보 불러오기
          List<S3FileDto> images = apiStore.getImages();

          // 부하 때문에 파일이 없을 경우에만 업데이트 하기로 TODO: 파일업데이트
          if (images == null) {
            // 파일 등록 (현재는 대표 이미지 1개만 등록)
            if (files.get(i) != null) {
              apiStoreFileList.add(files.get(i));
              fileDtoList = fileUploader.uploadFiles(apiStoreFileList, s3SavedFolder);
              fileSavedResult++;
            }

            if (fileDtoList.size() > 0) {
              apiStore.update(fileDtoList);
              result++;
            }

          }
        } // if (apiStore != null)

      }
    }

    if (result > 0 && result == fileSavedResult) {
      result = 1;
    } else if (result == 0) {
      result = 0;
    } else {
      result = -1;
    }

    return result;
  }

  // 기존 파일 삭제
  //                if (images != null && images.size() > 0) {
  //                    for (int j = 0; j < images.size(); j++) {
  //                        fileUploader.deleteFile(images.get(j).getUploadFilePath(),
  //                            images.get(j).getUploadFileName());
  //                    }
  //                }
}