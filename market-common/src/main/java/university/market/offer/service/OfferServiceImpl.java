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
import university.market.offer.domain.OfferStatus;
import university.market.offer.domain.OfferVO;
import university.market.offer.exception.OfferException;
import university.market.offer.exception.OfferExceptionType;
import university.market.offer.mapper.OfferMapper;
import university.market.offer.service.dto.request.OfferRequest;
import university.market.offer.service.dto.response.OfferResponse;
import university.market.utils.auth.PermissionCheck;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferMapper offerMapper;

    private final ItemService itemService;

    private final PermissionCheck permissionCheck;

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    @Transactional
    public void createOffer(OfferRequest offerRequest, MemberVO currentMember) {
        ItemVO item = itemService.getItemById(offerRequest.itemId());

        permissionCheck.hasPermission(() -> currentMember.equals(item.getSeller()),
                new OfferException(OfferExceptionType.NO_OFFER_MYSELF));

        OfferVO offer = OfferVO.builder()
                .item(itemService.getItemById(offerRequest.itemId()))
                .buyer(currentMember)
                .price(offerRequest.price())
                .status(OfferStatus.OFFER)
                .build();

        offerMapper.createOffer(offer);
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    @Transactional
    public void updateStatusOffer(Long offerId, String status, MemberVO currentMember) {
        OfferVO offer = offerMapper.findOfferById(offerId);
        ItemVO item = itemService.getItemById(offer.getItem().getId());

        permissionCheck.hasPermission(() -> !currentMember.equals(item.getSeller()));

        // offerStatus: ACCEPT, DECLINE
        offerMapper.updateStatusOffer(offerId, OfferStatus.fromValue(status));
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    @Transactional
    public void updatePriceOffer(Long offerId, int price, MemberVO currentMember) {
        checkOfferBuyer(offerId, currentMember);

        offerMapper.updatePriceOffer(offerId, price);
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    @Transactional
    public void deleteOffer(Long offerId, MemberVO currentMember) {
        checkOfferBuyer(offerId, currentMember);

        offerMapper.deleteOffer(offerId);
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    @Transactional
    public List<OfferResponse> getOffersByItemId(Long itemId, MemberVO currentMember) {
        permissionCheck.hasPermission(() -> !currentMember.equals(itemService.getItemById(itemId).getSeller()));

        List<OfferVO> offers = offerMapper.getOffersByItemId(itemId);
        return offers.stream().map(
                OfferResponse::of
        ).collect(Collectors.toList());
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    @Transactional
    public List<OfferResponse> getOffersByMemberId(MemberVO currentMember) {
        List<OfferVO> offers = offerMapper.getOffersByMemberId(currentMember.getId());

        return offers.stream().map(
                OfferResponse::of
        ).collect(Collectors.toList());
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Override
    public OfferResponse getOfferById(Long offerId, MemberVO currentMember) {
        OfferVO offer = offerMapper.findOfferById(offerId);
        permissionCheck.hasPermission(() -> !currentMember.equals(offer.getBuyer()));
        return OfferResponse.of(offer);
    }

    private void checkOfferBuyer(Long offerId, MemberVO currentMember) {
        OfferVO offer = offerMapper.findOfferById(offerId);

        permissionCheck.hasPermission(() -> !currentMember.equals(offer.getBuyer()));
    }
}
