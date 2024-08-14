package university.market.item.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import university.market.item.domain.ItemVO;
import university.market.item.domain.status.StatusType;
import university.market.item.mapper.ItemMapper;
import university.market.item.service.dto.request.PostItemRequest;
import university.market.item.service.dto.request.UpdateItemRequest;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.exception.MemberException;
import university.market.member.exception.MemberExceptionType;
import university.market.utils.auth.PermissionCheck;
import university.market.utils.test.helper.item.ItemFixture;
import university.market.utils.test.helper.member.MemberFixture;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    private ItemMapper itemMapper;

    @Mock
    private PermissionCheck permissionCheck;

    @InjectMocks
    private ItemServiceImpl itemService;

    private ItemVO mockItem;

    private ItemVO mockItem2;

    private MemberVO mockMember;

    private UpdateItemRequest updateItemRequest;

    @BeforeEach
    public void init() {
        // given
        mockMember = MemberFixture.testIdMember(AuthType.ROLE_VERIFY_USER);

        mockItem = ItemFixture.testIdItem(mockMember);

        mockItem2 = ItemFixture.testItem(mockMember);

        updateItemRequest = new UpdateItemRequest(
                mockItem.getId(),
                mockItem2.getTitle(),
                mockItem2.getDescription(),
                mockItem2.getStatus().name(),
                mockItem2.isAuction(),
                mockItem2.getPrice()
        );
    }

    @Test
    @Transactional
    @DisplayName("[success] item 등록 성공")
    public void item_등록_성공() {
        // given
        PostItemRequest postItemRequest = new PostItemRequest(
                mockItem.getTitle(),
                mockItem.getDescription(),
                mockItem.isAuction(),
                mockItem.getPrice()
        );

        // when
        itemService.postItem(postItemRequest, mockMember);

        // then
        verify(itemMapper).postItem(any(ItemVO.class));
    }

    @Test
    @DisplayName("[success] item 업데이트 성공")
    public void item_업데이트_성공() {
        // mocking
        doNothing().when(itemMapper).updateItem(updateItemRequest.itemId(), mockItem2);
        when(itemMapper.getItemById(updateItemRequest.itemId())).thenReturn(mockItem2);

        // when
        itemService.updateItem(updateItemRequest, mockMember);
        ItemVO updatedItem = itemMapper.getItemById(updateItemRequest.itemId());

        // then
        assertThat(updatedItem.getTitle()).isEqualTo(updateItemRequest.title());
        assertThat(updatedItem.getDescription()).isEqualTo(updateItemRequest.description());
        assertThat(updatedItem.getStatus()).isEqualTo(StatusType.valueOf(updateItemRequest.status()));
        assertThat(updatedItem.isAuction()).isEqualTo(updateItemRequest.auction());
        assertThat(updatedItem.getPrice()).isEqualTo(updateItemRequest.price());
    }

    @Test
    @DisplayName("[fail] item 업데이트 실패 - 권한 없음")
    public void item_업데이트_실패_권한_없음() {
        MemberVO testedMember = MemberFixture.testIdMember(AuthType.ROLE_USER);

        // mocking
        when(itemMapper.getItemById(updateItemRequest.itemId())).thenReturn(mockItem2);

        doThrow(new MemberException(MemberExceptionType.UNAUTHORIZED_PERMISSION)).when(permissionCheck)
                .hasPermission(any());

        // when
        MemberException exception = assertThrows(MemberException.class, () -> {
            itemService.updateItem(updateItemRequest, testedMember);
        });

        // then
        assertThat(exception.exceptionType()).isEqualTo(MemberExceptionType.UNAUTHORIZED_PERMISSION);
    }


    @Test
    @DisplayName("[success] item 삭제 성공")
    public void item_삭제_성공() {
        // given
        Long itemId = mockItem.getId();

        // mocking
        when(itemMapper.getItemById(itemId)).thenReturn(mockItem);
        doNothing().when(itemMapper).deleteItem(itemId);

        // when
        itemService.deleteItem(itemId, mockMember);

        // then
        verify(itemMapper).deleteItem(itemId);
    }

    @Test
    @DisplayName("[fail] item 삭제 실패 권한 없음")
    public void item_삭제_실패_권한_없음() {
        // given
        MemberVO testedMember = MemberFixture.testIdMember(AuthType.ROLE_USER);
        Long itemId = mockItem.getId();

        // mocking
        when(itemMapper.getItemById(itemId)).thenReturn(mockItem);
        doThrow(new MemberException(MemberExceptionType.UNAUTHORIZED_PERMISSION)).when(permissionCheck)
                .hasPermission(any());

        // when
        MemberException exception = assertThrows(MemberException.class, () -> {
            itemService.deleteItem(itemId, testedMember);
        });

        // then
        assertThat(exception.exceptionType()).isEqualTo(MemberExceptionType.UNAUTHORIZED_PERMISSION);
    }

    @Test
    @DisplayName("[success] admin item 삭제 성공")
    public void admin_item_삭제_성공() {
        // given
        MemberVO adminMember = MemberFixture.testIdMember(AuthType.ROLE_ADMIN);
        Long itemId = mockItem.getId();

        // mocking
        when(itemMapper.getItemById(itemId)).thenReturn(mockItem);
        doNothing().when(itemMapper).deleteItem(itemId);

        // when
        itemService.deleteItem(itemId, adminMember);

        // then
        verify(itemMapper).deleteItem(itemId);
    }

    @Test
    @DisplayName("[success] item 조회 성공")
    public void item_조회_성공() {
        // given
        Long itemId = mockItem.getId();

        // mocking
        when(itemMapper.getItemById(itemId)).thenReturn(mockItem);

        // when
        ItemVO item = itemService.getItemById(itemId);

        // then
        verify(itemMapper).getItemById(itemId);

        assertThat(item.getId()).isEqualTo(itemId);
        assertThat(item.getTitle()).isEqualTo(mockItem.getTitle());
        assertThat(item.getDescription()).isEqualTo(mockItem.getDescription());
        assertThat(item.getImageUrl()).isEqualTo(mockItem.getImageUrl());
        assertThat(item.getStatus()).isEqualTo(mockItem.getStatus());
    }
}
