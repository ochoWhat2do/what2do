package com.ocho.what2do.user.entity;


import com.ocho.what2do.storefavorite.entity.StoreFavorite;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor // 생성자를 직접 만들어주었기 때문에 기초 생성자가 필수라서 추가
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column
    private String picture;

    @Column
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<StoreFavorite> storeFavorites = new ArrayList<>();

    private String nickname;

    @Column
    private String gender;

    @Column
    private String city;

    @Column
    private String introduction;

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, NAVER, GOOGLE

    @Column(name="refresh_token")
    private String refreshToken; // 리프레시 토큰

    @Column
    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

    public User(String email, String password, UserRoleEnum role) {
        this.email = email;
        this.password = password;
        this.role = role;
        nickname = email.substring(0, email.indexOf('@'));
        introduction = "I'm " + nickname + " from ITWASSUMMER:D";
    }

    public User(String email, String password, UserRoleEnum role, String city, String gender) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.city = city;
        this.gender = gender;
        nickname = email.substring(0, email.indexOf('@'));
        introduction = "I'm " + nickname + " from ITWASSUMMER:D";
    }

    @Builder
    public User(String email, UserRoleEnum role, String nickname, String picture, String socialId, SocialType socialType) {
        this.email = email;
        this.role = role;
        this.nickname = nickname;
        this.picture = picture;
        this.socialId = socialId;
        this.socialType = socialType;
    }

    public void editUserInfo(String nickname, String introduction, String picture) {
        this.nickname = nickname;
        this.introduction = introduction;
        this.picture = picture;
    }

    // 소셜로그인 시 활용
    public User socialUpdate(String nickname, String picture){
        this.nickname = nickname;
        this.picture = picture;

        return this;
    }

    public String getRoleKey(){
        return this.role.getAuthority();
    }

    public void editPassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }
}
