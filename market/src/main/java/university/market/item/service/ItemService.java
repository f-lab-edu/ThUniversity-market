package university.market.item.service;

import university.market.item.service.dto.request.PostItemRequest;
import university.market.item.service.dto.request.UpdateItemRequest;
import university.market.item.service.dto.response.ItemResponse;

public interface ItemService {
    public void postItem(PostItemRequest postItemRequest);

    public void updateItem(UpdateItemRequest updateItemRequest);

    public void deleteItem(Long id);

    public ItemResponse getItemById(Long id);
}
