package university.market.chat.room.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import university.market.chat.room.domain.ChatMemberVO;
import university.market.chat.room.domain.ChatVO;
import university.market.chat.room.domain.chatauth.ChatAuthType;
import university.market.chat.room.exception.ChatException;
import university.market.chat.room.exception.ChatExceptionType;
import university.market.chat.room.mapper.ChatMapper;
import university.market.chat.room.mapper.ChatMemberMapper;
import university.market.chat.room.service.dto.ChatCreateRequest;
import university.market.item.service.ItemService;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.service.MemberService;
import university.market.member.utils.auth.PermissionCheck;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ItemService itemService;

    private final MemberService memberService;

    private final ChatMapper chatMapper;

    private final ChatMemberMapper chatMemberMapper;

    private final PermissionCheck permissionCheck;

    //    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    @Transactional
    public ChatVO createChat(ChatCreateRequest request, MemberVO currentMember) {
        ChatVO chat = ChatVO.builder()
                .title(request.title())
                .item(itemService.getItemById(request.itemId()))
                .build();

        chatMapper.createChat(chat);

        request.memberEmails()
                .forEach(
                        member -> {
                            MemberVO memberVO = memberService.findMemberByEmail(member);
                            permissionCheck.hasPermission(
                                    () -> memberVO.getAuth().equals(AuthType.ROLE_USER));
                            chatMemberMapper.addMember(ChatMemberVO.builder()
                                    .chatAuth(ChatAuthType.GUEST)
                                    .chat(chat)
                                    .member(memberVO)
                                    .build());
                        }
                );

        chatMemberMapper.addMember(ChatMemberVO.builder()
                .chatAuth(ChatAuthType.HOST)
                .chat(chat)
                .member(currentMember)
                .build());

        return chat;
    }

    //    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    public ChatVO getChat(Long chatId, MemberVO currentMember) {
        ChatMemberVO chatMember = chatMemberMapper.getChatMemberByChatAndMember(chatId,
                currentMember.getId());
        permissionCheck.hasPermission(() -> !chatMember.getMember().getId()
                .equals(currentMember.getId()));
        return chatMember.getChat();
    }

    //    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    public List<ChatVO> getChatsByMember(MemberVO currentMember) {
        return chatMemberMapper.getChatsByMember(currentMember.getId())
                .stream().map(
                        ChatMemberVO::getChat
                ).collect(Collectors.toList());
    }

    //    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    public List<MemberVO> getMembersByChat(Long chatId, MemberVO currentMember) {
        List<MemberVO> members = chatMemberMapper.getMembersByChat(chatId).stream().map(
                ChatMemberVO::getMember
        ).toList();
        permissionCheck.hasPermission(() -> members.stream().noneMatch(
                        member -> member.getId().equals(currentMember.getId())),
                new ChatException(ChatExceptionType.NOT_EXISTED_CHAT_MEMBER));

        return members;
    }

    //    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    public void deleteChat(Long chatId, MemberVO currentMember) {
        ChatMemberVO chatMember = chatMemberMapper.getChatMemberByChatAndMember(chatId, currentMember.getId());

        if (chatMember == null) {
            throw new ChatException(ChatExceptionType.NOT_EXISTED_CHAT_MEMBER);
        }

        permissionCheck.hasPermission(
                () -> !(chatMember.getChatAuth().equals(ChatAuthType.HOST)) || !(currentMember.getAuth()
                        == AuthType.ROLE_ADMIN));

        chatMapper.deleteChat(chatId);
    }

    //    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    public void addMember(Long chatId, String memberEmail, MemberVO currentMember) {
        List<MemberVO> members = chatMemberMapper.getMembersByChat(chatId).stream().map(
                ChatMemberVO::getMember
        ).toList();
        permissionCheck.hasPermission(() -> members.stream().anyMatch(
                        member -> member.getId().equals(currentMember.getId())),
                new ChatException(ChatExceptionType.NOT_EXISTED_CHAT_MEMBER));

        chatMemberMapper.addMember(ChatMemberVO.builder()
                .chat(chatMapper.getChat(chatId))
                .member(memberService.findMemberByEmail(memberEmail))
                .build());
    }

    //    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    public void removeMember(Long chatId, Long memberId, MemberVO currentMember) {
        permissionCheck.hasPermission(
                () -> (chatMemberMapper.getChatMemberByChatAndMember(chatId, currentMember.getId())
                        .getChatAuth()
                        != ChatAuthType.HOST) || currentMember.getAuth() != AuthType.ROLE_ADMIN);
        chatMemberMapper.deleteMember(chatId, memberId);
    }

    //    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    public void removeMyself(Long chatId, MemberVO currentMember) {
        chatMemberMapper.deleteMember(chatId, currentMember.getId());
    }

    //    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    public void updateChat(Long chatId, String title, MemberVO currentMember) {
        permissionCheck.hasPermission(
                () -> chatMemberMapper.getChatMemberByChatAndMember(chatId, currentMember.getId())
                        .getChatAuth()
                        != ChatAuthType.HOST);
        chatMapper.updateChat(chatId, title);
    }

    //    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    public ChatMemberVO getChatMember(Long chatId, Long memberId) {
        return chatMemberMapper.getChatMemberByChatAndMember(chatId, memberId);
    }
}
