package com.ocho.what2do.user.dto;

import com.ocho.what2do.user.entity.User;
import com.ocho.what2do.user.entity.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;

@Getter

public class SignupResponseDto {

    private String email;
    private boolean admin;
    private String role;
    private String nickname;


    public SignupResponseDto(User user) {

        this.email = user.getEmail();
        this.admin = user.getRole().equals(UserRoleEnum.ADMIN);
        this.nickname = user.getNickname();
        this.role = user.getRole().getAuthority();

    }
}
