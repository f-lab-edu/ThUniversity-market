package university.market.member.service.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public record LoginRequest (
    String email,
    String password
){ }
