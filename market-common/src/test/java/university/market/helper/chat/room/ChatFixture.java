package university.market.helper.chat.room;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import university.market.chat.room.domain.ChatVO;
import university.market.item.domain.ItemVO;
import university.market.verify.email.utils.random.RandomUtil;
import university.market.verify.email.utils.random.RandomUtilImpl;

public class ChatFixture {

    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private ChatFixture() {
    }

    public static ChatVO testChat(ItemVO item) {
        return ChatVO.builder()
                .title(randomUtil.generateRandomCode('A', 'z', 10))
                .item(item)
                .build();
    }

    public static ChatVO testIdChat(ItemVO item) {
        return new ChatVO(
                Long.parseLong(randomUtil.generateRandomCode('0', '9', 10)),
                randomUtil.generateRandomCode('A', 'z', 10),
                item,
                false,
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        );
    }
}
