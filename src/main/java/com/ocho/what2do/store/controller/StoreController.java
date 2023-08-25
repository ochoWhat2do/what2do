package com.ocho.what2do.store.controller;

import com.ocho.what2do.common.dto.ApiResponseDto;
import com.ocho.what2do.common.security.UserDetailsImpl;
import com.ocho.what2do.store.dto.StoreListResponseDto;
import com.ocho.what2do.store.dto.StoreRequestDto;
import com.ocho.what2do.store.dto.StoreResponseDto;
import com.ocho.what2do.store.dto.StoreViewResponseDto;
import com.ocho.what2do.store.entity.Store;
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

    @PostMapping("/stores") //가게 등록
    public ResponseEntity<StoreResponseDto> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody StoreRequestDto requestDto) {
        StoreResponseDto result = storeService.createStore(requestDto, userDetails.getUser());

        return ResponseEntity.status(201).body(result);
    }

    @GetMapping("/stores") //가게 전체 조회
    public ResponseEntity<StoreListResponseDto> getStores() {
        StoreListResponseDto result = storeService.getStores();

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/stores/{storeId}") //가게 단건 조회
    public ResponseEntity<StoreViewResponseDto> getStoreById(@PathVariable Long storeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StoreViewResponseDto result = storeService.getStoreById(storeId, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/stores/{storeId}") //가게 수정
    public ResponseEntity<ApiResponseDto> updateStore(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long storeId, @RequestBody StoreRequestDto requestDto) {
        StoreResponseDto result;
            Store store = storeService.findStore(storeId);
            result = storeService.updateStore(store, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/stores/{storeId}") //가게 삭제
    public ResponseEntity<ApiResponseDto> deleteStore(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long storeId) {
            Store store = storeService.findStore(storeId);
            storeService.deleteStore(store, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "가게 삭제 성공"));
    }

    @Operation(summary = "가게 찜하기", description = "가게를 찜합니다.")
    @PostMapping("/stores/{storeId}/storefavorites") // 가게 찜하기
    public ResponseEntity<StoreFavoriteResponseDto> addStoreFavorite(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long storeId){
        StoreFavoriteResponseDto result = storeService.addStoreFavorite(storeId,userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "가게 찜 목록 조회", description = "가게를 찜한 목록을 조회합니다.")
    @GetMapping("stores/{storeId}/storefavorites") // 가게 찜 목록 조회
    public ResponseEntity<StoreFavoriteListResponseDto> getStoreFavorite(@AuthenticationPrincipal UserDetailsImpl userDetails){
        StoreFavoriteListResponseDto result = storeService.getStoreFavorite(userDetails.getUser());

        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "가게 찜하기 취소", description = "가게를 찜하기를 취소합니다.")
    @DeleteMapping("stores/{storeId}/storefavorites") //가게 찜 취소
    public ResponseEntity<ApiResponseDto> deleteStoreFavorite(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long storeId){
        storeService.deleteStoreFavorite(storeId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "가게 찜하기 취소 성공"));
    }

}