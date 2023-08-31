package com.ocho.what2do.admin.controller;

import com.ocho.what2do.admin.dto.TokenRequestDto;
import com.ocho.what2do.admin.dto.UserLockRequestDto;
import com.ocho.what2do.admin.dto.UserRoleRequestDto;
import com.ocho.what2do.admin.service.AdminUserServiceImpl;
import com.ocho.what2do.common.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminUserController {

  private final AdminUserServiceImpl adminUserService;
  @Operation(summary = "관리자 토큰 확인", description = "관리자 토큰 확인합니다.")
  @PostMapping("/checks")
  public ResponseEntity<ApiResponseDto> checkToken(@RequestBody TokenRequestDto requestDto) {
    return ResponseEntity.ok(adminUserService.checkToken(requestDto.getToken()));
  }

  @Operation(summary = "관리자의 사용자 정지", description = "관리자가 사용자의 로그인 가능 여부를 처리합니다.")
  @PatchMapping("/users/lock")
  public ResponseEntity<ApiResponseDto> lockUser(@RequestBody UserLockRequestDto requestDto) {
    return ResponseEntity.ok(adminUserService.lockUser(requestDto));
  }

  @Operation(summary = "관리자의 사용자 권한 수정", description = "관리자가 사용자의 권한을 처리합니다.")
  @PutMapping("/users/role")
  public ResponseEntity<ApiResponseDto> changeRole(@RequestBody UserRoleRequestDto requestDto) {
    return ResponseEntity.ok(
        adminUserService.changeRole(requestDto.getEmail(), requestDto.getRole()));
  }

}
