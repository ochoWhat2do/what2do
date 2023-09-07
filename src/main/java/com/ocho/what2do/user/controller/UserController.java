package com.ocho.what2do.user.controller;

import com.ocho.what2do.common.dto.ApiResponseDto;
import com.ocho.what2do.common.security.UserDetailsImpl;
import com.ocho.what2do.user.dto.*;
import com.ocho.what2do.user.entity.User;
import com.ocho.what2do.user.service.RegisterEmail;
import com.ocho.what2do.user.service.UserService;
import com.ocho.what2do.userpassword.dto.EditPasswordRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@Tag(name = "사용자 API", description = "사용자의 회원 가입 기능과 관련된 API 정보를 담고 있습니다.")
public class UserController {
    private final UserService userService;
    private final RegisterEmail registerEmail;

    public UserController(UserService userService, RegisterEmail registerEmail) {
        this.userService = userService;
        this.registerEmail = registerEmail;
    }

    @Operation(summary = "회원 가입", description = "SignupRequesetDto를 통해 회원이 제출한 정보의 유효성 검사 후 통과 시 DB에 저장하고 성공 메시지를 반환합니다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) throws IllegalAccessException {
        userService.signup(requestDto);
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "회원 가입 성공"));
    }

    @Operation(summary = "사용자 정보 조회", description = "LoginRequestDto을 DB에 저장된 사용자 정보와 비교하여 동일할 시 성공 메시지를 반환합니다.")
    @GetMapping("/info")
    public ResponseEntity<UserResponseDto> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        UserResponseDto userResponseDto = userService.getUserInfo(user);
        return ResponseEntity.ok().body(new UserResponseDto(user));
    }

    @Operation(summary = "프로필 수정", description = "프로필 정보를 수정합니다.")
    @PutMapping(value = "/info", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UserProfileDto> changeUserInfo(@RequestPart("profilePic") MultipartFile profilePic,
                                                         @RequestPart("requestDto") EditUserRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        UserProfileDto userProfileDto = userService.editUserInfo(profilePic, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(userProfileDto);
    }

    @Operation(summary = "사용자 정보 삭제(회원탈퇴)", description = "전달된 Bearer 토큰을 통해 본인 혹은 관리자 여부 확인 후 userId를 통해 찾은 사용자의 정보를 삭제합니다.")
    @DeleteMapping("/info")
    public ResponseEntity<ApiResponseDto> deleteUserInfo(@RequestBody WithdrawalRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deleteUserInfo(requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "회원 정보 삭제 성공"));
    }

    @Operation(summary = "사용자 비밀번호 수정", description = "전달된 Bearer 토큰을 통해 본인 확인 후 EditPasswordRequestDto를 통해 해당 사용자의 비밀번호를 수정합니다.")
    @PatchMapping("/password")
    public ResponseEntity<ApiResponseDto> editUserPassword(@Valid @RequestBody EditPasswordRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.editUserPassword(requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "회원 비밀번호 수정 성공"));
    }

    @Operation(summary = "로그아웃", description = "전달받은 access 토큰을 가지고 로그아웃 처리")
    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity<ApiResponseDto> logout(@RequestHeader("Authorization") String requestAccessToken) {
        return ResponseEntity.ok().body(userService.logout(requestAccessToken));
    }

    @Operation(summary = "이메일 중복 체크", description = "입력한 이메일이 이미 등록되었는지 확인합니다.")
    @GetMapping("/checkEmail")
    public ResponseEntity<Boolean> checkDuplicate(@RequestParam String email) {
        boolean isDuplicate = userService.checkDuplicateEmail(email);
        return ResponseEntity.ok().body(isDuplicate);
    }

    @Operation(summary = "프로필 정보", description = "프로필 정보를 받아옵니다.")
    @GetMapping("/profile")
    public UserProfileDto getUserProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserProfileDto userProfileDto = userService.getUserProfile(userDetails.getUser());
        return userProfileDto;
    }

    @Operation(summary = "이메일 인증", description = "회원가입 시 이메일 인증")
    @PostMapping("/confirmEmail")
    public String mailConfirm(@RequestBody EmailAuthRequestDto requestDto) throws Exception {
        return registerEmail.sendSimpleMessage(requestDto.getEmail());
    }

    @Operation(summary = "사용자 목록 조회", description = "사용자 목록 페이징 조회")
    @GetMapping("/list")
    public ResponseEntity<Page<UserInfoResponseDto>> listUsers(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc) {
        Page<UserInfoResponseDto> userDtoList = userService.getUserList(page - 1, size, sortBy, isAsc);
        return ResponseEntity.ok(userDtoList);
    }
}
