package com.ocho.what2do.common.auth.service;

import com.ocho.what2do.user.dto.SocialUserResponseDto;
import com.ocho.what2do.user.dto.UserResponseDto;

public interface OauthService {

  /**
   * 카카오 로그인
   * @param code 코드
   * @return UserResponseDto 사용자 객체
   */
  SocialUserResponseDto kakaoLogin(String code);
}
