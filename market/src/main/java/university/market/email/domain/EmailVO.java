package university.market.email.domain;

import java.sql.Timestamp;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailVO {

    private Long id;

    private String email;

    private String verificationCode;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    @Builder
    public EmailVO(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
    }
}
