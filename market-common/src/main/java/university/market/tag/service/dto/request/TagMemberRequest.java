package university.market.tag.service.dto.request;

import lombok.Builder;

@Builder
public record TagMemberRequest(
        long tagId
) {
}
