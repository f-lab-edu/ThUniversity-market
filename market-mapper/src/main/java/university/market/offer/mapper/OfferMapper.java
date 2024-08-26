package university.market.offer.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import university.market.offer.domain.OfferStatus;
import university.market.offer.domain.OfferVO;

@Mapper
public interface OfferMapper {
    void createOffer(OfferVO offer);

    void updateStatusOffer(Long id, OfferStatus status);

    void updatePriceOffer(Long id, int price);

    void deleteOffer(Long id);

    OfferVO findOfferById(Long id);

    List<OfferVO> getOffersByItemId(Long itemId);

    List<OfferVO> getOffersByMemberId(Long memberId);
}
