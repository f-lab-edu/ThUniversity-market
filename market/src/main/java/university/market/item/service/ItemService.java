package university.market.item.service;

import university.market.item.domain.ItemVO;
import university.market.item.service.dto.request.PostItemRequest;
import university.market.item.service.dto.request.UpdateItemRequest;
import university.market.item.service.dto.response.ItemResponse;

public interface ItemService {
    void postItem(PostItemRequest postItemRequest);

    void updateItem(UpdateItemRequest updateItemRequest);

    void deleteItem(Long id);

    ItemVO getItemById(Long id);
}
