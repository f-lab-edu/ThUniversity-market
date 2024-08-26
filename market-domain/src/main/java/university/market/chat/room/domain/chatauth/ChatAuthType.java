package university.market.chat.room.domain.chatauth;

public enum ChatAuthType {
    GUEST,
    HOST;

    public static ChatAuthType fromValue(String value) {
        return switch (value.toUpperCase()) {
            case "GUEST" -> GUEST;
            case "HOST" -> HOST;
            default -> throw new IllegalArgumentException("없는 ChatAuthType 입니다. value = " + value);
        };
    }
}
