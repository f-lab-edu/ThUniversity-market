package university.market.dibs.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import university.market.dibs.domain.DibsVO;

@Mapper
public interface DibsMapper {
    void addDibs(DibsVO dibsVO);

    void removeDibs(Long dibsId);

    List<DibsVO> getDibsByMemberId(Long memberId);
}
