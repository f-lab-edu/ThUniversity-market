package university.market.socket;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import university.market.MarketSocketApplication;
import university.market.chat.message.mapper.MessageMapper;
import university.market.chat.message.service.dto.request.MessageRequest;
import university.market.chat.room.domain.ChatMemberVO;
import university.market.chat.room.domain.ChatVO;
import university.market.chat.room.domain.chatauth.ChatAuthType;
import university.market.chat.room.mapper.ChatMapper;
import university.market.chat.room.mapper.ChatMemberMapper;
import university.market.item.domain.ItemVO;
import university.market.item.mapper.ItemMapper;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.mapper.MemberMapper;
import university.market.member.utils.jwt.JwtTokenProvider;
import university.market.utils.random.RandomUtil;
import university.market.utils.test.helper.chat.room.ChatFixture;
import university.market.utils.test.helper.chat.room.ChatMemberFixture;
import university.market.utils.test.helper.item.ItemFixture;
import university.market.utils.test.helper.member.MemberFixture;

@Slf4j
@SpringBootTest(classes = MarketSocketApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
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
    private RandomUtil randomUtil;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private WebSocketHandler webSocketHandler;

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
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        String token = jwtTokenProvider.generateToken(buyer.getId());
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        StandardWebSocketClient client = new StandardWebSocketClient();

        try (WebSocketSession session = client.doHandshake(webSocketHandler, headers,
                new URI("ws://localhost:8081/ws/message")).get()) {
            String message = randomUtil.generateRandomCode('A', 'Z', 100);
            MessageRequest messageRequest = new MessageRequest(chat.getId(), message);
            String messageRequestString = new ObjectMapper().writeValueAsString(messageRequest);
            // Ensure the message is received// Wait for the message to be processed

            log.info("Sending message: {}", messageRequestString);
            log.info("session isOpen: {}", session.isOpen());
            session.sendMessage(new TextMessage(messageRequestString));

            Thread.sleep(1000);

            String receivedMessage = messageMapper.getMessagesByChat(chat.getId()).getFirst().getContent();
            assertThat(receivedMessage).isEqualTo(message);
        } catch (ExecutionException e) {
            log.error("Handshake failed due to: ", e.getCause());
        } catch (Exception e) {
            log.error("WebSocket handshake failed: ", e);
            throw e;
        }
    }
}
