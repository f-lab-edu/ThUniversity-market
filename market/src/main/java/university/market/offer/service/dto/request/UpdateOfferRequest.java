package university.market.offer.service.dto.request;

public record UpdateOfferRequest(
        Long offerId,
        int price
) {
}
