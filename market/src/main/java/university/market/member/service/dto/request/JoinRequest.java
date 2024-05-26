package university.market.member.service.dto.request;

import lombok.Data;

public record JoinRequest (
    String name,
    String email,
    String password,
    String university
){}
