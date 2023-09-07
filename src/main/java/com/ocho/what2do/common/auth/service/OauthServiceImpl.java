package com.ocho.what2do.common.auth.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.ocho.what2do.common.auth.dto.GoogleAccessResponseDto;
import com.ocho.what2do.common.auth.dto.GoogleUserInfoDto;
import com.ocho.what2do.common.auth.dto.KakaoUserInfoDto;
import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.jwt.JwtUtil;
import com.ocho.what2do.common.message.CustomErrorCode;
import com.ocho.what2do.user.dto.SocialUserResponseDto;
import com.ocho.what2do.user.entity.SocialType;
import com.ocho.what2do.user.entity.User;
import com.ocho.what2do.user.entity.UserRoleEnum;
import com.ocho.what2do.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j(topic = "OAuth Login")
@Service
@RequiredArgsConstructor
public class OauthServiceImpl implements OauthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;
    private String googleTokenUrl = "https://oauth2.googleeapis.com/token";

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;


    @Override
    public SocialUserResponseDto kakaoLogin(String code) {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = null;
        try {
            accessToken = getToken(code);
        } catch (JsonProcessingException e) {
            throw new CustomException(CustomErrorCode.ILLEGAL_JSON_STRING_EXCEPTION);
        }

        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoUserInfoDto kakaoUserInfo = null;
        SocialUserResponseDto socialUserResponseDto = null;
        if (accessToken != null && !accessToken.isEmpty()) {
            try {
                kakaoUserInfo = getKakaoUserInfo(accessToken);
            } catch (JsonProcessingException e) {
                throw new CustomException(CustomErrorCode.ILLEGAL_JSON_STRING_EXCEPTION);
            }

            // 3. 중요시에 회원가입
            User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

            // 4. jwt 토큰 반환
            String createdAccessToken = jwtUtil.createAccessToken(kakaoUserInfo.getEmail(),
                    kakaoUser.getRole()); // JwtService의 createAccessToken을 사용하여 AccessToken 발급
            String refreshToken = jwtUtil.createRefreshToken(); // JwtService의 createRefreshToken을 사용하여 RefreshToken 발급

            userRepository.findByEmail(kakaoUserInfo.getEmail())
                    .ifPresent(user -> {
                        user.updateRefreshToken(refreshToken);
                        userRepository.saveAndFlush(user);
                    });
            socialUserResponseDto = SocialUserResponseDto
                    .builder()
                    .email(kakaoUserInfo.getEmail())
                    .nickname(kakaoUserInfo.getNickname())
                    .role(String.valueOf(kakaoUser.getRole()))
                    .accessToken(createdAccessToken)
                    .refreshToken(refreshToken)
                    .picture(kakaoUser.getPicture())
                    .build();
        }

        return socialUserResponseDto;
    }

    @Override
    public SocialUserResponseDto GoogleLogin(String code) {
        ResponseEntity<String> googleResponse = null;
        // 1. "인가 코드"로 "액세스 토큰" 요청
        try {
            googleResponse = getGoogleToken(code);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // 2. 토큰으로 구글 API 호출 : "액세스 토큰"으로 "구글 사용자 정보" 가져오기
        // ObjectMapper를 통해 String to Object로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // NULL이 아닌 값만 응답받기(NULL인 경우는 생략)

        GoogleAccessResponseDto googleAccessResponseDto = null;
        try {
            googleAccessResponseDto = objectMapper.readValue(googleResponse.getBody(), new TypeReference<GoogleAccessResponseDto>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String userInfoToken = "";

        if (googleAccessResponseDto != null) {
            userInfoToken = googleAccessResponseDto.getIdToken();
        }


        String requestUrl = UriComponentsBuilder.fromHttpUrl("https://oauth2.googleapis.com/tokeninfo").queryParam("id_token", userInfoToken).toUriString();
        String resultJson = restTemplate.getForObject(requestUrl, String.class);

        GoogleUserInfoDto googleUserInfo = null;
        SocialUserResponseDto socialUserResponseDto = null;

        if (resultJson != null) {
            try {
                googleUserInfo = objectMapper.readValue(resultJson, new TypeReference<GoogleUserInfoDto>() {
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        } else {
            throw new CustomException(CustomErrorCode.GOOGLE_LOGIN_FAILED, null);
        }

        // 3. 회원가입
        if (googleUserInfo != null) {
            User googleUser = registerGoogleUserIfNeeded(googleUserInfo);

            // 4. jwt 토큰 반환
            String createdAccessToken = jwtUtil.createAccessToken(googleUserInfo.getEmail(),
                    googleUser.getRole()); // JwtService의 createAccessToken을 사용하여 AccessToken 발급
            String refreshToken = jwtUtil.createRefreshToken(); // JwtService의 createRefreshToken을 사용하여 RefreshToken 발급

            userRepository.findByEmail(googleUserInfo.getEmail())
                    .ifPresent(user -> {
                        user.updateRefreshToken(refreshToken);
                        userRepository.saveAndFlush(user);
                    });
            socialUserResponseDto = SocialUserResponseDto
                    .builder()
                    .email(googleUserInfo.getEmail())
                    .nickname(googleUserInfo.getName())
                    .role(String.valueOf(googleUser.getRole()))
                    .accessToken(createdAccessToken)
                    .refreshToken(refreshToken)
                    .picture(googleUser.getPicture())
                    .build();
        }

        return socialUserResponseDto;
    }

    private ResponseEntity<String> getGoogleToken(String code) throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<>();

        params.put("code", code);
        params.put("client_id", googleClientId);
        params.put("client_secret", googleClientSecret);
        params.put("redirect_url", googleRedirectUri);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String>
                responseEntity = restTemplate.postForEntity(googleTokenUrl, params, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity;
        }
        return null;
    }


    private String getToken(String code) throws JsonProcessingException {
        log.info("인가코드 : " + code);

        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        log.info("accessToken : " + accessToken);

        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        String id = String.valueOf(jsonNode.get("id").asLong());
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();
        String profile_image = jsonNode.get("properties")
                .get("profile_image").asText();

        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);
        return new KakaoUserInfoDto(id, nickname, email, profile_image);
    }


    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        //SocialType.KAKAO
        String kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findBySocialTypeAndSocialId(SocialType.KAKAO,
                String.valueOf(kakaoId)).orElse(null);

        if (kakaoUser == null) {
            // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
            String kakaoEmail = kakaoUserInfo.getEmail();
            User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                // 기존 회원정보에 카카오 Id 추가
                kakaoUser = kakaoUser.socialIdUpdate(SocialType.KAKAO, kakaoId);
            } else {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // email: kakao email
                String email = kakaoUserInfo.getEmail();

                kakaoUser = User.builder()
                        .socialType(SocialType.KAKAO)
                        .socialId(kakaoId)
                        .email(email)
                        .nickname(kakaoUserInfo.getNickname())
                        .picture(kakaoUserInfo.getPicture())
                        .role(UserRoleEnum.GUEST)
                        .password(encodedPassword)
                        .build();
            }

            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }

    private User registerGoogleUserIfNeeded(GoogleUserInfoDto googleUserInfo) {
        // DB 에 중복된 Google Id 가 있는지 확인
        //SocialType.GOOGLE
        String googleId = googleUserInfo.getId();
        User googleUser = userRepository.findBySocialTypeAndSocialId(SocialType.GOOGLE,
                String.valueOf(googleId)).orElse(null);

        if (googleUser == null) {
            // 구글 사용자 email 동일한 email 가진 회원이 있는지 확인
            String googleEmail = googleUserInfo.getEmail();
            User sameEmailUser = userRepository.findByEmail(googleEmail).orElse(null);
            if (sameEmailUser != null) {
                googleUser = sameEmailUser;
                // 기존 회원정보에 구글 Id 추가
                googleUser = googleUser.socialIdUpdate(SocialType.GOOGLE, googleId);
            } else {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // email: google email
                String email = googleUserInfo.getEmail();

                googleUser = User.builder()
                        .socialType(SocialType.GOOGLE)
                        .socialId(googleId)
                        .email(email)
                        .nickname(googleUserInfo.getName())
                        .picture(googleUserInfo.getPicture())
                        .role(UserRoleEnum.GUEST)
                        .password(encodedPassword)
                        .build();
            }

            userRepository.save(googleUser);
        }
        return googleUser;
    }

}
