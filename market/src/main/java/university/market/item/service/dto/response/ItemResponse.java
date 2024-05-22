package university.market.item.service.dto.response;

import lombok.Builder;

@Builder
public record ItemResponse(
        Long itemId,
        String title,
        String description,
        String image_url,
        String status,
        boolean auction,
        int price
) {
}
