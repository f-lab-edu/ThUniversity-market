package university.market.chat.message.service.dto.request;

public record MessageRequest(
        Long chatId,
        String content
) {
}
