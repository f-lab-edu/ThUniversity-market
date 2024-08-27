package university.market.tag.service;

import java.util.List;
import university.market.item.domain.ItemVO;
import university.market.member.domain.MemberVO;
import university.market.tag.domain.TagItemVO;
import university.market.tag.domain.TagMemberVO;
import university.market.tag.service.dto.request.TagMemberRequest;

public interface TagService {
    void deleteTag(long tagId);

    void createTagItem(List<String> tags, ItemVO item);

    void deleteTagItem(long tagItemId, MemberVO member);

    void createTagMember(TagMemberRequest request);

    void deleteTagMember(long tagMemberId, MemberVO member);

    List<TagItemVO> findTagItemsByTagId(long TagId);

    List<TagItemVO> findTagItemsByItemId(long itemId);

    List<TagMemberVO> findTagMembersByMemberId(long memberId);
}
