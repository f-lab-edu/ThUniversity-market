package university.market.item.service.dto.request;

import java.util.List;

public record PostItemRequest(
        String title,
        String description,
        boolean auction,
        int price,

        List<String> tags
) {
}
