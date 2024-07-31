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
import university.market.chat.message.exception.MessageException;
import university.market.chat.message.exception.MessageExceptionType;
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

    private final MessageService messageService;
    private final ChatService chatService;
    private final HttpRequest httpRequest;
    private static ConcurrentHashMap<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();

    private final MemberService memberService;

    @AuthCheck({AuthType.ROLE_ADMIN, AuthType.ROLE_VERIFY_USER})
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            MemberVO currentMember = httpRequest.getCurrentMember(session);
            sessions.put(currentMember.getId(), session);
            memberService.updateMemberStatus(currentMember.getId(), MemberStatus.ONLINE);
        } catch (Exception e) {
            throw new MessageException(MessageExceptionType.SOCKET_NOT_CONNECTED);
        }
    }

    @AuthCheck({AuthType.ROLE_ADMIN, AuthType.ROLE_VERIFY_USER})
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        MessageRequest request = new ObjectMapper().readValue(payload, MessageRequest.class);
        MemberVO currentMember = httpRequest.getCurrentMember(session);
        messageService.sendMessage(request.chatId(), request.content(), currentMember);

        broadcastMessage(request.chatId(), request, currentMember);
    }

    private void broadcastMessage(Long chatId, MessageRequest messageRequest, MemberVO currentMember) {
        List<MemberVO> chatMembers = chatService.getMembersByChat(chatId, currentMember);
        chatMembers.forEach(member -> {
            WebSocketSession session = sessions.get(member.getId());
            if (session != null && session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(messageRequest)));
                } catch (Exception e) {
                    throw new MessageException(MessageExceptionType.NOT_SEND_MESSAGE);
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
        MemberVO currentMember = httpRequest.getCurrentMember(session);
        memberService.updateMemberStatus(currentMember.getId(), MemberStatus.OFFLINE);
        sessions.remove(currentMember.getId());
        // to do: 종료 코드마다의 작업 정리
    }
}
