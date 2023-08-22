package com.ocho.what2do.user.entity;

public enum UserRoleEnum {
    USER(Authority.USER),
    ADMIN(Authority.ADMIN),
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
