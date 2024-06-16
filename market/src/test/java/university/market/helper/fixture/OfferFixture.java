package university.market.helper.fixture;

import java.security.NoSuchAlgorithmException;
import university.market.item.domain.ItemVO;
import university.market.member.domain.MemberVO;
import university.market.offer.domain.OfferVO;
import university.market.verify.email.utils.random.RandomUtil;
import university.market.verify.email.utils.random.RandomUtilImpl;

public class OfferFixture {

    private final static RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private OfferFixture() {}

    public static OfferVO testOffer(MemberVO buyer, ItemVO item) {
        return OfferVO.builder()
                .price(Integer.parseInt(randomUtil.generateRandomCode('0', '9', 6)))
                .item(item)
                .buyer(buyer)
                .build();
    }
}
