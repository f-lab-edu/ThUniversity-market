package university.market.offer.service;

import java.util.List;
import university.market.offer.service.dto.request.OfferRequest;
import university.market.offer.service.dto.response.OfferResponse;

public interface OfferService {

    void createOffer(OfferRequest offerRequest);

    void updateStatusOffer(Long offerId, String status);

    void updatePriceOffer(Long offerId, int price);

    void deleteOffer(Long offerId);

    List<OfferResponse> getOffersByItemId(Long itemId);

    List<OfferResponse> getOffersByMemberId();

    OfferResponse getOfferById(Long id);
}
