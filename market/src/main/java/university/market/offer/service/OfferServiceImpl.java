package university.market.offer.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import university.market.item.domain.ItemVO;
import university.market.item.service.ItemService;
import university.market.member.annotation.AuthCheck;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.utils.auth.PermissionCheck;
import university.market.member.utils.http.HttpRequest;
import university.market.offer.domain.OfferStatus;
import university.market.offer.domain.OfferVO;
import university.market.offer.exception.OfferException;
import university.market.offer.exception.OfferExceptionType;
import university.market.offer.mapper.OfferMapper;
import university.market.offer.service.dto.request.OfferRequest;
import university.market.offer.service.dto.response.OfferResponse;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferMapper offerMapper;

    private final ItemService itemService;

    private final HttpRequest httpRequest;

    private final PermissionCheck permissionCheck;

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    @Transactional
    public void createOffer(OfferRequest offerRequest) {
        MemberVO member = httpRequest.getCurrentMember();
        ItemVO item = itemService.getItemById(offerRequest.itemId());

        permissionCheck.hasPermission(() -> member.equals(item.getSeller()),
                new OfferException(OfferExceptionType.NO_OFFER_MYSELF));

        OfferVO offer = OfferVO.builder()
                .item(itemService.getItemById(offerRequest.itemId()))
                .buyer(member)
                .price(offerRequest.price())
                .status(OfferStatus.OFFER)
                .build();

        offerMapper.createOffer(offer);
    }

    @Override
    @Transactional
    public void updateStatusOffer(Long offerId, String status) {
        MemberVO member = httpRequest.getCurrentMember();
        OfferVO offer = offerMapper.findOfferById(offerId);
        ItemVO item = itemService.getItemById(offer.getItem().getId());

        permissionCheck.hasPermission(() -> !member.equals(item.getSeller()));

        // offerStatus: ACCEPT, DECLINE
        offerMapper.updateStatusOffer(offerId, OfferStatus.fromValue(status));
    }

    @Override
    @Transactional
    public void updatePriceOffer(Long offerId, int price) {
        checkOfferBuyer(offerId);

        offerMapper.updatePriceOffer(offerId, price);
    }

    @Override
    @Transactional
    public void deleteOffer(Long offerId) {
        checkOfferBuyer(offerId);

        offerMapper.deleteOffer(offerId);
    }

    @Override
    @Transactional
    public List<OfferResponse> getOffersByItemId(Long itemId) {
        MemberVO member = httpRequest.getCurrentMember();

        permissionCheck.hasPermission(() -> !member.equals(itemService.getItemById(itemId).getSeller()));

        List<OfferVO> offers = offerMapper.getOffersByItemId(itemId);
        return offers.stream().map(
                OfferResponse::of
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<OfferResponse> getOffersByMemberId() {
        MemberVO member = httpRequest.getCurrentMember();
        List<OfferVO> offers = offerMapper.getOffersByMemberId(member.getId());
        
        return offers.stream().map(
                OfferResponse::of
        ).collect(Collectors.toList());
    }

    @Override
    public OfferResponse getOfferById(Long offerId) {
        OfferVO offer = offerMapper.findOfferById(offerId);
        return OfferResponse.of(offer);
    }

    private void checkOfferBuyer(Long offerId) {
        MemberVO member = httpRequest.getCurrentMember();
        OfferVO offer = offerMapper.findOfferById(offerId);

        permissionCheck.hasPermission(() -> !member.equals(offer.getBuyer()));
    }
}
