package university.market.chat.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import university.market.chat.annotation.aspect.ChatAuthAspect;
import university.market.chat.room.domain.ChatVO;
import university.market.chat.room.exception.ChatException;
import university.market.chat.room.exception.ChatExceptionType;
import university.market.helper.chat.room.ChatFixture;
import university.market.helper.item.ItemFixture;
import university.market.helper.member.MemberFixture;
import university.market.item.domain.ItemVO;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.utils.http.HttpRequest;
import university.market.utils.auth.PermissionCheck;

@ExtendWith(MockitoExtension.class)
public class ChatAuthTest {
    @Mock
    private HttpRequest httpRequest;

    @Mock
    private PermissionCheck permissionCheck;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private MethodSignature methodSignature;

    @Mock
    private Parameter parameter;

    @Mock
    private Method method;

    @InjectMocks
    private ChatAuthAspect chatAuthAspect;

    private MemberVO seller;

    private MemberVO buyer;

    private ItemVO item;

    private ChatVO chat;

    @BeforeEach
    public void init() {
        seller = MemberFixture.testIdMember(AuthType.ROLE_VERIFY_USER);
        buyer = MemberFixture.testIdMember(AuthType.ROLE_VERIFY_USER);

        item = ItemFixture.testIdItem(seller);

        chat = ChatFixture.testIdChat(item);
    }

    @Test
    @DisplayName("[success] 채팅방 소속 인원 통과")
    public void 채팅방_소속_인원_통과() {
        // given
        Parameter[] parameters = new Parameter[]{parameter};
        // mocking
        when(httpRequest.getCurrentMember()).thenReturn(buyer);
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.getArgs()).thenReturn(new Object[]{chat.getId()});
        when(method.getParameters()).thenReturn(parameters);
        when(parameter.isAnnotationPresent(ChatAuth.class)).thenReturn(true);
        doNothing().when(permissionCheck).hasPermission(any(), any());
        // when
        chatAuthAspect.checkAuth(joinPoint);

        // then
        verify(permissionCheck).hasPermission(any(), any());
    }

    @Test
    @DisplayName("[success] admin 유저 통과")
    public void admin_유저_통과() {
        // given
        MemberVO adminMember = MemberFixture.testIdMember(AuthType.ROLE_ADMIN);
        Parameter[] parameters = new Parameter[]{parameter};

        // mocking
        when(httpRequest.getCurrentMember()).thenReturn(adminMember);
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.getArgs()).thenReturn(new Object[]{chat.getId()});
        when(method.getParameters()).thenReturn(parameters);
        when(parameter.isAnnotationPresent(ChatAuth.class)).thenReturn(true);

        // when
        chatAuthAspect.checkAuth(joinPoint);

        // then
        verify(permissionCheck).hasPermission(any(), any());
    }

    @Test
    @DisplayName("[fail] 채팅방 미소속 인원 차단")
    public void 채팅방_미소속_인원_차단() {
        // given
        MemberVO member = MemberFixture.testIdMember(AuthType.ROLE_VERIFY_USER);
        Parameter[] parameters = new Parameter[]{parameter};

        // mocking
        when(httpRequest.getCurrentMember()).thenReturn(member);
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.getArgs()).thenReturn(new Object[]{chat.getId()});
        when(method.getParameters()).thenReturn(parameters);
        when(parameter.isAnnotationPresent(ChatAuth.class)).thenReturn(true);
        doThrow(new ChatException(ChatExceptionType.NOT_EXISTED_CHAT_MEMBER)).when(permissionCheck)
                .hasPermission(any(), any());

        // when
        Exception exception = null;
        try {
            chatAuthAspect.checkAuth(joinPoint);
        } catch (Exception e) {
            exception = e;
        }

        // then
        assertThat(exception.getMessage()).isEqualTo(ChatExceptionType.NOT_EXISTED_CHAT_MEMBER.errorMessage());
    }
}
