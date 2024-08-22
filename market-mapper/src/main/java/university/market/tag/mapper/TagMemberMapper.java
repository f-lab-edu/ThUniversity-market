package university.market.tag.mapper;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import university.market.tag.domain.TagMemberVO;

@Mapper
public interface TagMemberMapper {

    void insertTagMember(TagMemberVO tagMember);

    void deleteTagMember(long id);

    Optional<TagMemberVO> findTagMemberById(long id);

    List<TagMemberVO> findTagMembersByTagId(long tagId);

    List<TagMemberVO> findTagMembersByMemberId(long memberId);
}
