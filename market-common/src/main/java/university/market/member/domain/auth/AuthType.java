package university.market.member.domain.auth;

public enum AuthType {
    ROLE_USER, ROLE_VERIFY_USER, ROLE_ADMIN;

    public static AuthType fromString(String name) {
        return name == null ? null : AuthType.valueOf(name);
    }
}
