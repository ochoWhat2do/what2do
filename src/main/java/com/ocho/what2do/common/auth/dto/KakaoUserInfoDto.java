package com.ocho.what2do.common.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {
  private String id;
  private String nickname;
  private String email;
  private String picture;

  public KakaoUserInfoDto(String id, String nickname, String email,String picture) {
    this.id = id;
    this.nickname = nickname;
    this.email = email;
    this.picture = picture;
  }
}