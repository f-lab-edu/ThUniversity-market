package university.market.tag.service.dto.request;

import lombok.Builder;
import university.market.member.domain.MemberVO;

@Builder
public record TagMemberRequest(
        long tagId,
        MemberVO member
) {
}
