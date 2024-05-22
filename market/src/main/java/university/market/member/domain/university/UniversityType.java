package university.market.member.domain.university;

public enum UniversityType {
    PUSAN, SEOUL, YONSEI, KOREA;

    public static UniversityType fromString(String name) {
        return name == null ? null : UniversityType.valueOf(name);
    }
}
