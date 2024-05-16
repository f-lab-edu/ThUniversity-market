package university.market.member.domain;

import java.sql.Timestamp;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import university.market.member.domain.auth.AuthType;
import university.market.member.domain.university.UniversityType;

@Data
@NoArgsConstructor
public class MemberVO {

    private Long id;

    private String name;

    private String email;

    private String password;

    private UniversityType university;

    private AuthType auth;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    @Builder
    public MemberVO(String name, String email, String password, String university) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.university = UniversityType.valueOf(university.toUpperCase());
        this.auth = AuthType.ROLE_USER;
    }
}
