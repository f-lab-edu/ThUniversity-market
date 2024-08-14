package university.market.offer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import university.market.helper.fixture.ItemFixture;
import university.market.helper.fixture.MemberFixture;
import university.market.helper.fixture.OfferFixture;
import university.market.item.domain.ItemVO;
import university.market.item.service.ItemService;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.exception.MemberException;
import university.market.member.exception.MemberExceptionType;
import university.market.offer.domain.OfferStatus;
import university.market.offer.domain.OfferVO;
import university.market.offer.exception.OfferException;
import university.market.offer.exception.OfferExceptionType;
import university.market.offer.mapper.OfferMapper;
import university.market.offer.service.dto.request.OfferRequest;
import university.market.offer.service.dto.response.OfferResponse;
import university.market.utils.auth.PermissionCheck;

@ExtendWith(MockitoExtension.class)
public class OfferServiceTest {

    @Mock
    private OfferMapper offerMapper;

    @Mock
    private ItemService itemService;

    @Mock
    private PermissionCheck permissionCheck;

    @InjectMocks
    private OfferServiceImpl offerService;

    private MemberVO member;

    private MemberVO seller;

    private ItemVO item;

    private OfferVO offer;

    @BeforeEach
    public void init() {
        member = MemberFixture.testIdMember(AuthType.ROLE_VERIFY_USER);
        seller = MemberFixture.testIdMember(AuthType.ROLE_VERIFY_USER);
        item = ItemFixture.testIdItem(seller);
        offer = OfferFixture.testIdOffer(member, item, 3000, OfferStatus.OFFER);
    }

    @Test
    @DisplayName("[success] createOffer 성공")
    public void createOffer_성공() {
        // given
        OfferRequest offerRequest = OfferRequest.builder()
                .itemId(item.getId())
                .price(offer.getPrice())
                .build();
        // mocking
        when(itemService.getItemById(offerRequest.itemId())).thenReturn(item);
        doNothing().when(permissionCheck).hasPermission(any(), any());
        doNothing().when(offerMapper).createOffer(any(OfferVO.class));

        // when
        offerService.createOffer(offerRequest, member);

        // then
        verify(offerMapper).createOffer(any(OfferVO.class));
    }

    @Test
    @DisplayName("[fail] createOffer 자기 아이템 오퍼 불가")
    public void createOffer_자기_아이템_오퍼_불가() {
        // given
        OfferRequest offerRequest = OfferRequest.builder()
                .itemId(item.getId())
                .price(offer.getPrice())
                .build();
        // mocking
        when(itemService.getItemById(offerRequest.itemId())).thenReturn(item);
        doThrow(new OfferException(OfferExceptionType.NO_OFFER_MYSELF)).when(permissionCheck)
                .hasPermission(any(), any());

        // when
        OfferException exception = assertThrows(OfferException.class, () -> {
            offerService.createOffer(offerRequest, seller);
        });

        // then
        assertThat(exception.exceptionType()).isEqualTo(OfferExceptionType.NO_OFFER_MYSELF);
    }

    @Test
    @DisplayName("[success] updateStatusOffer 성공")
    public void updateStatusOffer_성공() {
        // given
        OfferStatus offerStatus = OfferStatus.ACCEPT;

        // mocking
        when(offerMapper.findOfferById(offer.getId())).thenReturn(offer);
        when(itemService.getItemById(item.getId())).thenReturn(item);
        doNothing().when(permissionCheck).hasPermission(any());
        doNothing().when(offerMapper).updateStatusOffer(offer.getId(), offerStatus);

        // when
        offerService.updateStatusOffer(offer.getId(), offerStatus.name(), seller);

        // then
        verify(offerMapper).updateStatusOffer(offer.getId(), offerStatus);
    }

