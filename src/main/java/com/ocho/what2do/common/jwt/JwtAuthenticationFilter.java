package com.ocho.what2do.common.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocho.what2do.common.dto.ApiResponseDto;
import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.message.CustomErrorCode;
import com.ocho.what2do.user.dto.LoginRequestDto;
import com.ocho.what2do.user.entity.User;
import com.ocho.what2do.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.server.ResponseStatusException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final JwtUtil jwtUtil;

  private final RedisTemplate redisTemplate;

  public static final String BEARER_PREFIX = "Bearer ";

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public JwtAuthenticationFilter(JwtUtil jwtUtil, RedisTemplate redisTemplate) {
    this.jwtUtil = jwtUtil;
    this.redisTemplate = redisTemplate;
    setFilterProcessesUrl("/api/users/login");
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    log.info("로그인 시도");
    LoginRequestDto requestDto = null;
    try {
      requestDto = new ObjectMapper().readValue(request.getInputStream(),
          LoginRequestDto.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }


    try {
      return getAuthenticationManager().authenticate(
          new UsernamePasswordAuthenticationToken(
              requestDto.getEmail(),
              requestDto.getPassword(),
              null
          )
      );
    } catch (Exception e) {
      if (e instanceof BadCredentialsException) {
        Optional<User> user = userRepository.findByEmail(requestDto.getEmail());
        if(user.isPresent()) {
          User loginUser = user.get();
          if (!passwordEncoder.matches(requestDto.getPassword(), loginUser.getPassword())) {
            throw new CustomException(CustomErrorCode.NOT_VALID_LOGIN_PASSWORD_INFO, e);
          }
        } else {
          throw new CustomException(CustomErrorCode.NOT_VALID_LOGIN_USER_INFO, e);
        }
      }

      throw new CustomException(CustomErrorCode.NOT_VALID_LOGIN_USER_INFO, e);
    }
  }
}
