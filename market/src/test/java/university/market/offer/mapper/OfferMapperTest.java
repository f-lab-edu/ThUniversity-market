package university.market.offer.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;
import university.market.helper.fixture.ItemFixture;
import university.market.helper.fixture.MemberFixture;
import university.market.helper.fixture.OfferFixture;
import university.market.item.domain.ItemVO;
import university.market.item.mapper.ItemMapper;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.mapper.MemberMapper;
import university.market.offer.domain.OfferVO;

@MybatisTest
@Slf4j
@AutoConfigureTestDatabase(replace = NONE)
public class OfferMapperTest {

    @Autowired
    private OfferMapper offerMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private ItemMapper itemMapper;

    private OfferVO offer;

    private ItemVO item;

    private MemberVO seller;

    private MemberVO buyer;

    @BeforeEach
    @Transactional
    public void init() {
        // given
        buyer = MemberFixture.testMember(AuthType.ROLE_VERIFY_USER);
        seller = MemberFixture.testMember(AuthType.ROLE_VERIFY_USER);

        memberMapper.joinMember(buyer);
        memberMapper.joinMember(seller);

        item = ItemFixture.testItem(seller);
        itemMapper.postItem(item);

        offer = OfferFixture.testOffer(buyer, item);
    }

    @Test
    @Transactional
    @DisplayName("[success] offer 등록후 찾기 성공")
    public void offer_등록후_찾기_성공() {
        // when
        offerMapper.createOffer(offer);
        OfferVO savedOffer = offerMapper.findOfferById(offer.getId());

        // then
        assertThat(savedOffer.getId()).isEqualTo(offer.getId());
        assertThat(savedOffer.getPrice()).isEqualTo(offer.getPrice());
        assertThat(savedOffer.getItem().getId()).isEqualTo(offer.getItem().getId());
        assertThat(savedOffer.getBuyer().getId()).isEqualTo(offer.getBuyer().getId());
    }

    @Test
    @Transactional
    @DisplayName("[success] offer 업데이트 성공")
    public void offer_업데이트_성공() {
        // given
        offerMapper.createOffer(offer);
        OfferVO updateOffer = OfferFixture.testOffer(buyer, item);

        // when
        offerMapper.updateOffer(offer.getId(), updateOffer.getPrice());
        OfferVO updatedOffer = offerMapper.findOfferById(offer.getId());

        // then
        assertThat(updatedOffer.getPrice()).isEqualTo(updateOffer.getPrice());
    }

    @Test
    @Transactional
    @DisplayName("[success] offer 삭제 성공")
    public void offer_삭제_성공() {
        // given
        offerMapper.createOffer(offer);

        // when
        offerMapper.deleteOffer(offer.getId());
        OfferVO deletedOffer = offerMapper.findOfferById(offer.getId());

        // then
        assertThat(deletedOffer).isNull();
    }

    @Test
    @Transactional
    @DisplayName("[success] offer 수락 성공")
    public void offer_수락_성공() {
        // given
        offerMapper.createOffer(offer);

        // when
        offerMapper.acceptOffer(offer.getId());
        OfferVO acceptedOffer = offerMapper.findOfferById(offer.getId());

        // then
        assertThat(acceptedOffer.isAccepted()).isTrue();
    }

    @Test
    @Transactional
    @DisplayName("[success] item id로 offer 목록 조회 성공")
    public void item_id로_offer_목록_조회_성공() {
        // given
        MemberVO buyer2 = MemberFixture.testMember(AuthType.ROLE_VERIFY_USER);
        memberMapper.joinMember(buyer2);

        OfferVO offer2 = OfferFixture.testOffer(buyer2, item);
        offerMapper.createOffer(offer);
        offerMapper.createOffer(offer2);


        // when
        List<OfferVO> savedOffers = offerMapper.getOffersByItemId(item.getId());

        // then
        assertThat(savedOffers.size()).isEqualTo(2);
        assertThat(savedOffers.get(0).getId()).isEqualTo(offer.getId());
        assertThat(savedOffers.get(1).getId()).isEqualTo(offer2.getId());

    }

    @Test
    @Transactional
    @DisplayName("[success] member id로 offer 목록 조회 성공")
    public void member_id로_offer_목록_조회_성공() {
        // given
        ItemVO item2 = ItemFixture.testItem(seller);
        itemMapper.postItem(item2);

        OfferVO offer2 = OfferFixture.testOffer(buyer, item2);
        offerMapper.createOffer(offer);
        offerMapper.createOffer(offer2);

        // when
        List<OfferVO> savedOffers = offerMapper.getOffersByMemberId(buyer.getId());

        // then
        assertThat(savedOffers.size()).isEqualTo(2);
        assertThat(savedOffers.get(0).getId()).isEqualTo(offer.getId());
        assertThat(savedOffers.get(1).getId()).isEqualTo(offer2.getId());
    }

}
