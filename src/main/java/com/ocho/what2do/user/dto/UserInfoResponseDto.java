package com.ocho.what2do.user.dto;

import com.ocho.what2do.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponseDto {
    private Long id;
    private String nickname;
    private String email;

    public UserInfoResponseDto(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
    }
}
