package university.market.item.service;

import university.market.item.domain.ItemVO;
import university.market.item.service.dto.request.PostItemRequest;
import university.market.item.service.dto.request.UpdateItemRequest;

public interface ItemService {
    public void postItem(PostItemRequest postItemRequest);

    public void updateItem(UpdateItemRequest updateItemRequest);

    public void deleteItem(Long id);

    public ItemVO getItemById(Long id);
}
