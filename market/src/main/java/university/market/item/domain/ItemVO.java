package university.market.item.domain;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import university.market.item.domain.status.StatusType;
import university.market.member.domain.MemberVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemVO {
    private Long id;

    private String title;

    private String description;

    private String imageUrl;

    private MemberVO seller;

    private StatusType status;

    private boolean auction;

    private int price;

    private boolean isDeleted;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    @Builder
    public ItemVO(String title, String description, String imageUrl, MemberVO seller, StatusType statusType,
                  boolean auction, int price) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.seller = seller;
        this.status = statusType;
        this.auction = auction;
        this.price = price;
    }
}
