package university.market.chat.message.socket;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import university.market.chat.message.domain.MessageVO;
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

@Slf4j
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

    private MemberVO seller;
    private MemberVO buyer;
    private ItemVO item;
    private ChatVO chat;

    private final String SEND_CHAT_ENDPOINT = "/pub/send/";
    private final String SUBSCRIBE_CHAT_ENDPOINT = "/sub/chat/";

    @Transactional
    @BeforeEach
    public void init() throws Exception {
        seller = MemberFixture.testMember(AuthType.ROLE_VERIFY_USER);
        buyer = MemberFixture.testMember(AuthType.ROLE_VERIFY_USER);

        memberMapper.joinMember(seller);
        memberMapper.joinMember(buyer);

        item = ItemFixture.testItem(seller);
        itemMapper.postItem(item);

        chat = ChatFixture.testIdChat(item);
        chatMapper.createChat(chat);

        ChatMemberVO chatMember1 = ChatMemberFixture.testChatMember(ChatAuthType.HOST, chat, seller);
        ChatMemberVO chatMember2 = ChatMemberFixture.testChatMember(ChatAuthType.GUEST, chat, buyer);
        chatMemberMapper.addMember(chatMember1);
        chatMemberMapper.addMember(chatMember2);
    }

    @Test
    public void testWebSocketConnection() throws Exception {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                log.info("Connected to WebSocket server");
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                log.error("Transport error", exception);
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                log.info("Received frame: {}", payload);
            }

            @Override
            public void handleException(StompSession session, StompCommand command, StompHeaders headers,
                                        byte[] payload, Throwable exception) {
                log.error("Handling exception", exception);
            }
        };

        StompHeaders connectHeaders = new StompHeaders();
        String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImFkbWluQGV4YW1wbGUuY29tIiwiZXhwIjoxNzIwMzUzMDQ2fQ._23k29xG880bcAuWgwNpmsftWwsA4uCRq3hHgvpJLG8";
        connectHeaders.add("Authorization", "Bearer " + jwtToken);
        StompSession session = stompClient.connect("ws://localhost:8080/connection", sessionHandler, connectHeaders)
                .get(1, TimeUnit.SECONDS);

        BlockingQueue<MessageVO> blockingQueue = new LinkedBlockingQueue<>();
        session.subscribe(SUBSCRIBE_CHAT_ENDPOINT + chat.getId(), new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return MessageVO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.offer((MessageVO) payload);
            }
        });

        MessageRequest messageRequest = new MessageRequest("Hello, World!");
        session.send(SEND_CHAT_ENDPOINT + chat.getId(), messageRequest);

        MessageVO message = blockingQueue.poll(5, TimeUnit.SECONDS);
        assertThat(message).isNotNull();
        assertThat(message.getContent()).isEqualTo("Hello, World!");
        assertThat(message.getSender().getId()).isEqualTo(buyer.getId());
    }
}
