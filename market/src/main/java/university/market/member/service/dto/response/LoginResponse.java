package university.market.member.service.dto.response;

import lombok.Builder;

@Builder
public record LoginResponse(
        Long memberId,
        String token
) {
}
