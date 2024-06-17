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
import university.market.member.annotation.AuthCheck;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.utils.auth.PermissionCheck;
import university.market.member.utils.http.HttpRequest;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;
    private final HttpRequest httpRequest;
    private final PermissionCheck permissionCheck;

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Transactional
    @Override
    public ItemVO postItem(PostItemRequest postItemRequest) {
        final MemberVO member = httpRequest.getCurrentMember();
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
        return itemVO;
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Transactional
    @Override
    public void updateItem(UpdateItemRequest updateItemRequest) {
        final MemberVO member = httpRequest.getCurrentMember();
        final ItemVO item = itemMapper.getItemById(updateItemRequest.itemId());

        if (item == null) {
            throw new ItemException(ItemExceptionType.INVALID_ITEM);
        }

        permissionCheck.hasPermission(() -> item.getSeller() != member && member.getAuth() != AuthType.ROLE_ADMIN);

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

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Transactional
    @Override
    public void deleteItem(Long itemId) {
        final MemberVO member = httpRequest.getCurrentMember();
        final ItemVO item = itemMapper.getItemById(itemId);

        if (item == null) {
            throw new ItemException(ItemExceptionType.INVALID_ITEM);
        }

        permissionCheck.hasPermission(() -> item.getSeller() != member && member.getAuth() != AuthType.ROLE_ADMIN);

        itemMapper.deleteItem(itemId);
    }

    @AuthCheck({AuthType.ROLE_USER, AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Transactional
    @Override
    public ItemVO getItemById(Long id) {
        return itemMapper.getItemById(id);
    }
}
