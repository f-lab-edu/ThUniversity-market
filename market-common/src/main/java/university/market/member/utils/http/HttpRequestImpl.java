package university.market.member.utils.http;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import university.market.member.domain.MemberVO;
import university.market.member.exception.MemberException;
import university.market.member.exception.MemberExceptionType;
import university.market.member.mapper.MemberMapper;
import university.market.member.utils.jwt.JwtTokenProvider;

@Component
@Slf4j
@RequiredArgsConstructor
public class HttpRequestImpl implements HttpRequest {

    private static final ThreadLocal<MemberVO> currentUser = new ThreadLocal<>();

    private final HttpServletRequest request;

    private final MemberMapper memberMapper;

    private final JwtTokenProvider jwtTokenProvider;

    public final static String prefixToken = "Bearer ";

    public final static String socketToken = "token";

    @Override
    public MemberVO getCurrentMember() {
        if (currentUser.get() != null) {
            return currentUser.get();
        }

        String token = getTokenFromRequest(request, null);
        MemberVO member = memberMapper.findMemberById(jwtTokenProvider.extractMemberId(token))
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        currentUser.set(member);
        return currentUser.get();
    }


    public static void clearCurrentUser() {
        currentUser.remove();
    }

    public MemberVO getCurrentMember(WebSocketSession session) {
        if (currentUser.get() != null) {
            return currentUser.get();
        }

        String token = getTokenFromRequest(null, session);
        MemberVO member = memberMapper.findMemberById(jwtTokenProvider.extractMemberId(token))
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        currentUser.set(member);
        return currentUser.get();
    }


    private String getTokenFromRequest(HttpServletRequest request, WebSocketSession session) {
        String token = null;
        if (request != null) {
            token = request.getHeader(HttpHeaders.AUTHORIZATION);
        }

        if (session != null) {
            token = String.valueOf(session.getAttributes().get(socketToken));
        }
        if (token == null || !token.startsWith(prefixToken)) {
            throw new MemberException(MemberExceptionType.INVALID_ACCESS_TOKEN);
        }

        String validToken = token.substring(7);
        jwtTokenProvider.validateToken(validToken);
        return validToken;
    }
}
