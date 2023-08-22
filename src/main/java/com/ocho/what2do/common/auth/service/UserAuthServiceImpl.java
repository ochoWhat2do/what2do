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

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;

  private final RedisTemplate<String, String> redisTemplate;

  @Override
  public TokenDto reissueToken(String refreshToken) {
    // Refresh Token 검증
    jwtUtil.validateToken(refreshToken);

    // Access Token 에서 User num을 가져옴
    Authentication authentication = jwtUtil.getAuthentication(refreshToken);
    UserRoleEnum role = ((UserDetailsImpl) authentication.getPrincipal()).getUser().getRole();

    // Redis에서 저장된 Refresh Token 값을 가져옴
    String redisRefreshToken = redisTemplate.opsForValue().get("RT:"+((UserDetailsImpl) authentication.getPrincipal()).getUser().getEmail());
    if(!redisRefreshToken.equals(refreshToken)) {
      throw new CustomException(CustomErrorCode.NOT_EXIST_REFRESH_JWT, null);
    }
    // 토큰 재발행
    TokenDto tokenDto = new TokenDto(
        jwtUtil.createAccessToken(authentication.getName(), role),
        jwtUtil.createRefreshToken(authentication)
    );

    return tokenDto;
  }
}
