package com.ocho.what2do.user.entity;

import java.io.Serializable;
import lombok.Getter;

/**
 * 직렬화 기능을 가진 User클래스
 */
@Getter
public class SessionUser implements Serializable {
  private String nickname;
  private String email;
  private String picture;

  public SessionUser(User user){
    this.nickname = user.getNickname();
    this.email = user.getEmail();
    this.picture = user.getPicture();
  }
}
