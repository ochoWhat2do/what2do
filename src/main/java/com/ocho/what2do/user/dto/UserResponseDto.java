package com.ocho.what2do.user.dto;

import com.ocho.what2do.user.entity.User;
import com.ocho.what2do.user.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private Long id;
    private String email;
    private boolean admin;
    private String role;
    private String nickname;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.admin = user.getRole().equals(UserRoleEnum.ADMIN);
        this.nickname = user.getNickname();
        this.role = user.getRole().getAuthority();
    }
}
