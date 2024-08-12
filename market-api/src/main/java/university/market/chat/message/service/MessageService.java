package university.market.chat.message.service;

import java.util.List;
import university.market.chat.message.domain.MessageVO;
import university.market.member.domain.MemberVO;

public interface MessageService {
    void sendMessage(Long chatId, String content, MemberVO member);

    List<MessageVO> getMessageByChat(Long chatId, MemberVO member);
}
