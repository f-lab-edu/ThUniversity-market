package university.market.chat.room.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
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
import university.market.chat.room.service.dto.ChatCreateRequest;
import university.market.item.domain.ItemVO;
import university.market.item.service.ItemService;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.exception.MemberException;
import university.market.member.exception.MemberExceptionType;
import university.market.member.service.MemberService;
import university.market.member.utils.http.HttpRequest;
import university.market.utils.auth.PermissionCheck;
import university.market.utils.test.helper.chat.room.ChatFixture;
import university.market.utils.test.helper.chat.room.ChatMemberFixture;
import university.market.utils.test.helper.item.ItemFixture;
import university.market.utils.test.helper.member.MemberFixture;

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
        chatMember = ChatMemberFixture.testIdChatMember(ChatAuthType.HOST, chat, member);
    }

    @Test
    @DisplayName("[success] 채팅방 생성 및 인원 추가")
    public void createChat_채팅방_생성_및_인원_추가() {
        // given
        MemberVO testedMember = MemberFixture.testIdMember(AuthType.ROLE_VERIFY_USER);

        // mocking
        when(itemService.getItemById(item.getId())).thenReturn(item);
        when(memberService.findMemberById(testedMember.getId())).thenReturn(testedMember);

        // when
        ChatVO chatVO = chatService.createChat(ChatCreateRequest.builder()
                .title(chat.getTitle())
                .itemId(item.getId())
                .memberIds(List.of(testedMember.getId()))
                .build(), member);

        // then
        assertThat(chatVO.getTitle()).isEqualTo(chat.getTitle());
    }

    @Test
    @DisplayName("[fail] 채팅방 생성 및 권한 없는 인원 추가")
    public void createChat_채팅방_생성_및_권한_없는_인원_추가() {
        // given
        MemberVO testedMember = MemberFixture.testIdMember(AuthType.ROLE_USER);
        MemberVO testedMember2 = MemberFixture.testIdMember(AuthType.ROLE_USER);
        List<Long> memberIds = List.of(testedMember.getId(), testedMember2.getId());

        // mocking
        when(itemService.getItemById(item.getId())).thenReturn(item);
        doThrow(new MemberException(MemberExceptionType.UNAUTHORIZED_PERMISSION))
                .when(permissionCheck).hasPermission(any());
        // when
        BaseException exception = assertThrows(MemberException.class, () -> {
            chatService.createChat(ChatCreateRequest.builder()
                    .title(chat.getTitle())
                    .itemId(item.getId())
                    .memberIds(memberIds)
                    .build(), member);
        });

        // then
        assertThat(exception.exceptionType().errorCode()).isEqualTo(
                MemberExceptionType.UNAUTHORIZED_PERMISSION.errorCode());
    }

    @Test
    @DisplayName("[success] 채팅방 조회")
    public void getChat_채팅방_조회() {
        // mocking
        when(chatMemberMapper.getChatMemberByChatAndMember(chat.getId(), member.getId())).thenReturn(
                Optional.of(chatMember));

        // when
        ChatVO findChat = chatService.getChat(chat.getId(), member);

        // then
        assertThat(findChat.getTitle()).isEqualTo(chat.getTitle());
    }

    @Test
    @DisplayName("[fail] 채팅방 조회 권한 없음")
    public void getChat_채팅방_조회_권한_없음() {
        // given
        MemberVO testedMember = MemberFixture.testIdMember(AuthType.ROLE_VERIFY_USER);

        // mocking;
        when(chatMemberMapper.getChatMemberByChatAndMember(chat.getId(), testedMember.getId())).thenReturn(
                Optional.empty());

        // when & then
        assertThatThrownBy(() -> chatService.getChat(chat.getId(), testedMember))
                .isInstanceOf(ChatException.class)
                .hasMessage(ChatExceptionType.NOT_EXISTED_CHAT_MEMBER.errorMessage());
    }

    @Test
    @DisplayName("[fail] 채팅방 삭제 권한 없음")
    public void deleteChat_채팅방_삭제_권한_없음() {
        // mocking
        MemberVO testedMember = MemberFixture.testIdMember(AuthType.ROLE_VERIFY_USER);
        doThrow(new MemberException(MemberExceptionType.UNAUTHORIZED_PERMISSION)).when(permissionCheck)
                .hasPermission(any());

        // then
        assertThatThrownBy(() -> chatService.deleteChat(chat.getId(), testedMember))
                .isInstanceOf(MemberException.class)
                .hasMessage(MemberExceptionType.UNAUTHORIZED_PERMISSION.errorMessage());
    }

    @Test
    @DisplayName("[fail] 채팅방 멤버 삭제 권한 없음")
    public void removeMember_채팅방_멤버_삭제_권한_없음() {
        // mocking
        MemberVO testedMember = MemberFixture.testIdMember(AuthType.ROLE_VERIFY_USER);
        doThrow(new MemberException(MemberExceptionType.UNAUTHORIZED_PERMISSION)).when(permissionCheck)
                .hasPermission(any());

        // when
        BaseException exception = assertThrows(MemberException.class, () -> {
            chatService.removeMember(chat.getId(), member.getId(), testedMember);
        });

        // then
        assertThat(exception.exceptionType().errorCode()).isEqualTo(
                MemberExceptionType.UNAUTHORIZED_PERMISSION.errorCode());
    }


}
