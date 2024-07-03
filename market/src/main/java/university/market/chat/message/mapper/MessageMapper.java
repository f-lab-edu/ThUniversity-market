package university.market.chat.message.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import university.market.chat.message.domain.MessageVO;

@Mapper
public interface MessageMapper {
    void sendMessage(MessageVO message);

    List<MessageVO> getMessagesByChat(Long chatId);
}
