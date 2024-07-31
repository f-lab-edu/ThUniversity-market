package university.market.chat.room.annotation.aspect;

import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import university.market.chat.room.annotation.ChatAuth;
import university.market.chat.room.exception.ChatException;
import university.market.chat.room.exception.ChatExceptionType;
import university.market.chat.room.service.ChatService;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.utils.auth.PermissionCheck;
import university.market.member.utils.http.HttpRequest;

@Aspect
@Component
@RequiredArgsConstructor
public class ChatAuthAspect {

    private final HttpRequest httpRequest;
    private final PermissionCheck permissionCheck;
    private final ChatService chatService;

    @Before("@annotation(university.market.chat.room.annotation.ChatAuth)")
    public void checkAuth(JoinPoint joinPoint) {
        MemberVO currentUser;
        if (joinPoint.getArgs()[0] instanceof WebSocketSession) {
            WebSocketSession session = (WebSocketSession) joinPoint.getArgs()[0];
            currentUser = httpRequest.getCurrentMember(session);
        } else {
            currentUser = httpRequest.getCurrentMember();
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();

        Long chatId;
        if (method.getParameters()[0].isAnnotationPresent(ChatAuth.class)) {
            chatId = (Long) args[0];
        } else {
            chatId = null;
        }

        permissionCheck.hasPermission(() -> chatService.getChatMember(
                        chatId, currentUser.getId()) == null
                        && currentUser.getAuth() != AuthType.ROLE_ADMIN,
                new ChatException(ChatExceptionType.NOT_EXISTED_CHAT_MEMBER));
    }
}
