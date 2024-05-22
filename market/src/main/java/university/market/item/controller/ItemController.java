package university.market.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import university.market.item.service.ItemService;
import university.market.item.service.dto.request.PostItemRequest;
import university.market.item.service.dto.request.UpdateItemRequest;
import university.market.item.service.dto.response.ItemResponse;

@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping("/")
    public ResponseEntity<Void> postItem(@RequestBody PostItemRequest postItemRequest) {
        itemService.postItem(postItemRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/")
    public ResponseEntity<Void> updateItem(@RequestBody UpdateItemRequest updateItemRequest) {
        itemService.updateItem(updateItemRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long itemId) {
        ItemResponse item = itemService.getItemById(itemId);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
