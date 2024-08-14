package university.market.helper.fixture;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import university.market.chat.message.domain.MessageVO;
import university.market.chat.room.domain.ChatVO;
import university.market.member.domain.MemberVO;
import university.market.utils.random.RandomUtil;
import university.market.utils.random.RandomUtilImpl;

public class MessageFixture {
    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private MessageFixture() {
    }

    public static MessageVO testMessage(ChatVO chat, MemberVO member) {
        return MessageVO.builder()
                .chat(chat)
                .content(randomUtil.generateRandomCode('A', 'z', 300))
                .sender(member)
                .build();
    }

    public static MessageVO testIdMessage(ChatVO chat, MemberVO member) {
        return new MessageVO(
                Long.parseLong(randomUtil.generateRandomCode('0', '9', 10)),
                randomUtil.generateRandomCode('A', 'z', 300),
                member,
                chat,
                new Timestamp(System.currentTimeMillis())
        );
    }
}
