package university.market.chat.room.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;
import university.market.chat.room.domain.ChatMemberVO;
import university.market.chat.room.domain.ChatVO;
import university.market.chat.room.domain.chatauth.ChatAuthType;
import university.market.chat.room.exception.ChatException;
import university.market.chat.room.exception.ChatExceptionType;
import university.market.helper.chat.room.ChatFixture;
import university.market.helper.chat.room.ChatMemberFixture;
import university.market.helper.item.ItemFixture;
import university.market.helper.member.MemberFixture;
import university.market.item.domain.ItemVO;
import university.market.item.mapper.ItemMapper;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.mapper.MemberMapper;

@MybatisTest
@AutoConfigureTestDatabase(replace = NONE)
public class ChatMemberMapperTest {
    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private ChatMemberMapper chatMemberMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private ItemMapper itemMapper;

    private ChatVO chat;

    private MemberVO seller;

    private MemberVO buyer;

    private ChatMemberVO chatMember1;

    private ChatMemberVO chatMember2;

    @BeforeEach
    public void init() {
        seller = MemberFixture.testMember(AuthType.ROLE_VERIFY_USER);
        memberMapper.joinMember(seller);

        ItemVO item = ItemFixture.testItem(seller);
        itemMapper.postItem(item);

        chat = ChatFixture.testChat(item);
        chatMapper.createChat(chat);

        buyer = MemberFixture.testMember(AuthType.ROLE_VERIFY_USER);
        memberMapper.joinMember(buyer);

        chatMember1 = ChatMemberFixture.testChatMember(ChatAuthType.HOST, chat, seller);
        chatMember2 = ChatMemberFixture.testChatMember(ChatAuthType.GUEST, chat, buyer);
    }

    @Test
    @Transactional
    @DisplayName("[success] 채팅 멤버 추가 및 조회")
    public void addMember_채팅_멤버_추가_getMembersByChat_조회() {
        chatMemberMapper.addMember(chatMember1);
        chatMemberMapper.addMember(chatMember2);

        List<ChatMemberVO> chatMembers = chatMemberMapper.getMembersByChat(chat.getId())
                .orElseThrow(() -> new ChatException(ChatExceptionType.NOT_EXISTED_CHAT_MEMBER));
        List<MemberVO> members = chatMembers.stream().map(ChatMemberVO::getMember).toList();

        assertThat(members.size()).isEqualTo(2);
        assertThat(members.getFirst().getName()).isEqualTo(chatMember1.getMember().getName());
        assertThat(members.getLast().getName()).isEqualTo(chatMember2.getMember().getName());
    }

    @Test
    @Transactional
    @DisplayName("[success] 채팅 멤버 삭제")
    public void deleteMember_채팅_멤버_삭제() {
        chatMemberMapper.addMember(chatMember1);

        chatMemberMapper.deleteMember(chat.getId(), chatMember1.getMember().getId());

        List<ChatMemberVO> chatMembers = chatMemberMapper.getMembersByChat(chat.getId())
                .orElseThrow(() -> new ChatException(ChatExceptionType.NOT_EXISTED_CHAT_MEMBER));
        List<MemberVO> members = chatMembers.stream().map(ChatMemberVO::getMember).toList();

        assertThat(members.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @DisplayName("[success] 멤버 채팅방 조회")
    public void getChatsByMember_멤버_채팅방_조회() {
        chatMemberMapper.addMember(chatMember1);
        chatMemberMapper.addMember(chatMember2);

        ItemVO item2 = ItemFixture.testItem(seller);
        itemMapper.postItem(item2);

        ChatVO chat2 = ChatFixture.testChat(item2);
        chatMapper.createChat(chat2);

        ChatMemberVO chatMember3 = ChatMemberFixture.testChatMember(ChatAuthType.HOST, chat2, seller);
        ChatMemberVO chatMember4 = ChatMemberFixture.testChatMember(ChatAuthType.GUEST, chat2, buyer);

        chatMemberMapper.addMember(chatMember3);
        chatMemberMapper.addMember(chatMember4);

        List<ChatMemberVO> chatMembers = chatMemberMapper.getChatsByMember(buyer.getId())
                .orElseThrow(() -> new ChatException(ChatExceptionType.NOT_EXISTED_CHAT_MEMBER));
        List<ChatVO> chats = chatMembers.stream().map(ChatMemberVO::getChat).toList();

        assertThat(chats.size()).isEqualTo(2);
        assertThat(chats.getFirst().getTitle()).isEqualTo(chat.getTitle());
        assertThat(chats.getLast().getTitle()).isEqualTo(chat2.getTitle());
    }

    @Test
    @Transactional
    @DisplayName("[success] 채팅 멤버 조회")
    public void getChatMemberByChatAndMember_채팅_멤버_조회() {
        chatMemberMapper.addMember(chatMember1);
        chatMemberMapper.addMember(chatMember2);

        ChatMemberVO chatMember = chatMemberMapper.getChatMemberByChatAndMember(chat.getId(), buyer.getId())
                .orElseThrow(() -> new ChatException(ChatExceptionType.NOT_EXISTED_CHAT_MEMBER));

        assertThat(chatMember.getMember().getName()).isEqualTo(buyer.getName());
    }
}
