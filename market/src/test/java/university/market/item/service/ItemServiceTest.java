package university.market.item.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
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
import university.market.helper.fixture.ItemFixture;
import university.market.helper.fixture.MemberFixture;
import university.market.item.domain.ItemVO;
import university.market.item.domain.status.StatusType;
import university.market.item.mapper.ItemMapper;
import university.market.item.service.dto.request.PostItemRequest;
import university.market.item.service.dto.request.UpdateItemRequest;
import university.market.item.service.dto.response.ItemResponse;
import university.market.member.domain.MemberVO;
import university.market.member.exception.MemberException;
import university.market.member.exception.MemberExceptionType;
import university.market.member.service.MemberServiceImpl;
import university.market.member.utils.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    private ItemMapper itemMapper;

    @Mock
    private MemberServiceImpl memberService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @InjectMocks
    private ItemServiceImpl itemService;

    private ItemVO mockItem;

    private MemberVO mockMember;

    @BeforeEach
    public void init() {
        // given
        mockMember = MemberFixture.testMember();
        mockItem = ItemFixture.testItem(mockMember);
    }

    @Test
    @Transactional
    @DisplayName("[success] item 등록 성공")
    public void item_등록_성공() {
        // given
        PostItemRequest postItemRequest = new PostItemRequest(
                "token",
                "title",
                "description",
                false,
                1000
        );

        // mocking
        when(jwtTokenProvider.extractEmail(postItemRequest.memberToken())).thenReturn(mockMember.getEmail());
        when(memberService.findMemberByEmail(mockMember.getEmail())).thenReturn(mockMember);

        // when
        itemService.postItem(postItemRequest);

        // then
        verify(itemMapper).postItem(any(ItemVO.class));
    }

    @Test
    @DisplayName("[success] item 업데이트 성공")
    public void item_업데이트_성공() {
        // given
        UpdateItemRequest updateItemRequest = new UpdateItemRequest(
                mockItem.getId(),
                "updated title",
                "updated description",
                "token",
                StatusType.SELLING.name(),
                false,
                2000
        );

        // mocking
        when(jwtTokenProvider.extractEmail(updateItemRequest.memberToken())).thenReturn(mockItem.getSeller().getEmail());
        when(memberService.findMemberByEmail(mockItem.getSeller().getEmail())).thenReturn(mockItem.getSeller());
        when(itemMapper.getItemById(updateItemRequest.itemId())).thenReturn(mockItem);

        // when
        itemService.updateItem(updateItemRequest);

        // then
        verify(itemMapper).updateItem(eq(updateItemRequest.itemId()), any(ItemVO.class));
    }

    @Test
    @DisplayName("[fail] item 업데이트 실패 - 권한 없음")
    public void item_업데이트_실패_권한_없음() {
        // given
        UpdateItemRequest updateItemRequest = new UpdateItemRequest(
                mockItem.getId(),
                "updated title",
                "updated description",
                "token",
                StatusType.SELLING.name(),
                false,
                2000
        );

        // mocking
        when(jwtTokenProvider.extractEmail(updateItemRequest.memberToken())).thenReturn(mockMember.getEmail());
        when(memberService.findMemberByEmail(mockMember.getEmail())).thenReturn(mockMember);
        when(itemMapper.getItemById(updateItemRequest.itemId())).thenReturn(mockItem);

        // when & then
        try {
            itemService.updateItem(updateItemRequest);
        } catch (MemberException e) {
            assert e.exceptionType() == MemberExceptionType.UNAUTHORIZED_PERMISSION;
        }
    }

    @Test
    @DisplayName("[success] item 삭제 성공")
    public void item_삭제_성공() {
        // given
        Long itemId = mockItem.getId();

        // mocking
        doNothing().when(itemMapper).deleteItem(itemId);

        // when
        itemService.deleteItem(itemId);

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
        ItemVO findedItem = itemService.getItemById(itemId);

        // then
        verify(itemMapper).getItemById(itemId);

        assert findedItem.getTitle().equals(mockItem.getTitle());
        assert findedItem.getDescription().equals(mockItem.getDescription());
        assert findedItem.getImageUrl().equals(mockItem.getImageUrl());
        assert findedItem.getStatus().equals(mockItem.getStatus());
        assert findedItem.isAuction() == mockItem.isAuction();
        assert findedItem.getPrice() == mockItem.getPrice();
    }
}
