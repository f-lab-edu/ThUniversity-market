package university.market.chat.room.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import university.market.chat.room.domain.ChatVO;
import university.market.chat.room.service.ChatService;
import university.market.chat.room.service.dto.ChatCreateRequest;
import university.market.member.annotation.AuthCheck;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.utils.http.HttpRequest;

@Tag(name = "Chat", description = "채팅방 관련 API")
@Controller
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final HttpRequest httpRequest;

    @Operation(summary = "채팅방 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "클라이언트 에러", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json")),
    })
    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @PostMapping("/")
    public ResponseEntity<ChatVO> createChat(@RequestBody ChatCreateRequest request) {
        return ResponseEntity.ok(chatService.createChat(request, httpRequest.getCurrentMember()));
    }

    @Operation(summary = "채팅방 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "클라이언트 에러", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json")),
    })
    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @GetMapping("/{chatId}")
    public ResponseEntity<ChatVO> getChat(@PathVariable Long chatId) {
        return ResponseEntity.ok(chatService.getChat(chatId, httpRequest.getCurrentMember()));
    }

    @Operation(summary = "내가 속한 채팅방 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "클라이언트 에러", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json")),
    })
    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @GetMapping("/")
    public ResponseEntity<List<ChatVO>> getChatsByMember() {
        return ResponseEntity.ok(chatService.getChatsByMember(httpRequest.getCurrentMember()));
    }

    @Operation(summary = "채팅방의 멤버 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "클라이언트 에러", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json")),
    })
    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @GetMapping("/member/{chatId}")
    public ResponseEntity<List<MemberVO>> getMembersByChat(@PathVariable Long chatId) {
        return ResponseEntity.ok(chatService.getMembersByChat(chatId, httpRequest.getCurrentMember()));
    }

    @Operation(summary = "채팅방 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "클라이언트 에러", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json")),
    })
    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @DeleteMapping("/{chatId}")
    public ResponseEntity<Void> deleteChat(@PathVariable Long chatId) {
        chatService.deleteChat(chatId, httpRequest.getCurrentMember());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "채팅방 멤버 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "클라이언트 에러", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json")),
    })
    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @PostMapping("/{chatId}")
    public ResponseEntity<Void> addMember(@PathVariable Long chatId, @RequestBody long memberId) {
        chatService.addMember(chatId, memberId, httpRequest.getCurrentMember());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "채팅방 멤버 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "클라이언트 에러", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json")),
    })
    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @DeleteMapping("/{chatId}/{memberId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long chatId, @PathVariable Long memberId) {
        chatService.removeMember(chatId, memberId, httpRequest.getCurrentMember());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "채팅방에서 나가기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "클라이언트 에러", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json")),
    })
    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @DeleteMapping("/{chatId}/myself")
    public ResponseEntity<Void> removeMyself(@PathVariable Long chatId) {
        chatService.removeMyself(chatId, httpRequest.getCurrentMember());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "채팅방 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "클라이언트 에러", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json")),
    })
    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @PatchMapping("/{chatId}")
    public ResponseEntity<Void> updateChat(@PathVariable Long chatId, @RequestBody String title) {
        chatService.updateChat(chatId, title, httpRequest.getCurrentMember());
        return ResponseEntity.ok().build();
    }
}
