package university.market.member.utils.http;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import university.market.member.domain.MemberVO;
import university.market.member.exception.MemberException;
import university.market.member.exception.MemberExceptionType;
import university.market.member.mapper.MemberMapper;
import university.market.member.utils.jwt.JwtTokenProvider;

@Component
public class HttpRequestImpl implements HttpRequest {

    private static final ThreadLocal<MemberVO> currentUser = new ThreadLocal<>();

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public MemberVO getCurrentMember() {
        if (currentUser.get() != null) {
            return currentUser.get();
        }

        String token = getTokenFromRequest(request, null);
        MemberVO member = memberMapper.findMemberByEmail(jwtTokenProvider.extractEmail(token));
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
        MemberVO member = memberMapper.findMemberByEmail(jwtTokenProvider.extractEmail(token));
        currentUser.set(member);
        return currentUser.get();
    }


    private String getTokenFromRequest(HttpServletRequest request, WebSocketSession session) {
        String token = null;
        if (request != null) {
            token = request.getHeader("Authorization");
        }

        if (session != null) {
            token = String.valueOf(session.getAttributes().get("token"));
        }
        if (token == null || !token.startsWith("Bearer ")) {
            throw new MemberException(MemberExceptionType.INVALID_ACCESS_TOKEN);
        }

        String validToken = token.substring(7);
        jwtTokenProvider.validateToken(validToken);
        return validToken;
    }
}
