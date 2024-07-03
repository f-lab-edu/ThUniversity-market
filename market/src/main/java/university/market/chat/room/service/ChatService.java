package university.market.chat.room.service;

import java.util.List;
import university.market.chat.room.domain.ChatMemberVO;
import university.market.chat.room.domain.ChatVO;
import university.market.chat.room.service.dto.ChatCreateRequest;
import university.market.member.domain.MemberVO;

public interface ChatService {
    ChatVO createChat(ChatCreateRequest request);

    ChatVO getChat(Long chatId);

    List<ChatVO> getChatsByMember();

    List<MemberVO> getMembersByChat(Long chatId);

    void deleteChat(Long chatId);

    void addMember(Long chatId, String memberEmail);

    void removeMember(Long chatId, Long memberId);

    void removeMyself(Long chatId);

    void updateChat(Long chatId, String title);

    ChatMemberVO getChatMember(Long chatId, Long memberId);
}
