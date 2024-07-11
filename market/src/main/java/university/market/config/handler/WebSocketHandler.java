package university.market.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import university.market.chat.message.service.MessageService;
import university.market.chat.message.service.dto.request.MessageRequest;
import university.market.chat.room.service.ChatService;
import university.market.member.annotation.AuthCheck;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.domain.memberstatus.MemberStatus;
import university.market.member.service.MemberService;
import university.market.member.utils.http.HttpRequest;

@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final HttpRequest httpRequest;
    private final MessageService messageService;
    private final ChatService chatService;
    private static ConcurrentHashMap<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();

    private final MemberService memberService;

    @AuthCheck({AuthType.ROLE_ADMIN, AuthType.ROLE_VERIFY_USER})
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            sessions.put(httpRequest.getCurrentMember().getId(), session);
            memberService.updateMemberStatus(httpRequest.getCurrentMember().getId(), MemberStatus.ONLINE);
        } catch (Exception e) {
            throw new Exception("연결에 실패했습니다.");
        }
    }

    @AuthCheck({AuthType.ROLE_ADMIN, AuthType.ROLE_VERIFY_USER})
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String payload = message.getPayload();
            MessageRequest messageRequest = new ObjectMapper().readValue(payload, MessageRequest.class);
            messageService.sendMessage(messageRequest, httpRequest.getCurrentMember());

            broadcastMessage(messageRequest.chatId(), messageRequest);

        } catch (Exception e) {
            throw new Exception("메시지 전송에 실패했습니다.");
        }
    }

    private void broadcastMessage(Long chatId, MessageRequest messageRequest) {
        List<MemberVO> chatMembers = chatService.getMembersByChat(chatId);
        chatMembers.forEach(member -> {
            WebSocketSession session = sessions.get(member.getId());
            if (session != null && session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(messageRequest)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

//    @Override
//    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
//    }

    @AuthCheck({AuthType.ROLE_ADMIN, AuthType.ROLE_VERIFY_USER})
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(httpRequest.getCurrentMember().getId());
        memberService.updateMemberStatus(httpRequest.getCurrentMember().getId(), MemberStatus.OFFLINE);
        // to do: 종료 코드마다의 작업 정리
    }
}
