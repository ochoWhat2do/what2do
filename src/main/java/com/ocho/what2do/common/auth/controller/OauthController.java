package com.ocho.what2do.common.auth.controller;

import com.ocho.what2do.common.auth.service.OauthService;
import com.ocho.what2do.user.dto.SocialUserResponseDto;
import com.ocho.what2do.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
}
