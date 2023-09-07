package com.ocho.what2do.common.auth.controller;

import com.ocho.what2do.common.auth.service.OauthService;
import com.ocho.what2do.user.dto.SocialUserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OauthController {

  private final OauthService oauthService;

  @Operation(summary = "카카오 로그인", description = "카카오 로그인 처리.")
  @PostMapping("/oauth/kakao")
  public ResponseEntity KakaoLogin(@RequestParam String code) {

    SocialUserResponseDto socialUserResponseDto = oauthService.kakaoLogin(code);

    return ResponseEntity.ok()
        .header("Authorization", socialUserResponseDto.getAccessToken())
        .header("Authorization_refresh", socialUserResponseDto.getRefreshToken())
        .body(socialUserResponseDto);
  }

  @Operation(summary = "구글 로그인", description = "구글 로그인 처리.")
  @PostMapping("/login/oauth2/code/google")
  public ResponseEntity GoogleLogin(@RequestParam String code) {

    SocialUserResponseDto socialUserResponseDto = oauthService.GoogleLogin(code);

    return ResponseEntity.ok()
            .header("Authorization", socialUserResponseDto.getAccessToken())
            .header("Authorization_refresh", socialUserResponseDto.getRefreshToken())
            .body(socialUserResponseDto);
  }
}
