package university.market.chat.message.service;

import java.sql.Timestamp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import university.market.chat.message.domain.MessageVO;
import university.market.chat.message.mapper.MessageMapper;
import university.market.chat.message.service.dto.request.MessageRequest;
import university.market.chat.room.exception.ChatException;
import university.market.chat.room.exception.ChatExceptionType;
import university.market.chat.room.mapper.ChatMemberMapper;
import university.market.chat.room.service.ChatService;
import university.market.member.annotation.AuthCheck;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.utils.auth.PermissionCheck;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageMapper messageMapper;
    private final ChatMemberMapper chatMemberMapper;
    private final ChatService chatService;
    private final PermissionCheck permissionCheck;

    @AuthCheck({AuthType.ROLE_ADMIN, AuthType.ROLE_VERIFY_USER})
    @Override
    public void sendMessage(MessageRequest request, MemberVO member) {
        MessageVO message = MessageVO.builder()
                .content(request.content())
                .sender(member)
                .chat(chatService.getChat(request.chatId(), member))
                .build();

        messageMapper.sendMessage(message);
    }

    @AuthCheck({AuthType.ROLE_ADMIN, AuthType.ROLE_VERIFY_USER})
    @Override
    public List<MessageVO> getMessageByChat(Long chatId, MemberVO member) {
        permissionCheck.hasPermission(() -> chatService.getChatMember(chatId, member.getId()) != null,
                new ChatException(ChatExceptionType.NOT_EXISTED_CHAT_MEMBER));

        chatMemberMapper.updateLastReadAt(chatId, member.getId(), new Timestamp(System.currentTimeMillis()));
        return messageMapper.getMessagesByChat(chatId);
    }
}
