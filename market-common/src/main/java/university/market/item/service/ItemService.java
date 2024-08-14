package university.market.item.service;

import university.market.item.domain.ItemVO;
import university.market.item.service.dto.request.PostItemRequest;
import university.market.item.service.dto.request.UpdateItemRequest;
import university.market.member.domain.MemberVO;

public interface ItemService {
    ItemVO postItem(PostItemRequest postItemRequest, MemberVO currentMember);

    void updateItem(UpdateItemRequest updateItemRequest, MemberVO currentMember);

    void deleteItem(Long id, MemberVO currentMember);

    ItemVO getItemById(Long id);
}
