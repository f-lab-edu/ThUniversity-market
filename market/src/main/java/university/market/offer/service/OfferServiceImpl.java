package university.market.offer.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import university.market.item.service.ItemService;
import university.market.member.domain.MemberVO;
import university.market.member.exception.MemberException;
import university.market.member.exception.MemberExceptionType;
import university.market.member.service.MemberService;
import university.market.member.utils.jwt.JwtTokenProvider;
import university.market.offer.domain.OfferVO;
import university.market.offer.mapper.OfferMapper;
import university.market.offer.service.dto.request.OfferRequest;
import university.market.offer.service.dto.request.UpdateOfferRequest;
import university.market.offer.service.dto.response.OfferResponse;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferMapper offerMapper;

    private final ItemService itemService;

    private final MemberService memberService;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    private final HttpServletRequest httpServletRequest;

    @Override
    @Transactional
    public void createOffer(OfferRequest offerRequest) {
        MemberVO member = getMemberByHeader();
        OfferVO offer = OfferVO.builder()
                .item(itemService.getItemById(offerRequest.itemId()))
                .buyer(member)
                .price(offerRequest.price())
                .build();

        offerMapper.createOffer(offer);
    }

    @Override
    @Transactional
    public void acceptOffer(Long offerId) {
        offerMapper.acceptOffer(offerId);
    }

    @Override
    @Transactional
    public void updateOffer(UpdateOfferRequest updatedOfferRequest) {
        offerMapper.updateOffer(updatedOfferRequest.offerId(), updatedOfferRequest.price());
    }

    @Override
    @Transactional
    public void deleteOffer(Long offerId) {
        offerMapper.deleteOffer(offerId);
    }

    @Override
    public List<OfferResponse> getOffersByItemId(Long itemId) {
        List<OfferVO> offers = offerMapper.getOffersByItemId(itemId);
        return offers.stream().map(
                OfferResponse::of
        ).collect(Collectors.toList());
    }

    @Override
    public List<OfferResponse> getOffersByMemberId(Long memberId) {
        List<OfferVO> offers = offerMapper.getOffersByMemberId(memberId);
        return offers.stream().map(
                OfferResponse::of
        ).collect(Collectors.toList());
    }

    @Override
    public OfferResponse getOfferById(Long offerId) {

        OfferVO offer = offerMapper.findOfferById(offerId);
        return OfferResponse.of(offer);
    }

    // 추후에 이 메세드를 ThreadLocal로 대체해 없앨 예정
    private MemberVO getMemberByHeader() {
        String token = httpServletRequest.getHeader("Authorization");
        if (token == null) {
            throw new MemberException(MemberExceptionType.INVALID_ACCESS_TOKEN);
        }
        String email = jwtTokenProvider.extractEmail(token.substring(7));
        return memberService.findMemberByEmail(email);
    }
}
