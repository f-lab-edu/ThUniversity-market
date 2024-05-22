package university.market.item.domain;

import java.sql.Timestamp;
import university.market.item.domain.status.StatusType;
import university.market.member.domain.MemberVO;

public class ItemVO {
    private Long id;

    private String title;

    private String description;

    private String image_url;

    private MemberVO seller;

    private StatusType status;
    private boolean auction;
    private int price;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}
