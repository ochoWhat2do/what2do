package com.ocho.what2do.store.controller;

import com.ocho.what2do.common.dto.ApiResponseDto;
import com.ocho.what2do.common.security.UserDetailsImpl;
import com.ocho.what2do.store.dto.StoreListResponseDto;
import com.ocho.what2do.store.dto.StoreRequestDto;
import com.ocho.what2do.store.dto.StoreResponseDto;
import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.RejectedExecutionException;

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
    public ResponseEntity<StoreResponseDto> getStoreById(@PathVariable Long storeId) {
        StoreResponseDto result = storeService.getStoreById(storeId);
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

}
