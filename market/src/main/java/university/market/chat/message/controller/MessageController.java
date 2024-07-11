package university.market.chat.message.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import university.market.chat.message.domain.MessageVO;
import university.market.chat.message.service.MessageService;
import university.market.member.annotation.AuthCheck;
import university.market.member.domain.auth.AuthType;
import university.market.member.utils.http.HttpRequest;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final HttpRequest httpRequest;

    @AuthCheck({AuthType.ROLE_ADMIN, AuthType.ROLE_VERIFY_USER})
    @GetMapping("/{chatId}")
    public ResponseEntity<List<MessageVO>> getMessageByChat(@PathVariable Long chatId) {
        List<MessageVO> messages = messageService.getMessageByChat(chatId, httpRequest.getCurrentMember());
        return ResponseEntity.ok(messages);
    }
}
