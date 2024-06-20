package university.market.chat.mapper;

import org.apache.ibatis.annotations.Mapper;
import university.market.chat.domain.ChatVO;

@Mapper
public interface ChatMapper {
    void createChat(ChatVO chat);

    ChatVO getChat(Long chatId);

    void deleteChat(Long chatId);

    void updateChat(Long chatId, String title);

}
