package com.ocho.what2do.common.jwt;

import com.ocho.what2do.common.redis.RedisUtil;
import com.ocho.what2do.common.security.UserDetailsImpl;
import com.ocho.what2do.common.security.UserDetailsServiceImpl;
import com.ocho.what2do.user.entity.UserRoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
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

@Component
@RequiredArgsConstructor
public class JwtUtil {
  // JWT 데이터
  // Header KEY 값
  public static final String AUTHORIZATION_HEADER = "Authorization";
  // 사용자 권한 값의 KEY
  public static final String AUTHORIZATION_KEY = "auth";
  // Token 식별자
  public static final String BEARER_PREFIX = "Bearer ";
  // 토큰 만료시간
  private static final long TOKEN_TIME = 60 * 60 * 1000L; // 60 분

  // refresh 토큰 헤더 값
  public static final String AUTHORIZATION_REFRESH_HEADER = "Authorization_Refresh";

  // refresh 토큰 만료시간
  private static final long REFRESH_TOKEN_TIME = (60 * 1000) * 60 * 24 * 7; // 7일

  private final RedisTemplate<String, String> redisTemplate;

  private final RedisTemplate<String, Object> redisBlackListTemplate;

  private final UserDetailsServiceImpl userDetailsService;
  
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
  }

  // JWT 생성
  // 토큰 생성
  public String createAccessToken(String email, UserRoleEnum role) {
    Date date = new Date();

    return BEARER_PREFIX +
        Jwts.builder()
            .setSubject(email) // 사용자 식별자값(ID) -- 기존의 username에서 email로 변경
            .claim(AUTHORIZATION_KEY, role) // 사용자 권한
            .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
            .setIssuedAt(date) // 발급일
            .signWith(key, signatureAlgorithm) // 암호화 알고리즘
            .compact();
  }

  /**
   * RefreshToken 생성
   * RefreshToken은 Claim에 email도 넣지 않으므로 withClaim() X
   */
  public String createRefreshToken(Authentication authentication) {
    long now = (new Date()).getTime();

    String refreshToken = Jwts.builder()
        .setExpiration(new Date(now + REFRESH_TOKEN_TIME))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
    // redis에 저장
    redisTemplate.opsForValue().set(
        "RT:" + ((UserDetailsImpl) authentication.getPrincipal()).getEmail(),
        refreshToken,
        REFRESH_TOKEN_TIME,
        TimeUnit.MILLISECONDS
    );

    return refreshToken;
  }

  // HttpServletRequest 에서 Header Value : JWT 가져오기
  public String getTokenFromRequest(HttpServletRequest req) {
    String bearerToken = req.getHeader(AUTHORIZATION_HEADER);
    if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(BEARER_PREFIX.length());
    }
    return null;
  }

  // 토큰 검증
  public boolean validateToken(String token) {
    try {
      if(redisUtil.hasKeyBlackList(token)) {
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
        .getBody().getSubject();
    UserDetails userDetails = userDetailsService.loadUserByUsername(userPrincipal.replace("RT:", ""));

    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  //JWT 토큰의 만료시간
  public Long getExpiration(String accessToken){

    Date expiration = Jwts.parserBuilder().setSigningKey(secretKey)
        .build().parseClaimsJws(accessToken).getBody().getExpiration();

    long now = new Date().getTime();
    return expiration.getTime() - now;
  }

  // 토큰에서 사용자 정보 가져오기
  public Claims getUserInfoFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }
}