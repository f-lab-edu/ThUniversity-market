package university.market.member.service.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class LoginRequest {

    private String email;
    private String password;

}
