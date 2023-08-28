package com.ocho.what2do.user.service;

import com.ocho.what2do.common.dto.ApiResponseDto;
import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.jwt.JwtUtil;
import com.ocho.what2do.common.message.CustomErrorCode;
import com.ocho.what2do.common.redis.RedisUtil;
import com.ocho.what2do.common.security.UserDetailsImpl;
import com.ocho.what2do.user.dto.EditUserRequestDto;
import com.ocho.what2do.user.dto.SignupRequestDto;
import com.ocho.what2do.user.entity.User;
import com.ocho.what2do.user.entity.UserRoleEnum;
import com.ocho.what2do.user.repository.UserRepository;
import com.ocho.what2do.userpassword.dto.EditPasswordRequestDto;
import com.ocho.what2do.userpassword.entity.UserPassword;
import com.ocho.what2do.userpassword.repository.UserPasswordRepository;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserPasswordRepository userPasswordRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final RedisTemplate<String, String> redisTemplate;
  private final RedisUtil redisUtil;

  @Value("${admin.token}")
  private String adminToken;

  public static final String BEARER_PREFIX = "Bearer ";

  @Transactional
  @Override
  public void signup(SignupRequestDto requestDto) {
    String email = requestDto.getEmail();
    String password = passwordEncoder.encode(requestDto.getPassword());

    // 사용자 존재 여부 확인
    if (findUserByEmail(email) != null) {
      throw new CustomException(CustomErrorCode.USER_ALREADY_EXISTS, null);
    }

    UserRoleEnum role = UserRoleEnum.USER;
    if (requestDto.isAdmin() && requestDto.getAdminToken().equals(adminToken)) {
      role = UserRoleEnum.ADMIN;
    }

    User user = userRepository.save(new User(email, password, role, requestDto.getCity(),
        requestDto.getGender()));
    userPasswordRepository.save(new UserPassword(password, user));
  }

  @Transactional
  @Override
  public User editUserInfo(EditUserRequestDto requestDto, User user) {
    User found = findUser(user.getId());
    if (found == null) {
      throw new CustomException(CustomErrorCode.USER_NOT_FOUND, null);
    }

    found.editUserInfo(requestDto.getNickname(), requestDto.getIntroduction());
    return found;
  }

  @Transactional
  @Override
  public void deleteUserInfo(Long userId, User user) {
    User found = findUser(userId);

    confirmUser(found, user);

    userPasswordRepository.deleteAllByUser_Id(found.getId());
    userRepository.deleteById(found.getId());
  }

  @Transactional
  @Override
  public void editUserPassword(EditPasswordRequestDto requestDto, User user) {
    User found = findUser(user.getId());

    checkPassword(requestDto.getPassword(), found.getPassword());
    checkNewPassword(requestDto.getNewPassword(), requestDto.getNewPasswordConfirm());
    checkRecentPasswords(found.getId(), requestDto.getNewPassword());

    String newPassword = passwordEncoder.encode(requestDto.getNewPassword());
    userPasswordRepository.save(new UserPassword(newPassword, found));
    found.editPassword(newPassword);
  }

  @Override
  @Transactional
  public ApiResponseDto logout(String requestAccessToken) {
    if (StringUtils.hasText(requestAccessToken) && requestAccessToken.startsWith(BEARER_PREFIX)) {
      requestAccessToken = requestAccessToken.substring(7);
    }

    //엑세스 토큰 남은 유효시간
    Long expiration = jwtUtil.getExpiration(requestAccessToken);

    // 1. Access Token 검증
    if (!jwtUtil.validateToken(requestAccessToken)) {
      return new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), "잘못된 요청입니다.");
    }
    // 2. Access Token 에서 User 정보를 가져옵니다.
    Authentication authentication = jwtUtil.getAuthentication(requestAccessToken);
    String email = ((UserDetailsImpl) authentication.getPrincipal()).getUser().getEmail();

    // 3. Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
    Optional<User> optionalUser = userRepository.findByEmail(email);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      user.updateRefreshToken(null);
      userRepository.saveAndFlush(user);
    }
    // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
    redisUtil.setBlackList(requestAccessToken, "logout", expiration);

    return new ApiResponseDto(HttpStatus.OK.value(), "로그아웃 성공");
  }

  private User findUserByEmail(String email) {
    return userRepository.findByEmail(email).orElse(null);
  }

  private User findUser(Long userId) {
    return userRepository.findById(userId).orElseThrow(
        () -> new CustomException(CustomErrorCode.USER_NOT_FOUND, null));
  }

  private void confirmUser(User user1, User user2) {
    if (!user1.getId().equals(user2.getId()) && user2.getRole() != UserRoleEnum.ADMIN) {
      throw new CustomException(CustomErrorCode.UNAUTHORIZED_REQUEST, null);
    }
  }

  private void checkPassword(String inputPassword, String userPassword) {
    if (!passwordEncoder.matches(inputPassword, userPassword)) {
      throw new CustomException(CustomErrorCode.OLD_PASSWORD_MISMATCHED, null);
    }
  }

  private void checkNewPassword(String newPassword1, String newPassword2) {
    if (!newPassword1.equals(newPassword2)) {
      throw new CustomException(CustomErrorCode.NEW_PASSWORD_MISMATCHED, null);
    }
  }

  private void checkRecentPasswords(Long userId, String newPassword) {
    List<UserPassword> userPasswords = userPasswordRepository.get3RecentPasswords(userId);
    userPasswords.forEach(password -> {
      if (passwordEncoder.matches(newPassword, password.getPassword())) {
        throw new CustomException(CustomErrorCode.PASSWORD_RECENTLY_USED, null);
      }
    });
  }
}
