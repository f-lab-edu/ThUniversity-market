package university.market.offer.service;

import java.util.List;
import university.market.member.domain.MemberVO;
import university.market.offer.service.dto.request.OfferRequest;
import university.market.offer.service.dto.response.OfferResponse;

public interface OfferService {

    void createOffer(OfferRequest offerRequest, MemberVO currentMember);

    void updateStatusOffer(Long offerId, String status, MemberVO currentMember);

    void updatePriceOffer(Long offerId, int price, MemberVO currentMember);

    void deleteOffer(Long offerId, MemberVO currentMember);

    List<OfferResponse> getOffersByItemId(Long itemId, MemberVO currentMember);

    List<OfferResponse> getOffersByMemberId(MemberVO currentMember);

    OfferResponse getOfferById(Long id, MemberVO currentMember);
}
