package com.ocho.what2do.common.auth.service;

import com.ocho.what2do.common.auth.dto.TokenDto;

public interface UserAuthService {

  public TokenDto reissueToken(String refreshToken);
}
