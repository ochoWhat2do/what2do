package com.ocho.what2do.admin.service;

import com.ocho.what2do.admin.dto.UserLockRequestDto;
import com.ocho.what2do.common.dto.ApiResponseDto;
import com.ocho.what2do.user.entity.UserRoleEnum;
import org.springframework.http.ResponseEntity;

public interface AdminUserService {

  /**
   * 관리자 여부 확인
   * @param token 관리자 토큰
   * @return 성공 여부
   */
  ApiResponseDto checkToken(String token);

  /**
   * 권한 변경
   * @param email 이메일
   * @param roleName 권한
   * @return 성공 여부
   */
  ApiResponseDto changeRole(String email, String roleName);

  /**
   * 회원 정지
   * @param userLockRequestDto 회원 정보
   * @return 성공 여부
   */
  ApiResponseDto lockUser(UserLockRequestDto userLockRequestDto);
}
