package com.ocho.what2do.common.jwt;

import com.ocho.what2do.common.dto.ApiResponseDto;
import com.ocho.what2do.common.security.UserDetailsImpl;
import com.ocho.what2do.user.dto.LoginRequestDto;
import com.ocho.what2do.user.entity.UserRoleEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final JwtUtil jwtUtil;

  private final RedisTemplate redisTemplate;

  public JwtAuthenticationFilter(JwtUtil jwtUtil, RedisTemplate redisTemplate) {
    this.jwtUtil = jwtUtil;
    this.redisTemplate = redisTemplate;
    setFilterProcessesUrl("/api/users/login");
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    log.info("로그인 시도");
    try {
      LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

      return getAuthenticationManager().authenticate(
          new UsernamePasswordAuthenticationToken(
              requestDto.getEmail(),
              requestDto.getPassword(),
              null
          )
      );
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    log.info("로그인 성공 및 JWT 생성");
    String email = ((UserDetailsImpl) authResult.getPrincipal()).getEmail();
    UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

    String token = jwtUtil.createAccessToken(email, role);
    String refreshToken = jwtUtil.createRefreshToken(authResult);
    response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
    response.addHeader(JwtUtil.AUTHORIZATION_REFRESH_HEADER, refreshToken);

    response.setStatus(200);
    response.setContentType("application/json");
    String result = new ObjectMapper().writeValueAsString(new ApiResponseDto(HttpStatus.OK.value(), "Login Success"));

    response.getOutputStream().print(result);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    log.info("로그인 실패");

    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    response.setContentType("application/json");
    String result = new ObjectMapper().writeValueAsString(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), "Login Failed"));

    response.getOutputStream().print(result);
  }
}
