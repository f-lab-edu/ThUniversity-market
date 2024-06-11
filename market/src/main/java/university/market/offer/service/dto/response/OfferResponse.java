package university.market.offer.service.dto.response;

import lombok.Builder;
import university.market.offer.domain.OfferVO;

@Builder
public record OfferResponse(
        Long id,
        Long itemId,
        int price
) {
    public static OfferResponse of(OfferVO offer) {
        return OfferResponse.builder()
                        .id(offer.getId())
                        .itemId(offer.getItem().getId())
                        .price(offer.getPrice())
                        .build();
    }
}
