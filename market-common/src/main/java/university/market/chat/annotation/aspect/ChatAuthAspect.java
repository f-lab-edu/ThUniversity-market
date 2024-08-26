package university.market.chat.annotation.aspect;

import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import university.market.chat.annotation.ChatAuth;
import university.market.chat.room.exception.ChatException;
import university.market.chat.room.exception.ChatExceptionType;
import university.market.chat.room.mapper.ChatMemberMapper;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.utils.http.HttpRequest;
import university.market.utils.auth.PermissionCheck;

@Aspect
@Component
@RequiredArgsConstructor
public class ChatAuthAspect {

    private final HttpRequest httpRequest;
    private final PermissionCheck permissionCheck;
    private final ChatMemberMapper chatMemberMapper;

    @Before("@annotation(university.market.chat.annotation.ChatAuth)")
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

        permissionCheck.hasPermission(() -> chatMemberMapper.getChatMemberByChatAndMember(
                        chatId, currentUser.getId()) == null
                        && currentUser.getAuth() != AuthType.ROLE_ADMIN,
                new ChatException(ChatExceptionType.NOT_EXISTED_CHAT_MEMBER));
    }
}
