package com.ocho.what2do.common.auth.controller;

import com.ocho.what2do.common.auth.dto.TokenDto;
import com.ocho.what2do.common.auth.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserAuthService authService;

  @PostMapping("/reissue")
  public ResponseEntity<TokenDto> reissue(String refreshToken){
    return ResponseEntity.ok().body(authService.reissueToken(refreshToken));
  }
}
