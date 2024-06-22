package university.market.chat.service.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record ChatCreateRequest(
        String title,
        Long itemId,
        List<String> memberEmails
) {
}
