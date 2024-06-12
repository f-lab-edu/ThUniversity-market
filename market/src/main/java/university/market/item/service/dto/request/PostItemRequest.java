package university.market.item.service.dto.request;

public record PostItemRequest(
        String title,
        String description,
        boolean auction,
        int price

) {
}
