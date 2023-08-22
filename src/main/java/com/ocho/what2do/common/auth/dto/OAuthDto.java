package com.ocho.what2do.common.auth.dto;

import com.ocho.what2do.user.entity.UserRoleEnum;
import java.util.Map;
import lombok.Builder;
import com.ocho.what2do.user.entity.User;
import lombok.Getter;

@Getter
public class OAuthDto {
  private Map<String, Object> attributes; // OAuth2 반환하는 유저 정보 Map
  private String nameAttributeKey;
  private String name;
  private String email;
  private String picture;

  @Builder
  public OAuthDto(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
    this.attributes = attributes;
    this.nameAttributeKey = nameAttributeKey;
    this.name = name;
    this.email = email;
    this.picture = picture;
  }

  public static OAuthDto of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
    //(new!) kakao
    if("kakao".equals(registrationId)){
      return ofKakao("id", attributes);
    }
    else {
      return null;
    }
  }

  // (new!)
  private static OAuthDto ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
    // kakao는 kakao_account에 유저정보가 있다. (email)
    Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
    // kakao_account안에 또 profile이라는 JSON객체가 있다. (nickname, profile_image)
    Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

    return OAuthDto.builder()
        .name((String) kakaoProfile.get("nickname"))
        .email((String) kakaoAccount.get("email"))
        .picture((String) kakaoProfile.get("profile_image_url"))
        .attributes(attributes)
        .nameAttributeKey(userNameAttributeName)
        .build();
  }

  public User toEntity(){
    return User.builder()
        .nickname(name)
        .email(email)
        .picture(picture)
        .role(UserRoleEnum.GUEST) // 기본 권한 GUEST
        .build();
  }
}
