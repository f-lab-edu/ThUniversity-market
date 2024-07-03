package university.market.chat.room.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import university.market.base.exception.BaseException;
import university.market.chat.room.domain.ChatMemberVO;
import university.market.chat.room.domain.ChatVO;
import university.market.chat.room.domain.chatauth.ChatAuthType;
import university.market.chat.room.exception.ChatException;
import university.market.chat.room.exception.ChatExceptionType;
import university.market.chat.room.mapper.ChatMapper;
import university.market.chat.room.mapper.ChatMemberMapper;
import university.market.chat.room.service.ChatServiceImpl;
import university.market.chat.room.service.dto.ChatCreateRequest;
import university.market.helper.fixture.ChatFixture;
import university.market.helper.fixture.ChatMemberFixture;
import university.market.helper.fixture.ItemFixture;
import university.market.helper.fixture.MemberFixture;
import university.market.item.domain.ItemVO;
import university.market.item.service.ItemService;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.exception.MemberException;
import university.market.member.exception.MemberExceptionType;
import university.market.member.service.MemberService;
import university.market.member.utils.auth.PermissionCheck;
import university.market.member.utils.http.HttpRequest;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {
    @Mock
    private ItemService itemService;

    @Mock
    private MemberService memberService;

    @Mock
    private HttpRequest httpRequest;

    @Mock
    private ChatMapper chatMapper;

    @Mock
    private ChatMemberMapper chatMemberMapper;

    @Mock
    private PermissionCheck permissionCheck;

    @InjectMocks
    private ChatServiceImpl chatService;

    private MemberVO member;

    private ItemVO item;

    private ChatVO chat;

    private ChatMemberVO chatMember;

    @BeforeEach
    public void init() {
        member = MemberFixture.testIdMember(AuthType.ROLE_VERIFY_USER);
        item = ItemFixture.testIdItem(member);
        chat = ChatFixture.testIdChat(item);
        chatMember = ChatMemberFixture.testIdChatMember(ChatAuthType.GUEST, chat, member);
    }

    @Test
    @DisplayName("[success] 채팅방 생성 및 인원 추가")
    public void createChat_채팅방_생성_및_인원_추가() {
        // given
        MemberVO testedMember = MemberFixture.testIdMember(AuthType.ROLE_VERIFY_USER);

        // mocking
        when(httpRequest.getCurrentMember()).thenReturn(member);
        when(itemService.getItemById(item.getId())).thenReturn(item);
        when(memberService.findMemberByEmail(testedMember.getEmail())).thenReturn(testedMember);

        // when
        ChatVO chatVO = chatService.createChat(ChatCreateRequest.builder()
                .title(chat.getTitle())
                .itemId(item.getId())
                .memberEmails(List.of(testedMember.getEmail()))
                .build());

        // then
        assertThat(chatVO.getTitle()).isEqualTo(chat.getTitle());
    }

    @Test
    @DisplayName("[fail] 채팅방 생성 및 권한 없는 인원 추가")
    public void createChat_채팅방_생성_및_권한_없는_인원_추가() {
        // given
        MemberVO testedMember = MemberFixture.testIdMember(AuthType.ROLE_USER);
        MemberVO testedMember2 = MemberFixture.testIdMember(AuthType.ROLE_USER);
        List<String> memberEmails = List.of(testedMember.getEmail(), testedMember2.getEmail());

        // mocking
        when(httpRequest.getCurrentMember()).thenReturn(member);
        when(itemService.getItemById(item.getId())).thenReturn(item);
        doThrow(new MemberException(MemberExceptionType.UNAUTHORIZED_PERMISSION))
                .when(permissionCheck).hasPermission(any());
        // when
        BaseException exception = assertThrows(MemberException.class, () -> {
            chatService.createChat(ChatCreateRequest.builder()
                    .title(chat.getTitle())
                    .itemId(item.getId())
                    .memberEmails(memberEmails)
                    .build());
        });

        // then
        assertThat(exception.exceptionType().errorCode()).isEqualTo(
                MemberExceptionType.UNAUTHORIZED_PERMISSION.errorCode());
    }

    @Test
    @DisplayName("[success] 채팅방 조회")
    public void getChat_채팅방_조회() {
        // mocking
        when(httpRequest.getCurrentMember()).thenReturn(member);
        when(chatMemberMapper.getChatMemberByChatAndMember(chat.getId(), member.getId())).thenReturn(chatMember);

        // when
        ChatVO findChat = chatService.getChat(chat.getId());

        // then
        assertThat(findChat.getTitle()).isEqualTo(chat.getTitle());
    }

    @Test
    @DisplayName("[fail] 채팅방 조회 권한 없음")
    public void getChat_채팅방_조회_권한_없음() {
        // mocking
        when(httpRequest.getCurrentMember()).thenReturn(member);
        when(chatMemberMapper.getChatMemberByChatAndMember(chat.getId(), member.getId())).thenReturn(chatMember);
        doThrow(new MemberException(MemberExceptionType.UNAUTHORIZED_PERMISSION)).when(permissionCheck)
                .hasPermission(any());

        // when
        BaseException exception = assertThrows(MemberException.class, () -> {
            chatService.getChat(chat.getId());
        });

        // then
        assertThat(exception.exceptionType().errorCode()).isEqualTo(
                MemberExceptionType.UNAUTHORIZED_PERMISSION.errorCode());
    }

    @Test
    @DisplayName("[fail] 채팅방 소속 멤버 조회 실패")
    public void getMembersByChat_채팅방_소속_멤버_조회_실패() {
        // when
        doThrow(new ChatException(ChatExceptionType.NOT_EXISTED_CHAT_MEMBER))
                .when(permissionCheck)
                .hasPermission(any(), any());

        // when
        BaseException exception = assertThrows(ChatException.class, () -> {
            chatService.getMembersByChat(chat.getId());
        });

        // then
        assertThat(exception.exceptionType().errorCode()).isEqualTo(
                ChatExceptionType.NOT_EXISTED_CHAT_MEMBER.errorCode());
    }

    @Test
    @DisplayName("[fail] 채팅방 삭제 권한 없음")
    public void deleteChat_채팅방_삭제_권한_없음() {
        // mocking
        when(httpRequest.getCurrentMember()).thenReturn(member);
        when(chatMemberMapper.getChatMemberByChatAndMember(chat.getId(), member.getId())).thenReturn(chatMember);
        doThrow(new MemberException(MemberExceptionType.UNAUTHORIZED_PERMISSION)).when(permissionCheck)
                .hasPermission(any());

        // when
        BaseException exception = assertThrows(MemberException.class, () -> {
            chatService.deleteChat(chat.getId());
        });

        // then
        assertThat(exception.exceptionType().errorCode()).isEqualTo(
                MemberExceptionType.UNAUTHORIZED_PERMISSION.errorCode());
    }

    @Test
    @DisplayName("[fail] 채팅방 멤버 존재 여부 확인 실패")
    public void deleteChat_채팅방_멤버_존재_여부_확인_실패() {
        // mocking
        when(httpRequest.getCurrentMember()).thenReturn(member);
        when(chatMemberMapper.getChatMemberByChatAndMember(chat.getId(), member.getId())).thenReturn(null);

        // when
        BaseException exception = assertThrows(ChatException.class, () -> {
            chatService.deleteChat(chat.getId());
        });

        // then
        assertThat(exception.exceptionType().errorCode()).isEqualTo(
                ChatExceptionType.NOT_EXISTED_CHAT_MEMBER.errorCode());
    }

    @Test
    @DisplayName("[fail] 채팅방 멤버 삭제 권한 없음")
    public void removeMember_채팅방_멤버_삭제_권한_없음() {
        // mocking
        doThrow(new MemberException(MemberExceptionType.UNAUTHORIZED_PERMISSION)).when(permissionCheck)
                .hasPermission(any());

        // when
        BaseException exception = assertThrows(MemberException.class, () -> {
            chatService.removeMember(chat.getId(), member.getId());
        });

        // then
        assertThat(exception.exceptionType().errorCode()).isEqualTo(
                MemberExceptionType.UNAUTHORIZED_PERMISSION.errorCode());
    }
}
