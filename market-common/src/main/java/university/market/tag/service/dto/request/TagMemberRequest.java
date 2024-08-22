package university.market.tag.service.dto.request;

import university.market.member.domain.MemberVO;

public record TagMemberRequest(
        long tagId,
        MemberVO member
) {
}
