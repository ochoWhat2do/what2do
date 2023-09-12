package com.ocho.what2do.admin.controller;


import com.ocho.what2do.admin.dto.AdminStoreListResponseDto;
import com.ocho.what2do.admin.dto.AdminStoreRequestDto;
import com.ocho.what2do.admin.dto.AdminStoreResponseDto;
import com.ocho.what2do.admin.dto.AdminStoreViewResponseDto;
import com.ocho.what2do.admin.service.AdminStoreService;
import com.ocho.what2do.common.dto.ApiResponseDto;
import com.ocho.what2do.common.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminStoreController {

  private final AdminStoreService storeService;

  @Operation(summary = "관리자 가게 등록", description = "가게를 등록합니다.")
  @PostMapping("/stores") //가게 등록
  public ResponseEntity<AdminStoreResponseDto> createStore(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart AdminStoreRequestDto requestDto, @RequestPart(required = false) List<MultipartFile> files) {
    return new ResponseEntity<>(storeService.createStore(requestDto, userDetails.getUser(),files), HttpStatus.OK);
  }

  @Operation(summary = "관리자 가게 전체 조회", description = "가게를 조회합니다.")
  @GetMapping("/stores") //가게 전체 조회
  public ResponseEntity<AdminStoreListResponseDto> getStores() {
    AdminStoreListResponseDto result = storeService.getStores();

    return ResponseEntity.ok().body(result);
  }

  @Operation(summary = "관리자 가게 전체 조회", description = "가게를 조회합니다.")
  @GetMapping("/stores/{storeId}") //가게 단건 조회
  public ResponseEntity<AdminStoreViewResponseDto> getStoreById(@PathVariable Long storeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return new ResponseEntity<>(storeService.getStoreById(storeId, userDetails.getUser()), HttpStatus.OK);
  }

  @Operation(summary = "관리자 가게 수정", description = "가게를 수정합니다.")
  @PutMapping("/stores/{storeId}") //가게 수정
  public ResponseEntity<ApiResponseDto> updateStore(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long storeId, @RequestPart AdminStoreRequestDto requestDto,  @RequestPart(required = false) List<MultipartFile> files) {
    AdminStoreResponseDto result;
    result = storeService.updateStore(storeId, requestDto, userDetails.getUser(), files);
    return ResponseEntity.ok().body(result);
  }

  @Operation(summary = "관리자 가게 삭제", description = "가게를 삭제합니다.")
  @DeleteMapping("/stores/{storeId}") //가게 삭제
  public ResponseEntity<ApiResponseDto> deleteStore(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long storeId) {
    storeService.deleteStore(storeId, userDetails.getUser());
    return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "가게 삭제 성공"));
  }
}
