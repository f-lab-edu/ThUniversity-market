package university.market.chat.message.service;

import java.util.List;
import university.market.chat.message.domain.MessageVO;
import university.market.chat.message.service.dto.request.MessageRequest;
import university.market.member.domain.MemberVO;

public interface MessageService {
    void sendMessage(MessageRequest request, MemberVO member);

    List<MessageVO> getMessageByChat(Long chatId, MemberVO member);
}
