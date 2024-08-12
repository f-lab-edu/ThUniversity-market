package university.market.member.utils.http;

import org.springframework.web.socket.WebSocketSession;
import university.market.member.domain.MemberVO;

public interface HttpRequest {
    MemberVO getCurrentMember();

    MemberVO getCurrentMember(WebSocketSession session);
}
