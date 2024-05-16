package university.market.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import university.market.member.domain.MemberVO;

@Mapper
public interface MemberMapper {
    boolean joinMember(MemberVO memberVO);

    MemberVO findMemberByEmail(String email);

    MemberVO loginMember(String email, String password);

}
