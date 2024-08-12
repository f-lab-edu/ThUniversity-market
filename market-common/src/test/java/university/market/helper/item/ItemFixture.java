package university.market.helper.item;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import university.market.item.domain.ItemVO;
import university.market.item.domain.status.StatusType;
import university.market.member.domain.MemberVO;
import university.market.verify.email.utils.random.RandomUtil;
import university.market.verify.email.utils.random.RandomUtilImpl;

public class ItemFixture {

    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private ItemFixture() {
    }

    public static ItemVO testItem(MemberVO member) {
        return ItemVO.builder()
                .title(randomUtil.generateRandomCode('0', 'z', 10))
                .description(randomUtil.generateRandomCode('0', 'z', 500))
                .seller(member)
                .imageUrl("blank2")
                .seller(member)
                .statusType(StatusType.SELLING)
                .auction(false)
                .price(Integer.parseInt(randomUtil.generateRandomCode('0', '9', 6)))
                .build();
    }

    public static ItemVO testIdItem(MemberVO member) {
        return new ItemVO(
                Long.parseLong(randomUtil.generateRandomCode('0', '9', 16)),
                randomUtil.generateRandomCode('0', 'z', 10),
                randomUtil.generateRandomCode('0', 'z', 500),
                "https:///" + randomUtil.generateRandomCode('0', 'z', 10) + ".com",
                member,
                StatusType.SELLING,
                false,
                Integer.parseInt(randomUtil.generateRandomCode('0', '9', 6)),
                false,
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        );
    }
}
