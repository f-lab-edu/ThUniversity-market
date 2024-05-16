package university.market.member.service.dto.request;

import lombok.Data;

@Data
public class JoinRequest {

    private String name;
    private String email;
    private String password;
    private String university;

}
