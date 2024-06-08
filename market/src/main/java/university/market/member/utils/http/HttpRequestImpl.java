package university.market.member.utils.http;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import university.market.member.domain.MemberVO;
import university.market.member.exception.MemberException;
import university.market.member.exception.MemberExceptionType;
import university.market.member.mapper.MemberMapper;
import university.market.member.utils.jwt.JwtTokenProvider;

@Component
public class HttpRequestImpl implements HttpRequest {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public MemberVO getMemberSession() {
        HttpSession session = request.getSession(false);

        if (session == null) {
            session = createSession();
        }

        try {
            MemberVO currentUser = (MemberVO) session.getAttribute("currentUser");
            if (currentUser == null) {
                currentUser = (MemberVO) session.getAttribute("currentUser");
            }
            return currentUser;
        } catch (ClassCastException e) {
            throw new MemberException(MemberExceptionType.INVALID_CASTING);
        } catch (RuntimeException e) {
            throw new MemberException(MemberExceptionType.SESSION_ERROR);
        }
    }

    @Override
    public String getTokenFromRequest() {
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            throw new MemberException(MemberExceptionType.INVALID_ACCESS_TOKEN);
        }
        return token.substring(7);
    }

    private HttpSession createSession() {
        HttpSession session = request.getSession(true);  // Create a new session if not exists
        String token = getTokenFromRequest();
        String memberEmail = jwtTokenProvider.extractEmail(token);
        MemberVO member = memberMapper.findMemberByEmail(memberEmail);

        if (member == null) {
            throw new MemberException(MemberExceptionType.MEMBER_NOT_FOUND);
        }

        session.setAttribute("currentUser", member);
        return session;
    }
}
