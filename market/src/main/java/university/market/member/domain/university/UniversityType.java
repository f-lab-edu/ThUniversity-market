package university.market.member.domain.university;

import lombok.Getter;

@Getter
public enum UniversityType {
    ADMIN(0),
    PUSAN(1),
    SEOUL(2),
    YONSEI(3),
    KOREA(4);
    
    private final int value;

    UniversityType(int value) {
        this.value = value;
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
