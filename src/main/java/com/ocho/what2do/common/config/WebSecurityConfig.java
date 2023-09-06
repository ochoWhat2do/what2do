package com.ocho.what2do.common.config;

import com.ocho.what2do.common.jwt.JwtAuthenticationFilter;
import com.ocho.what2do.common.jwt.JwtAuthorizationFilter;
import com.ocho.what2do.common.jwt.JwtUtil;
import com.ocho.what2do.common.security.handler.LoginFailureHandler;
import com.ocho.what2do.common.security.handler.LoginSuccessHandler;
import com.ocho.what2do.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity  // Spring Security 지원을 가능하게 함
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final RedisTemplate redisTemplate;
    private final UserRepository userRepository;

    /*private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;*/
/*  private final UserOAuthService userOAuthService;
  private final OAuth2SuccessHandler oAuth2SuccessHandler;*/
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil, redisTemplate);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        filter.setAuthenticationSuccessHandler(loginSuccessHandler());
        filter.setAuthenticationFailureHandler(loginFailureHandler());
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userRepository);
    }

    /**
     * 로그인 성공 시 호출되는 LoginSuccessHandler 빈 등록
     */
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtUtil, userRepository);
    }

    /**
     * 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
     */
    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());

        // Session 방식 -> JWT 방식 설정 변경
        http.sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(authorizeHttpRequests ->
                authorizeHttpRequests
                        .requestMatchers(HttpMethod.POST, "/api/users/**").permitAll() // '/api/users'로 시작하는 요청 중 모든 POST 접근 허가
                        .requestMatchers("/swagger-ui/**", "/v3/**").permitAll() // swagger-ui 와 관련된 모든 요청 접근 허가
                        .requestMatchers("/oauth/kakao").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/info").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/checkEmail").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/confirmEmail").permitAll()
                        //  .requestMatchers("/admin/**").permitAll()
                        .requestMatchers("/api/daum/**").permitAll() // daum 지역 api 요청 접근 허가
                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

/*    http.oauth2Login(oauth2 -> oauth2
        .defaultSuccessUrl("/auth/login-success")
        .userInfoEndpoint(userInfo -> userInfo
            .userService(userOAuthService))
        .successHandler(oAuth2SuccessHandler)
    );*/

    /*http.oauth2Login(oauth2 -> oauth2
        .defaultSuccessUrl("/auth/login-success")
        .userInfoEndpoint(userInfo -> userInfo
            .userService(customOAuth2UserService))
        .successHandler(oAuth2LoginSuccessHandler)
        .failureHandler(oAuth2LoginFailureHandler)
    );*/

/*    http.oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo
            .userService(customOAuth2UserService))
        .successHandler(oAuth2LoginSuccessHandler)
        .failureHandler(oAuth2LoginFailureHandler)
    );*/

        // userInfoEndpoint : oauth2Login에 성공하면 customOAuth2UserService에서 설정을 진행하겠다라는 의미입니다.

        // 필터 관리
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
