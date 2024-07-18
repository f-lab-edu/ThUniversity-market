package university.market.chat.message.socket;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import university.market.chat.message.service.dto.request.MessageRequest;
import university.market.chat.room.domain.ChatMemberVO;
import university.market.chat.room.domain.ChatVO;
import university.market.chat.room.domain.chatauth.ChatAuthType;
import university.market.chat.room.mapper.ChatMapper;
import university.market.chat.room.mapper.ChatMemberMapper;
import university.market.helper.fixture.ChatFixture;
import university.market.helper.fixture.ChatMemberFixture;
import university.market.helper.fixture.ItemFixture;
import university.market.helper.fixture.MemberFixture;
import university.market.item.domain.ItemVO;
import university.market.item.mapper.ItemMapper;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.mapper.MemberMapper;
import university.market.member.utils.jwt.JwtTokenProvider;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class SocketConnectionTest {

    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ChatMemberMapper chatMemberMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private MemberVO seller;
    private MemberVO buyer;
    private ItemVO item;
    private ChatVO chat;

    @BeforeEach
    public void init() throws Exception {
        seller = MemberFixture.testMember(AuthType.ROLE_VERIFY_USER);
        buyer = MemberFixture.testMember(AuthType.ROLE_VERIFY_USER);

        memberMapper.joinMember(seller);
        memberMapper.joinMember(buyer);

        item = ItemFixture.testItem(seller);
        itemMapper.postItem(item);

        chat = ChatFixture.testChat(item);
        chatMapper.createChat(chat);

        ChatMemberVO chatMember1 = ChatMemberFixture.testChatMember(ChatAuthType.HOST, chat, seller);
        ChatMemberVO chatMember2 = ChatMemberFixture.testChatMember(ChatAuthType.GUEST, chat, buyer);
        chatMemberMapper.addMember(chatMember1);
        chatMemberMapper.addMember(chatMember2);
    }

    @Test
    public void testWebSocketConnection() throws Exception {
        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();
        StandardWebSocketClient client = new StandardWebSocketClient();
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        String token = jwtTokenProvider.generateToken(buyer.getEmail());
        headers.add("Authorization", "Bearer " + token);

        client.doHandshake(new TextWebSocketHandler() {
            @Override
            public void afterConnectionEstablished(org.springframework.web.socket.WebSocketSession session)
                    throws Exception {
                session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(
                        new MessageRequest(chat.getId(), "Hello, WebSocket!"))));
            }

            @Override
            protected void handleTextMessage(org.springframework.web.socket.WebSocketSession session,
                                             TextMessage message) throws Exception {
                blockingQueue.offer(message.getPayload());
            }
        }, headers, URI.create("ws://localhost:8080/ws/message")).get();

        String receivedMessage = blockingQueue.poll(5, TimeUnit.SECONDS);
        assertThat(receivedMessage).isEqualTo("{\"chatId\":" + chat.getId() + ",\"content\":\"Hello, WebSocket!\"}");
    }
}
