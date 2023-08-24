package com.ocho.what2do.common.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocho.what2do.common.auth.dto.TokenDto;
import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.message.CustomErrorCode;
import com.ocho.what2do.common.redis.RedisUtil;
import com.ocho.what2do.common.security.UserDetailsImpl;
import com.ocho.what2do.common.security.UserDetailsServiceImpl;
import com.ocho.what2do.user.entity.UserRoleEnum;
import com.ocho.what2do.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j(topic = "JWT 설정")
@Component
@RequiredArgsConstructor
public class JwtUtil {

  // JWT 데이터
  // Header KEY 값
  public static final String AUTHORIZATION_ACCESS_HEADER = "Authorization";
  // 사용자 권한 값의 KEY
  public static final String AUTHORIZATION_KEY = "auth";

  /**
   * JWT의 Subject와 Claim으로 email 사용 -> 클레임의 name을 "email"으로 설정
   * JWT의 헤더에 들어오는 값 : 'Authorization(Key) = Bearer {토큰} (Value)' 형식
   */
  private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
  private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
  private static final String EMAIL_CLAIM = "email";
  private static final String BEARER_PREFIX = "Bearer ";


  // refresh 토큰 헤더 값
  public static final String AUTHORIZATION_REFRESH_HEADER = "Authorization_Refresh";

  // access 토큰 만료시간
  // private static final long TOKEN_TIME = 60 * 60 * 1000L; // 60 분

  private static final long TOKEN_TIME = 1 * 60 * 1000L; // 1 분

  // refresh 토큰 만료시간
  // private static final long REFRESH_TOKEN_TIME = (60 * 1000) * 60 * 24 * 7; // 7일
  private static final long REFRESH_TOKEN_TIME = 1 * 60 * 1000L;

  private final RedisTemplate<String, String> redisTemplate;

  private final UserDetailsServiceImpl userDetailsService;

  private final UserRepository userRepository;

  protected ObjectMapper objectMapper;

  // 레디스 처리 객체
  private final RedisUtil redisUtil;

  @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
  private String secretKey;
  private Key key;
  private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

  // 로그 설정
  public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

  @PostConstruct
  public void init() {
    byte[] bytes = Base64.getDecoder().decode(secretKey);
    key = Keys.hmacShaKeyFor(bytes);
    objectMapper = new ObjectMapper();
  }

  // JWT 생성
  // 토큰 생성
  public String createAccessToken(String email, UserRoleEnum role) {
    Date date = new Date();

    return
        Jwts.builder()
            .setSubject(ACCESS_TOKEN_SUBJECT)
            .claim(EMAIL_CLAIM, email) // 사용자 식별자값(ID) -- email 사용
            .claim(AUTHORIZATION_KEY, role) // 사용자 권한
            .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
            .setIssuedAt(date) // 발급일
            .signWith(key, signatureAlgorithm) // 암호화 알고리즘
            .compact();
  }

  /**
   * RefreshToken 생성 RefreshToken은 Claim에 email도 넣지 않으므로 withClaim() X
   */
  public String createRefreshToken() {
    long now = (new Date()).getTime();

    String refreshToken =
        Jwts.builder()
            .setSubject(REFRESH_TOKEN_SUBJECT)
            .setExpiration(new Date(now + REFRESH_TOKEN_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

    return refreshToken;
  }

  // HttpServletRequest 에서 Header Value : JWT 가져오기
  public String getTokenFromRequest(HttpServletRequest req) {
    String bearerToken = req.getHeader(AUTHORIZATION_ACCESS_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(BEARER_PREFIX.length());
    }
    return null;
  }

  /**
   * 헤더에서 RefreshToken 추출 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서 헤더를 가져온 후 "Bearer"를
   * 삭제(""로 replace)
   */
  public Optional<String> extractRefreshToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(AUTHORIZATION_REFRESH_HEADER))
        .filter(refreshToken -> refreshToken.startsWith(BEARER_PREFIX))
        .map(refreshToken -> refreshToken.replace(BEARER_PREFIX, ""));
  }

  /**
   * 헤더에서 AccessToken 추출 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서 헤더를 가져온 후 "Bearer"를
   * 삭제(""로 replace)
   */
  public Optional<String> extractAccessToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(AUTHORIZATION_ACCESS_HEADER))
        .filter(refreshToken -> refreshToken.startsWith(BEARER_PREFIX))
        .map(refreshToken -> refreshToken.replace(BEARER_PREFIX, ""));
  }


  // 토큰 검증
  public boolean validateToken(String token) {
    try {
      if (redisUtil.hasKeyBlackList(token)) {
        return false;
      }
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
    } catch (ExpiredJwtException e) {
      logger.error("Expired JWT token, 만료된 JWT token 입니다.");
    } catch (UnsupportedJwtException e) {
      logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
    }
    return false;
  }

  /**
   * 토큰으로부터 클레임을 만들고, 이를 통해 User 객체 생성해 Authentication 객체 반환
   */
  public Authentication getAuthentication(String token) {
    String userPrincipal = Jwts.parser().
        setSigningKey(secretKey)
        .parseClaimsJws(token)
        .getBody().get(EMAIL_CLAIM).toString();
    UserDetails userDetails = userDetailsService.loadUserByUsername(userPrincipal);

    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  //JWT 토큰의 만료시간
  public Long getExpiration(String accessToken) {

    Date expiration = Jwts.parserBuilder().setSigningKey(secretKey)
        .build().parseClaimsJws(accessToken).getBody().getExpiration();

    long now = new Date().getTime();
    return expiration.getTime() - now;
  }

  // 토큰에서 사용자 정보 가져오기
  public Claims getUserInfoFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }

  /**
   * AccessToken + RefreshToken 헤더에 실어서 보내기
   */
  public void sendAccessAndRefreshToken(HttpServletResponse response, TokenDto tokenDto) {
    response.setStatus(HttpServletResponse.SC_OK);

    setAccessTokenHeader(response, tokenDto.getAccessToken());
    setRefreshTokenHeader(response, tokenDto.getRefreshToken());
    log.info("Access Token, Refresh Token 헤더 설정 완료");
  }

  /**
   * AccessToken 헤더 설정
   */
  public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
    response.setHeader(AUTHORIZATION_ACCESS_HEADER, accessToken);
  }

  /**
   * RefreshToken 헤더 설정
   */
  public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
    response.setHeader(AUTHORIZATION_REFRESH_HEADER, refreshToken);
  }

  // 토큰에서 email 가져오는 기능
  public Optional<String> getEmailFromToken(String token) {
    return
        Optional.ofNullable(
            Jwts.parserBuilder().setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
  }


  // 매개변수에 헤더이름 추가
  public String parseBearerToken(HttpServletRequest request, String headerName) {
    return Optional.ofNullable(request.getHeader(headerName))
        .filter(token -> token.substring(0, 7).equalsIgnoreCase("Bearer "))
        .map(token -> token.substring(7))
        .orElse(null);
  }

}