package university.market.dibs.domain;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import university.market.item.domain.ItemVO;
import university.market.member.domain.MemberVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DibsVO {
    private Long id;

    private MemberVO member;

    private ItemVO item;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    @Builder
    public DibsVO(MemberVO member, ItemVO item) {
        this.member = member;
        this.item = item;
    }
}
