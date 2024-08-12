package university.market.helper.dibs;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import university.market.dibs.domain.DibsVO;
import university.market.item.domain.ItemVO;
import university.market.member.domain.MemberVO;
import university.market.utils.random.RandomUtil;
import university.market.utils.random.RandomUtilImpl;

public class DibsFixture {
    public static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static DibsVO testDibs(MemberVO member, ItemVO item) {
        return DibsVO.builder()
                .member(member)
                .item(item)
                .build();
    }

    public static DibsVO testIdDibs(MemberVO member, ItemVO item) {
        return new DibsVO(
                Long.parseLong(randomUtil.generateRandomCode('0', '9', 16)),
                member,
                item,
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        );
    }
}
