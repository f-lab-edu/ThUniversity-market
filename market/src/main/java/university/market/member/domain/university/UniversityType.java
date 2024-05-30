package university.market.member.domain.university;

public enum UniversityType {
    PUSAN(0),
    SEOUL(1),
    YONSEI(2),
    KOREA(3);
    private final int value;

    UniversityType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static UniversityType fromValue(int value) {
        for (UniversityType type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("없는 UniversityType 입니다. value = " + value);
    }
}
