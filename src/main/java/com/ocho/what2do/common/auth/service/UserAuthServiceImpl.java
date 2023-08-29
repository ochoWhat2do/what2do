package com.ocho.what2do.common.auth.service;

import com.ocho.what2do.common.auth.dto.TokenDto;
import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.jwt.JwtUtil;
import com.ocho.what2do.common.message.CustomErrorCode;
import com.ocho.what2do.common.security.UserDetailsImpl;
import com.ocho.what2do.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService{

  @Override
  public TokenDto reissueToken(String refreshToken) {

    // 토큰 재발행
    TokenDto tokenDto = new TokenDto(
        "",
        ""
    );

    return tokenDto;
  }
}
