package university.market.item.controller;

import java.util.List;
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
import university.market.item.domain.ItemVO;
import university.market.item.service.ItemService;
import university.market.item.service.dto.request.PostItemRequest;
import university.market.item.service.dto.request.UpdateItemRequest;
import university.market.member.annotation.AuthCheck;
import university.market.member.domain.auth.AuthType;
import university.market.member.utils.http.HttpRequest;

@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final HttpRequest httpRequest;

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @PostMapping("/")
    public ResponseEntity<ItemVO> postItem(@RequestBody PostItemRequest postItemRequest) {
        ItemVO item = itemService.postItem(postItemRequest, httpRequest.getCurrentMember());
        return ResponseEntity.ok(item);
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @PatchMapping("/")
    public ResponseEntity<Void> updateItem(@RequestBody UpdateItemRequest updateItemRequest) {
        itemService.updateItem(updateItemRequest, httpRequest.getCurrentMember());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @AuthCheck({AuthType.ROLE_USER, AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemVO> getItemById(@PathVariable Long itemId) {
        ItemVO item = itemService.getItemById(itemId);
        return ResponseEntity.ok(item);
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId, httpRequest.getCurrentMember());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @GetMapping("/university/{universityType}")
    public ResponseEntity<List<ItemVO>> getItemsByUniversity(@PathVariable String universityType) {
        return ResponseEntity.ok(itemService.getItemsByUniversity(universityType));
    }
}
