package com.ocho.what2do.common.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GoogleUserInfoDto {
    private String id;
    private String iss;
    private String azp;
    private String aud;
    private String sub;
    private String email;
    private String emailVerified;
    private String atHash;
    private String name;
    private String picture;
    private String givenName;
    private String familyName;
    private String locale;
    private String iat;
    private String exp;
    private String alg;
    private String kid;
    private String typ;
}
