package com.ocho.what2do.user.entity;

public enum UserRoleEnum {
    // 일반 사용자
    USER(Authority.USER),
    // 관리자
    ADMIN(Authority.ADMIN),
    // 최초 소셜로그인 시 부여
    GUEST(Authority.GUEST);

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    private static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String GUEST = "ROLE_GUEST";
    }
}
