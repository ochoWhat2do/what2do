package com.ocho.what2do.common.auth.service;

import com.ocho.what2do.common.auth.dto.TokenDto;
import lombok.RequiredArgsConstructor;
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
