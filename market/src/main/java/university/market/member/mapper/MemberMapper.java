package university.market.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import university.market.member.domain.MemberVO;

@Mapper
public interface MemberMapper {
    void joinMember(MemberVO memberVO);

    MemberVO findMemberByEmail(String email);

    void deleteMemberByEmail(String email);

    void deleteMemberById(Long id);
}
