package com.ocho.what2do.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SocialUserResponseDto {
  private Long id;
  private String email;
  private boolean admin;
  private String role;
  private String nickname;
  private String picture;
  private String accessToken;
  private String refreshToken;

  @Builder
  public SocialUserResponseDto(String email, String nickname, String role, String picture, String accessToken, String refreshToken) {
    this.email = email;
    this.admin = false;
    this.nickname = nickname;
    this.role = role;
    this.picture = picture;
    this.accessToken = accessToken;
    this.refreshToken =refreshToken;
  }
}
