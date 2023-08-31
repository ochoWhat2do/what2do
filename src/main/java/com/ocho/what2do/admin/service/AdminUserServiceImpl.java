package com.ocho.what2do.admin.service;

import com.ocho.what2do.admin.dto.UserLockRequestDto;
import com.ocho.what2do.common.dto.ApiResponseDto;
import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.common.message.CustomErrorCode;
import com.ocho.what2do.user.entity.User;
import com.ocho.what2do.user.entity.UserRoleEnum;
import com.ocho.what2do.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

  @Value("${admin.token}")
  private String adminToken;

  private final UserRepository userRepository;

  @Override
  public ApiResponseDto checkToken(String token) {
    if (!token.equals(adminToken)) {
      throw new CustomException(CustomErrorCode.NOT_ADMIN_AUTH);
    }

    return new ApiResponseDto(HttpStatus.OK.value(), "관리자 확인 완료하였습니다.");
  }

  @Override
  @Transactional
  public ApiResponseDto changeRole(String email, String roleName) {
    User user = findUserByEmail(email);
    UserRoleEnum role = user.getRole();
    int num = 0;
    if (roleName.equals("ADMIN")) {
      role = UserRoleEnum.ADMIN;
    } else if (roleName.equals("USER")) {
      role = UserRoleEnum.USER;
    } else if (roleName.equals("GUEST")) {
      role = UserRoleEnum.GUEST;
    } else {
      throw new CustomException(CustomErrorCode.HAVE_NOT_USER_ROLE, null);
    }
    user.updateRole(role);

    return new ApiResponseDto(HttpStatus.OK.value(), "사용자 권한 수정 완료하였습니다.");
  }

  @Override
  @Transactional
  public ApiResponseDto lockUser(UserLockRequestDto userLockRequestDto) {
    User user = findUserByEmail(userLockRequestDto.getEmail());
    user.userLock(userLockRequestDto.isLocked());
    return new ApiResponseDto(HttpStatus.OK.value(), userLockRequestDto.getEmail() + "님은 이제 로그인 하실 수 없습니다.");
  }


  private User findUser(Long userId) {
    return userRepository.findById(userId).orElseThrow(
        () -> new CustomException(CustomErrorCode.USER_NOT_FOUND, null));
  }

  private User findUserByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(
        () -> new CustomException(CustomErrorCode.USER_NOT_FOUND, null));
  }
}
