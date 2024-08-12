package university.market.offer.service.dto.request;

import lombok.Builder;

@Builder
public record OfferRequest(
        Long itemId,
        int price
) {
}
