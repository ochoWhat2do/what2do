package com.ocho.what2do.store.controller;

import com.ocho.what2do.common.dto.ApiResponseDto;
import com.ocho.what2do.common.security.UserDetailsImpl;
import com.ocho.what2do.store.dto.StoreListResponseDto;
import com.ocho.what2do.store.dto.StoreResponseDto;
import com.ocho.what2do.store.service.StoreService;
import com.ocho.what2do.storefavorite.dto.StoreFavoriteListResponseDto;
import com.ocho.what2do.storefavorite.dto.StoreFavoriteResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @Operation(summary = "가게 전체 조회", description = "DB 내의 모든 가게를 조회합니다.")
    @GetMapping("/stores")
    public ResponseEntity<StoreListResponseDto> getStores() {
        StoreListResponseDto result = storeService.getStores();
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "가게 상세 조회", description = "DB 내의 가게의 상세 정보를 조회합니다.")
    @GetMapping("/stores/detail")
    public ResponseEntity<StoreResponseDto> getStoreKey(@RequestParam String storeKey) {
        StoreResponseDto result = storeService.getStore(storeKey);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "가게 찜 목록 조회", description = "가게를 찜한 목록을 조회합니다.")
    @GetMapping("stores/storefavorites") // 가게 찜 목록 조회
    public ResponseEntity<StoreFavoriteListResponseDto> getStoreFavorite(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(storeService.getStoreFavorite(userDetails.getUser()), HttpStatus.OK);

    }

    @Operation(summary = "가게 찜하기", description = "가게를 찜합니다.")
    @PostMapping("/stores/{storeId}/storefavorites") // 가게 찜하기
    public ResponseEntity<StoreFavoriteResponseDto> addStoreFavorite(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                     @PathVariable Long storeId) {
        StoreFavoriteResponseDto result = storeService.addStoreFavorite(storeId, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "가게 찜하기 취소", description = "가게를 찜하기를 취소합니다.")
    @DeleteMapping("stores/{storeId}/storefavorites") //가게 찜 취소
    public ResponseEntity<ApiResponseDto> deleteStoreFavorite(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @PathVariable Long storeId) {
        storeService.deleteStoreFavorite(storeId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "가게 찜하기 취소 성공"));
    }

}
