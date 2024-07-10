package university.market.chat.room.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import university.market.chat.room.domain.ChatMemberVO;

@Mapper
public interface ChatMemberMapper {

    List<ChatMemberVO> getChatsByMember(Long memberId);

    List<ChatMemberVO> getMembersByChat(Long chatId);

    void updateLastReadAt(Long chatId, Long memberId);

    ChatMemberVO getChatMemberByChatAndMember(Long chatId, Long memberId);

    void addMember(ChatMemberVO chatMember);

    void deleteMember(Long chatId, Long memberId);
}
