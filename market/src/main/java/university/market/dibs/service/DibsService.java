package university.market.dibs.service;

import java.util.List;
import university.market.dibs.domain.DibsVO;

public interface DibsService {
    public void addDibs(Long itemId);

    public void removeDibs(Long dibsId);

    public List<DibsVO> getDibsByMemberId();
}
