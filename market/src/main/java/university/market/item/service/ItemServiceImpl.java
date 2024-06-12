package university.market.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import university.market.item.domain.ItemVO;
import university.market.item.domain.status.StatusType;
import university.market.item.exception.ItemException;
import university.market.item.exception.ItemExceptionType;
import university.market.item.mapper.ItemMapper;
import university.market.item.service.dto.request.PostItemRequest;
import university.market.item.service.dto.request.UpdateItemRequest;
import university.market.item.service.dto.response.ItemResponse;
import university.market.member.domain.MemberVO;
import university.market.member.exception.MemberException;
import university.market.member.exception.MemberExceptionType;
import university.market.member.service.MemberService;
import university.market.member.utils.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public void postItem(PostItemRequest postItemRequest) {
        final MemberVO member = memberService.findMemberByEmail(
                jwtTokenProvider.extractEmail(postItemRequest.memberToken()));
        final ItemVO itemVO = ItemVO.builder()
                .title(postItemRequest.title())
                .description(postItemRequest.description())
                .imageUrl("blank")
                .seller(member)
                .statusType(StatusType.SELLING)
                .auction(postItemRequest.auction())
                .price(postItemRequest.price())
                .build();

        itemMapper.postItem(itemVO);
    }

    @Transactional
    @Override
    public void updateItem(UpdateItemRequest updateItemRequest) {
        final MemberVO member = memberService.findMemberByEmail(
                jwtTokenProvider.extractEmail(updateItemRequest.memberToken()));
        final ItemVO item = itemMapper.getItemById(updateItemRequest.itemId());

        if (item == null) {
            throw new ItemException(ItemExceptionType.INVALID_ITEM);
        }

        if (item.getSeller() != member) {
            throw new MemberException(MemberExceptionType.UNAUTHORIZED_PERMISSION);
        }

        final ItemVO updateItem = ItemVO.builder()
                .title(updateItemRequest.title())
                .description(updateItemRequest.description())
                .imageUrl("blank2")
                .seller(member)
                .statusType(StatusType.valueOf(updateItemRequest.status()))
                .auction(updateItemRequest.auction())
                .price(updateItemRequest.price())
                .build();

        itemMapper.updateItem(updateItemRequest.itemId(), updateItem);
    }

    @Transactional
    @Override
    public void deleteItem(Long id) {
        // 추후 권한에 따라 삭제 구현
        itemMapper.deleteItem(id);
    }

    @Transactional
    @Override
    public ItemResponse getItemById(Long id) {
        final ItemVO findItem = itemMapper.getItemById(id);
        return ItemResponse.builder()
                .itemId(id)
                .title(findItem.getTitle())
                .description(findItem.getDescription())
                .image_url(findItem.getImageUrl())
                .status(findItem.getStatus().name())
                .auction(findItem.isAuction())
                .price(findItem.getPrice())
                .build();
    }
}
