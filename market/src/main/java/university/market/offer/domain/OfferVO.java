package university.market.offer.domain;

import java.sql.Timestamp;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import university.market.item.domain.ItemVO;
import university.market.member.domain.MemberVO;

@Data
@NoArgsConstructor
public class OfferVO {
    Long id;

    ItemVO item;

    MemberVO buyer;

    int price;

    boolean accepted;

    Timestamp createdAt;

    Timestamp updatedAt;

    @Builder
    public OfferVO(ItemVO item, MemberVO buyer, int price) {
        this.item = item;
        this.buyer = buyer;
        this.price = price;
        this.accepted = false;
    }
}
