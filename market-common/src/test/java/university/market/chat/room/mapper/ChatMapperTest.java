package university.market.chat.room.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;
import university.market.chat.room.domain.ChatVO;
import university.market.helper.chat.room.ChatFixture;
import university.market.helper.item.ItemFixture;
import university.market.helper.member.MemberFixture;
import university.market.item.domain.ItemVO;
import university.market.item.mapper.ItemMapper;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.mapper.MemberMapper;

@MybatisTest
@AutoConfigureTestDatabase(replace = NONE)
public class ChatMapperTest {
    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private ItemMapper itemMapper;

    private ChatVO chat;

    @BeforeEach
    @Transactional
    public void init() {
        MemberVO seller = MemberFixture.testMember(AuthType.ROLE_VERIFY_USER);

        memberMapper.joinMember(seller);

        ItemVO item = ItemFixture.testItem(seller);

        itemMapper.postItem(item);

        chat = ChatFixture.testChat(item);
    }

    @Test
    @Transactional
    @DisplayName("[success] 채팅벙 생성")
    public void createChatTest_채팅방_생성() {
        chatMapper.createChat(chat);

        ChatVO findChat = chatMapper.getChat(chat.getId()).get();

        assertThat(findChat.getTitle()).isEqualTo(chat.getTitle());
    }

    @Test
    @Transactional
    @DisplayName("[success] 채팅방 삭제")
    public void deleteChat_채팅방_삭제() {
        chatMapper.createChat(chat);

        chatMapper.deleteChat(chat.getId());

        ChatVO findChat = chatMapper.getChat(chat.getId()).get();

        assertThat(findChat).isNull();
    }

    @Test
    @Transactional
    @DisplayName("[success] 채팅방 수정")
    public void updateChat_채팅방_수정() {
        chatMapper.createChat(chat);

        String newTitle = "newTitle";
        chatMapper.updateChat(chat.getId(), newTitle);

        ChatVO findChat = chatMapper.getChat(chat.getId()).get();

        assertThat(findChat.getTitle()).isEqualTo(newTitle);
    }
}
