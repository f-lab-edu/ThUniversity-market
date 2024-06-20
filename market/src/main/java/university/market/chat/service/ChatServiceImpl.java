package university.market.chat.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import university.market.chat.domain.ChatMemberVO;
import university.market.chat.domain.ChatVO;
import university.market.chat.domain.chatauth.ChatAuthType;
import university.market.chat.exception.ChatException;
import university.market.chat.exception.ChatExceptionType;
import university.market.chat.mapper.ChatMapper;
import university.market.chat.mapper.ChatMemberMapper;
import university.market.chat.service.dto.ChatCreateRequest;
import university.market.item.service.ItemService;
import university.market.member.annotation.AuthCheck;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.service.MemberService;
import university.market.member.utils.auth.PermissionCheck;
import university.market.member.utils.http.HttpRequest;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ItemService itemService;

    private final MemberService memberService;

    private final HttpRequest httpRequest;

    private final ChatMapper chatMapper;

    private final ChatMemberMapper chatMemberMapper;

    private final PermissionCheck permissionCheck;

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    @Transactional
    public ChatVO createChat(ChatCreateRequest request) {
        MemberVO currentMember = httpRequest.getCurrentMember();

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

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    public ChatVO getChat(Long chatId) {
        ChatMemberVO chatMember = chatMemberMapper.getChatMemberByChatAndMember(chatId,
                httpRequest.getCurrentMember().getId());

        permissionCheck.hasPermission(() -> !chatMember.getMember().getId()
                .equals(httpRequest.getCurrentMember().getId()));
        return chatMember.getChat();
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    public List<ChatVO> getChatsByMember() {
        return chatMemberMapper.getChatsByMember(httpRequest.getCurrentMember().getId())
                .stream().map(
                        ChatMemberVO::getChat
                ).collect(Collectors.toList());
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    public List<MemberVO> getMembersByChat(Long chatId) {
        List<MemberVO> members = chatMemberMapper.getMembersByChat(chatId).stream().map(
                ChatMemberVO::getMember
        ).toList();

        permissionCheck.hasPermission(() -> members.stream().anyMatch(
                        member -> member.getId().equals(httpRequest.getCurrentMember().getId())),
                new ChatException(ChatExceptionType.NOT_EXISTED_CHAT_MEMBER));

        return members;
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    public void deleteChat(Long chatId) {
        MemberVO member = httpRequest.getCurrentMember();
        ChatMemberVO chatMember = chatMemberMapper.getChatMemberByChatAndMember(chatId, member.getId());

        if (chatMember == null) {
            throw new ChatException(ChatExceptionType.NOT_EXISTED_CHAT_MEMBER);
        }

        permissionCheck.hasPermission(
                () -> !(chatMember.getChatAuth().equals(ChatAuthType.HOST)) || !(member.getAuth()
                        == AuthType.ROLE_ADMIN));

        chatMapper.deleteChat(chatId);
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    public void addMember(Long chatId, String memberEmail) {
        chatMemberMapper.addMember(ChatMemberVO.builder()
                .chat(chatMapper.getChat(chatId))
                .member(memberService.findMemberByEmail(memberEmail))
                .build());
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    public void removeMember(Long chatId, Long memberId) {
        permissionCheck.hasPermission(
                () -> chatMemberMapper.getChatMemberByChatAndMember(chatId, memberId).getChatAuth()
                        != ChatAuthType.HOST);

        chatMemberMapper.deleteMember(chatId, memberId);
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    public void removeMyself(Long chatId) {
        chatMemberMapper.deleteMember(chatId, httpRequest.getCurrentMember().getId());
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    public void updateChat(Long chatId, String title) {
        permissionCheck.hasPermission(
                () -> chatMemberMapper.getChatMemberByChatAndMember(chatId, httpRequest.getCurrentMember().getId())
                        .getChatAuth()
                        != ChatAuthType.HOST);
        chatMapper.updateChat(chatId, title);
    }
}
