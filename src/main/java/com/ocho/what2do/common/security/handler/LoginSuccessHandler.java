package com.ocho.what2do.common.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocho.what2do.common.auth.dto.TokenDto;
import com.ocho.what2do.common.dto.ApiResponseDto;
import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.jwt.JwtUtil;
import com.ocho.what2do.common.message.CustomErrorCode;
import com.ocho.what2do.common.security.UserDetailsImpl;
import com.ocho.what2do.user.entity.UserRoleEnum;
import com.ocho.what2do.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;

  private static final long ACCESS_TOKEN_TIME = 10 * 60 * 1000L; // 10 분

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    UserRoleEnum role = ((UserDetailsImpl) authentication.getPrincipal()).getUser().getRole();
    String email = extractUsername(authentication); // 인증 정보에서 Username(email) 추출

    // 만약 계정이 정지되었다면
    if(((UserDetailsImpl) authentication.getPrincipal()).getUser().isLocked()){
      // 계정정지 예외 발생
      throw new CustomException(CustomErrorCode.LOGIN_USER_ACCOUNT_LOCKED);
    }

    String accessToken = jwtUtil.createAccessToken(email,
        role); // JwtService의 createAccessToken을 사용하여 AccessToken 발급
    String refreshToken = jwtUtil.createRefreshToken(); // JwtService의 createRefreshToken을 사용하여 RefreshToken 발급
    TokenDto tokenDto = new TokenDto(
        accessToken,
        refreshToken
    );

    jwtUtil.sendAccessAndRefreshToken(response,
        tokenDto); // 응답 헤더에 AccessToken, RefreshToken 실어서 응답

    userRepository.findByEmail(email)
        .ifPresent(user -> {
          user.updateRefreshToken(refreshToken);
          userRepository.saveAndFlush(user);
        });
    log.info("로그인에 성공하였습니다. 이메일 : {}", email);
    log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
    log.info("발급된 AccessToken 만료 기간 : {}", ACCESS_TOKEN_TIME);

    response.setStatus(200);
    response.setContentType("application/json");
    String result = null;
    try {
      result = new ObjectMapper().writeValueAsString(
          new ApiResponseDto(HttpStatus.OK.value(), "Login Success"));
      response.getOutputStream().print(result);
    } catch (IOException e) {
      throw new CustomException(CustomErrorCode.ILLEGAL_JSON_STRING_EXCEPTION, e);
    }
  }

  private String extractUsername(Authentication authentication) {
    return ((UserDetailsImpl) authentication.getPrincipal()).getUser().getEmail();
  }
}