    @Test
    @DisplayName("[fail] updateStatusOffer 실패")
    public void updateStatusOffer_아이템_권한_부족() {
        // given
        OfferStatus offerStatus = OfferStatus.ACCEPT;

        // mocking
        when(offerMapper.findOfferById(offer.getId())).thenReturn(offer);
        when(itemService.getItemById(item.getId())).thenReturn(item);
        doThrow(new MemberException(MemberExceptionType.UNAUTHORIZED_PERMISSION)).when(permissionCheck)
                .hasPermission(any());

        // when
        MemberException exception = assertThrows(MemberException.class, () -> {
            offerService.updateStatusOffer(offer.getId(), offerStatus.name(), member);
        });

        // then
        assertThat(exception.exceptionType()).isEqualTo(MemberExceptionType.UNAUTHORIZED_PERMISSION);
    }

    @Test
    @DisplayName("[success] updatePriceOffer 성공")
    public void updatePriceOffer_성공() {
        // given
        int price = 4000;

        // mocking
        when(offerMapper.findOfferById(offer.getId())).thenReturn(offer);
        doNothing().when(permissionCheck).hasPermission(any());
        doNothing().when(offerMapper).updatePriceOffer(offer.getId(), price);

        // when
        offerService.updatePriceOffer(offer.getId(), price, member);

        // then
        verify(offerMapper).updatePriceOffer(offer.getId(), price);
    }

    @Test
    @DisplayName("[fail] updatePriceOffer 실패")
    public void updatePriceOffer_오퍼_권한_부족() {
        // given
        int price = 4000;

        // mocking
        when(offerMapper.findOfferById(offer.getId())).thenReturn(offer);
        doThrow(new MemberException(MemberExceptionType.UNAUTHORIZED_PERMISSION)).when(permissionCheck)
                .hasPermission(any());

        // when
        MemberException exception = assertThrows(MemberException.class, () -> {
            offerService.updatePriceOffer(offer.getId(), price, seller);
        });

        // then
        assertThat(exception.exceptionType()).isEqualTo(MemberExceptionType.UNAUTHORIZED_PERMISSION);
    }

    @Test
    @DisplayName("[success] deleteOffer 성공")
    public void deleteOffer_성공() {
        // mocking
        when(offerMapper.findOfferById(offer.getId())).thenReturn(offer);
        doNothing().when(permissionCheck).hasPermission(any());
        lenient().doNothing().when(offerMapper).deleteOffer(offer.getId());

        // when
        offerService.deleteOffer(offer.getId(), seller);

        // then
        verify(offerMapper).deleteOffer(offer.getId());
    }

    @Test
    @DisplayName("[success] getOffersByItemId 성공")
    public void getOffersByItemId_성공() {
        // given
        MemberVO member2 = MemberFixture.testIdMember(AuthType.ROLE_VERIFY_USER);
        OfferVO offer2 = OfferFixture.testIdOffer(member2, item, 4000, OfferStatus.OFFER);
        List<OfferVO> offers = List.of(offer, offer2);

        // mocking
        doNothing().when(permissionCheck).hasPermission(any());
        when(offerMapper.getOffersByItemId(item.getId())).thenReturn(offers);

        // when
        List<OfferResponse> savedOffers = offerService.getOffersByItemId(item.getId(), seller);

        // then
        assertThat(savedOffers).isNotEmpty();
        assertThat(savedOffers.size()).isEqualTo(offers.size());
    }

    @Test
    @DisplayName("[success] getOffersByMemberId 성공")
    public void getOffersByMemberId_성공() {
        // given
        ItemVO item2 = ItemFixture.testIdItem(seller);
        OfferVO offer2 = OfferFixture.testIdOffer(member, item2, 4000, OfferStatus.OFFER);
        List<OfferVO> offers = List.of(offer, offer2);

        // mocking
        when(offerMapper.getOffersByMemberId(member.getId())).thenReturn(offers);

        // when
        List<OfferResponse> savedOffers = offerService.getOffersByMemberId(member);

        // then
        assertThat(savedOffers).isNotEmpty();
        assertThat(savedOffers.size()).isEqualTo(offers.size());
    }
}
