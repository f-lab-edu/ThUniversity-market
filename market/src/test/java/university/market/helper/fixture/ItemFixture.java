package university.market.helper.fixture;

import university.market.item.domain.ItemVO;
import university.market.item.domain.status.StatusType;
import university.market.verify.email.utils.random.RandomUtil;
import university.market.verify.email.utils.random.RandomUtilImpl;

public class ItemFixture {

    private static final RandomUtil randomUtil = new RandomUtilImpl();

    private ItemFixture() {}

    public static ItemVO testItem() {
        return ItemVO.builder()
                .title(randomUtil.generateRandomCode('0', 'z', 10))
                .description(randomUtil.generateRandomCode('0', 'z', 500))
                .imageUrl("https:///" + randomUtil.generateRandomCode('0', 'z', 10) + ".com")
                .seller(MemberFixture.testMember())
                .statusType(StatusType.SELLING)
                .auction(false)
                .price(Integer.parseInt(randomUtil.generateRandomCode('0', '9', 6)))
                .build();
    }
}
