package com.ocho.what2do.user.dto;

import com.ocho.what2do.user.entity.User;
import com.ocho.what2do.user.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private Long userId;
    private String email;
    private boolean admin;
    private String role;
    private String nickname;
    private String picture;

    public UserResponseDto(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.admin = user.getRole().equals(UserRoleEnum.ADMIN);
        this.nickname = user.getNickname();
        this.role = user.getRole().getAuthority();
        this.picture = user.getPicture();
    }
}
