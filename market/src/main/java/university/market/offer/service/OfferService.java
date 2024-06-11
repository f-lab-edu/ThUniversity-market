package university.market.offer.service;

import java.util.List;
import university.market.offer.service.dto.request.OfferRequest;
import university.market.offer.service.dto.request.UpdateOfferRequest;
import university.market.offer.service.dto.response.OfferResponse;

public interface OfferService {

    void createOffer(OfferRequest offerRequest);

    void acceptOffer(Long offerId);


    void updateOffer(UpdateOfferRequest updatedOfferRequest);

    void deleteOffer(Long offerId);

    List<OfferResponse> getOffersByItemId(Long itemId);

    List<OfferResponse> getOffersByMemberId(Long itemId);

    OfferResponse getOfferById(Long id);
}
