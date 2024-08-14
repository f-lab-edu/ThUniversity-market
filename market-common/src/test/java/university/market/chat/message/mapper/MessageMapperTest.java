package university.market.chat.message.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.sql.Timestamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import university.market.chat.message.domain.MessageVO;
import university.market.chat.room.domain.ChatMemberVO;
import university.market.chat.room.domain.ChatVO;
import university.market.chat.room.domain.chatauth.ChatAuthType;
import university.market.chat.room.exception.ChatException;
import university.market.chat.room.exception.ChatExceptionType;
import university.market.chat.room.mapper.ChatMapper;
import university.market.chat.room.mapper.ChatMemberMapper;
import university.market.item.domain.ItemVO;
import university.market.item.mapper.ItemMapper;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.mapper.MemberMapper;
import university.market.utils.test.helper.chat.message.MessageFixture;
import university.market.utils.test.helper.chat.room.ChatFixture;
import university.market.utils.test.helper.chat.room.ChatMemberFixture;
import university.market.utils.test.helper.item.ItemFixture;
import university.market.utils.test.helper.member.MemberFixture;

@MybatisTest
@AutoConfigureTestDatabase(replace = NONE)
public class MessageMapperTest {
    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private ChatMemberMapper chatMemberMapper;

    @Autowired
    private ItemMapper itemMapper;

    private ChatVO chat;

    private ItemVO item;

    private MemberVO seller;

    private MemberVO buyer;

    @BeforeEach
    public void init() {
        seller = MemberFixture.testMember(AuthType.ROLE_VERIFY_USER);
        buyer = MemberFixture.testMember(AuthType.ROLE_VERIFY_USER);
        memberMapper.joinMember(seller);
        memberMapper.joinMember(buyer);

        item = ItemFixture.testItem(seller);
        itemMapper.postItem(item);

        chat = ChatFixture.testChat(item);
        chatMapper.createChat(chat);

        ChatMemberVO chatMember = ChatMemberFixture.testChatMember(ChatAuthType.HOST, chat, seller);
        ChatMemberVO chatMember2 = ChatMemberFixture.testChatMember(ChatAuthType.GUEST, chat, buyer);
        chatMemberMapper.addMember(chatMember);
        chatMemberMapper.addMember(chatMember2);
    }

    @Test
    @DisplayName("[success] 메시지 전송 및 조회 성공")
    public void 메세지_전송_조회_성공() {
        // given
        MessageVO message = MessageFixture.testMessage(chat, buyer);

        // when
        messageMapper.sendMessage(message);

        // then
        assertThat(message.getId()).isNotNull();
    }

    @Test
    @DisplayName("[success] 메시지 조회 lastReadAt 업데이트 성공")
    public void lastReadAt_업데이트_성공() {
        //given
        ChatMemberVO chatMember = chatMemberMapper.getChatMemberByChatAndMember(chat.getId(), buyer.getId())
                .orElseThrow(() -> new ChatException(ChatExceptionType.NOT_EXISTED_CHAT_MEMBER));
        Timestamp beforeLastReadAt = chatMember.getLastReadAt();

        //when
        chatMemberMapper.updateLastReadAt(chat.getId(), buyer.getId(),
                new Timestamp(System.currentTimeMillis() + 1000));

        //then
        ChatMemberVO updatedChatMember = chatMemberMapper.getChatMemberByChatAndMember(chat.getId(), buyer.getId())
                .orElseThrow(() -> new ChatException(ChatExceptionType.NOT_EXISTED_CHAT_MEMBER));
        Timestamp updatedLastReadAt = updatedChatMember.getLastReadAt();
        assertThat(updatedLastReadAt).isNotEqualTo(beforeLastReadAt);
    }
}
