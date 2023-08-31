package com.ocho.what2do.common.jwt;

import com.ocho.what2do.common.auth.dto.TokenDto;
import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.message.CustomErrorCode;
import com.ocho.what2do.common.security.UserDetailsImpl;
import com.ocho.what2do.common.security.UserDetailsServiceImpl;
import com.ocho.what2do.common.util.PasswordGenerator;
import com.ocho.what2do.user.entity.User;
import com.ocho.what2do.user.entity.UserRoleEnum;
import com.ocho.what2do.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;

  private static final String NO_CHECK_URL = "/api/users/login"; // "login"으로 들어오는 요청은 Filter 작동 X
  private static final String NO_CHECK_URL_SIGNUP = "/api/users/signup";

  private static final String START_CHECK_URL_ADMIN = "/api/admin";

  public static final String AUTHORIZATION_ACCESS_HEADER = "Authorization";
  private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Override
  protected void doFilterInternal(
      HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
      throws ServletException, IOException {
    if (req.getRequestURI().equals(NO_CHECK_URL)
    || req.getRequestURI().equals(NO_CHECK_URL_SIGNUP)
    ) {
      filterChain.doFilter(req, res); // "login" 요청이 들어오면, 다음 필터 호출
      return; // return으로 이후 현재 필터 진행 막기 (안해주면 아래로 내려가서 계속 필터 진행시킴)
    }

    // 사용자 요청 헤더에서 RefreshToken 추출
    // -> RefreshToken이 없거나 유효하지 않다면(redis에 저장된 RefreshToken과 다르다면) null을 반환
    // 사용자의 요청 헤더에 RefreshToken이 있는 경우는, AccessToken이 만료되어 요청한 경우밖에 없다.
    // 따라서, 위의 경우를 제외하면 추출한 refreshToken은 모두 null
    String refreshToken = jwtUtil.extractRefreshToken(req)
        .filter(jwtUtil::validateToken)
        .orElse(null);

    // 리프레시 토큰이 요청 헤더에 존재했다면, 사용자가 AccessToken이 만료되어서
    // RefreshToken까지 보낸 것이므로 리프레시 토큰이 DB의 리프레시 토큰과 일치하는지 판단 후,
    // 일치한다면 AccessToken을 재발급해준다.
    if (refreshToken != null) {
      checkRefreshTokenAndReIssueAccessToken(res, refreshToken);
      return; // RefreshToken을 보낸 경우에는 AccessToken을 재발급 하고 인증 처리는 하지 않게 하기위해 바로 return으로 필터 진행 막기
    }
    // RefreshToken이 없거나 유효하지 않다면, AccessToken을 검사하고 인증을 처리하는 로직 수행
    // AccessToken이 없거나 유효하지 않다면, 인증 객체가 담기지 않은 상태로 다음 필터로 넘어가기 때문에 403 에러 발생
    // AccessToken이 유효하다면, 인증 객체가 담긴 상태로 다음 필터로 넘어가기 때문에 인증 성공
    if (refreshToken == null) {
      checkAccessTokenAndAuthentication(req, res, filterChain);
    }
  }

  /**
   * [액세스 토큰 체크 & 인증 처리 메소드] request에서 extractAccessToken()으로 액세스 토큰 추출 후, isTokenValid()로 유효한 토큰인지
   * 검증 유효한 토큰이면, 액세스 토큰에서 extractEmail로 Email을 추출한 후 findByEmail()로 해당 이메일을 사용하는 유저 객체 반환 그 유저 객체를
   * saveAuthentication()으로 인증 처리하여 인증 허가 처리된 객체를 SecurityContextHolder에 담기 그 후 다음 인증 필터로 진행
   */
  private void checkAccessTokenAndAuthentication(HttpServletRequest req, HttpServletResponse res,
      FilterChain filterChain)
      throws ServletException, IOException {
    log.info("checkAccessTokenAndAuthentication() 호출");
    jwtUtil.extractAccessToken(req)
        .filter(jwtUtil::validateToken)
        .ifPresent(accessToken -> jwtUtil.getEmailFromToken(accessToken)
            .ifPresent(email -> userRepository.findByEmail(email)
                .ifPresent(this::saveAuthentication)));

    filterChain.doFilter(req, res);
  }

  /**
   * [리프레시 토큰으로 유저 정보 찾기 & 액세스 토큰/리프레시 토큰 재발급 메소드] 파라미터로 들어온 헤더에서 추출한 리프레시 토큰으로 DB에서 유저를 찾고, 해당 유저가
   * 있다면 JwtService.createAccessToken()으로 AccessToken 생성, reIssueRefreshToken()로 리프레시 토큰 재발급 & DB에
   * 리프레시 토큰 업데이트 메소드 호출 그 후 JwtService.sendAccessTokenAndRefreshToken()으로 응답 헤더에 보내기
   */
  public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response,
      String refreshToken) {

    Optional<User> optionalUser = userRepository.findByRefreshToken(refreshToken);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      UserRoleEnum role = user.getRole();
      String reIssuedRefreshToken = reIssueRefreshToken(user);
      TokenDto tokenDto = new TokenDto(
          jwtUtil.createAccessToken(user.getEmail(), role),
          reIssuedRefreshToken
      );
      jwtUtil.sendAccessAndRefreshToken(response, tokenDto);
    }
  }

  /**
   * [인증 허가 메소드] 파라미터의 유저 : 우리가 만든 회원 객체 / 빌더의 유저 : UserDetails의 User 객체
   * <p>
   * new UsernamePasswordAuthenticationToken()로 인증 객체인 Authentication 객체 생성
   * UsernamePasswordAuthenticationToken의 파라미터 1. UserDetailsUser 객체 (유저 정보) 2. credential(보통 비밀번호로,
   * 인증 시에는 보통 null로 제거) 3. Collection < ? extends GrantedAuthority>로, UserDetails의 User 객체 안에
   * Set<GrantedAuthority> authorities이 있어서 getter로 호출한 후에, new NullAuthoritiesMapper()로
   * GrantedAuthoritiesMapper 객체를 생성하고 mapAuthorities()에 담기
   * <p>
   * SecurityContextHolder.createEmptyContext()로 SecurityContext를 생성 후, setAuthentication()을 이용하여
   * 위에서 만든 Authentication 객체에 대한 인증 허가 처리
   */
  public void saveAuthentication(User myUser) {
    String password = myUser.getPassword();
    if (password == null) { // 소셜 로그인 유저의 비밀번호 임의로 설정 하여 소셜 로그인 유저도 인증 되도록 설정
      password = PasswordGenerator.generateRandomPassword();
    }

    UserDetailsImpl userDetailsImpl =
        new UserDetailsImpl(myUser);

    Authentication authentication =
        new UsernamePasswordAuthenticationToken(userDetailsImpl, null,
            authoritiesMapper.mapAuthorities(userDetailsImpl.getAuthorities()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }


  /**
   * [리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트 메소드] jwtService.createRefreshToken()으로 리프레시 토큰 재발급 후 DB에 재발급한
   * 리프레시 토큰 업데이트 후 Flush
   */
  private String reIssueRefreshToken(User user) {
    String reIssuedRefreshToken = jwtUtil.createRefreshToken();
    user.updateRefreshToken(reIssuedRefreshToken);
    userRepository.saveAndFlush(user);
    return reIssuedRefreshToken;
  }
}
