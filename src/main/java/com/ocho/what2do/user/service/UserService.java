package com.ocho.what2do.user.service;

import com.ocho.what2do.common.dto.ApiResponseDto;
import com.ocho.what2do.user.dto.EditUserRequestDto;
import com.ocho.what2do.user.dto.SignupRequestDto;
import com.ocho.what2do.user.dto.UserProfileDto;
import com.ocho.what2do.user.dto.UserResponseDto;
import com.ocho.what2do.user.dto.WithdrawalRequestDto;
import com.ocho.what2do.user.entity.User;
import com.ocho.what2do.userpassword.dto.EditPasswordRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

// 사용자 서비스
public interface UserService {
    /*
     * 회원 가입
     * @param requestDto 사용자 회원 가입을 위한 요청 정보
     */
    void signup(SignupRequestDto requestDto) throws IllegalAccessException;

    /*
     * 유저 정보 삭제
     * @param userId 삭제할 사용자의 식별 ID
     * @param user 삭제할 정보를 소유하고 있는 사용자 본인 혹은 관리자에 대한 정보
     */
    void deleteUserInfo(WithdrawalRequestDto requestDto, User user);

    /*
     * 유저 비밀번호 수정
     * @param requestDto 사용자 비밀번호 수정을 위한 요청 정보
     * @param user 수정할 정보를 소유하고 있는 사용자 본인에 대한 정보
     */
    void editUserPassword(EditPasswordRequestDto requestDto, User user);
    
    /*
     * 로그아웃
     * @param requestAccessToken 엑세스토큰
     * @return ApiResponseDto api 실행결과 리턴
     */
    ApiResponseDto logout(String requestAccessToken);


    /*
     * 이메일 중복체크
     * @param email 이메일
     * @return boolean 이메일이 중복되었는지 확인 후 리턴
     */
    boolean checkDuplicateEmail(String email);

    /*
     * 프로필 조회
     * @param User 사용자 정보
     * @return UserProfileDto 프로필 정보
     */
    UserProfileDto getUserProfile(User user);

    /*
     * 사용자정보 조회
     * @param User 사용자 정보
     * @return UserResponseDto 사용자 정보
     */
    UserResponseDto getUserInfo(User user);

    /*
     * 유저 정보 수정
     * @param requestDto 사용자 정보 수정을 위한 요청 정보
     * @param user 수정할 정보를 소유하고 있는 사용자 본인에 대한 정보
     * @return user 수정된 정보를 지닌 사용자를 반환
     */
    UserProfileDto editUserInfo(MultipartFile profilePic, EditUserRequestDto requestDto, User user);

    /*

    */
    User findUserById(Long userId);
}
