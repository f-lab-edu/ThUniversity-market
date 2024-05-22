package university.market.item.service.dto.request;

public record PostItemRequest(
        String title,
        String description,
        String memberToken,
        boolean auction,
        int price

) {
}
