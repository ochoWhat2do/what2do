package com.ocho.what2do.user.dto;

import com.ocho.what2do.user.entity.User;
import lombok.Getter;

@Getter
public class UserProfileDto {

  private String picture;
  private String email;
  private String introduction;
  private String nickname;
  private int favoriteNum;

  public UserProfileDto(User user) {
    this.picture = user.getPicture();
    this.email = user.getEmail();
    this.introduction = user.getIntroduction();
    this.favoriteNum = user.getStoreFavorites().size();
    this.nickname = user.getNickname();
  }

}
