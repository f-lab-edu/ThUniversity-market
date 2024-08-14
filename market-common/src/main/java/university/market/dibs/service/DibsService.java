package university.market.dibs.service;

import java.util.List;
import university.market.dibs.domain.DibsVO;
import university.market.member.domain.MemberVO;

public interface DibsService {
    public void addDibs(Long itemId, MemberVO currentMember);

    public void removeDibs(Long dibsId);

    public List<DibsVO> getDibsByMemberId(MemberVO currentMember);
}
