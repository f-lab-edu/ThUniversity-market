package university.market.chat.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import university.market.chat.domain.ChatVO;
import university.market.chat.service.ChatService;
import university.market.chat.service.dto.ChatCreateRequest;
import university.market.member.annotation.AuthCheck;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @PostMapping("/")
    public ResponseEntity<ChatVO> createChat(@RequestBody ChatCreateRequest request) {
        return ResponseEntity.ok(chatService.createChat(request));
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @GetMapping("/{chatId}")
    public ResponseEntity<ChatVO> getChat(@PathVariable Long chatId) {
        return ResponseEntity.ok(chatService.getChat(chatId));
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @GetMapping("/")
    public ResponseEntity<List<ChatVO>> getChatsByMember() {
        return ResponseEntity.ok(chatService.getChatsByMember());
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @GetMapping("/member/{chatId}")
    public ResponseEntity<List<MemberVO>> getMembersByChat(@PathVariable Long chatId) {
        return ResponseEntity.ok(chatService.getMembersByChat(chatId));
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @DeleteMapping("/{chatId}")
    public ResponseEntity<Void> deleteChat(@PathVariable Long chatId) {
        chatService.deleteChat(chatId);
        return ResponseEntity.ok().build();
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @PostMapping("/{chatId}")
    public ResponseEntity<Void> addMember(@PathVariable Long chatId, @RequestBody String memberEmail) {
        chatService.addMember(chatId, memberEmail);
        return ResponseEntity.ok().build();
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @DeleteMapping("/{chatId}/{memberId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long chatId, @PathVariable Long memberId) {
        chatService.removeMember(chatId, memberId);
        return ResponseEntity.ok().build();
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @DeleteMapping("/{chatId}/myself")
    public ResponseEntity<Void> removeMyself(@PathVariable Long chatId) {
        chatService.removeMyself(chatId);
        return ResponseEntity.ok().build();
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @PatchMapping("/{chatId}")
    public ResponseEntity<Void> updateChat(@PathVariable Long chatId, @RequestBody String title) {
        chatService.updateChat(chatId, title);
        return ResponseEntity.ok().build();
    }
}
