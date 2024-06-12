package university.market.item.service.dto.request;

public record UpdateItemRequest(
        Long itemId,
        String title,
        String description,
        String status,
        boolean auction,
        int price
) {
}
