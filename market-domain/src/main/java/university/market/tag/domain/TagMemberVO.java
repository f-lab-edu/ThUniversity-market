package university.market.tag.domain;

import java.sql.Timestamp;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import university.market.member.domain.MemberVO;

@Data
@NoArgsConstructor
public class TagMemberVO {
    long id;

    TagVO tag;

    MemberVO member;

    Timestamp createdAt;

    @Builder
    public TagMemberVO(TagVO tag, MemberVO member) {
        this.tag = tag;
        this.member = member;
    }
}
