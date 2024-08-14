package university.market.helper.fixture;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import university.market.item.domain.ItemVO;
import university.market.member.domain.MemberVO;
import university.market.offer.domain.OfferStatus;
import university.market.offer.domain.OfferVO;
import university.market.utils.random.RandomUtil;
import university.market.utils.random.RandomUtilImpl;

public class OfferFixture {

    private final static RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private OfferFixture() {
    }

    public static OfferVO testOffer(MemberVO buyer, ItemVO item, int price, OfferStatus status) {
        return OfferVO.builder()
                .price(price)
                .item(item)
                .buyer(buyer)
                .status(status)
                .build();
    }

    public static OfferVO testIdOffer(MemberVO buyer, ItemVO item, int price, OfferStatus status) {
        return new OfferVO(
                Long.parseLong(randomUtil.generateRandomCode('0', '9', 10)),
                item,
                buyer,
                price,
                status,
                false,
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        );
    }
}
