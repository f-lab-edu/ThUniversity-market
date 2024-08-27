package university.market.tag.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import university.market.item.domain.ItemVO;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.tag.domain.TagItemVO;
import university.market.tag.domain.TagMemberVO;
import university.market.tag.domain.TagVO;
import university.market.tag.exception.TagException;
import university.market.tag.exception.TagExceptionType;
import university.market.tag.mapper.TagItemMapper;
import university.market.tag.mapper.TagMapper;
import university.market.tag.mapper.TagMemberMapper;
import university.market.tag.service.dto.request.TagMemberRequest;
import university.market.utils.auth.PermissionCheck;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;
    private final TagItemMapper tagItemMapper;
    private final TagMemberMapper tagMemberMapper;
    private final PermissionCheck permissionCheck;

    @Transactional
    @Override
    public void deleteTag(long tagId) {
        tagMapper.deleteTag(tagId);
    }

    @Override
    public void createTagItem(List<String> tags, ItemVO item) {
        tags.forEach(tag -> {
            tag.toUpperCase();
            TagVO tagVO = tagMapper.findTagByTitle(tag).orElseGet(() -> {
                TagVO newTag = TagVO.builder()
                        .title(tag)
                        .build();
                tagMapper.insertTag(newTag);
                return newTag;
            });

            TagItemVO tagItem = TagItemVO.builder()
                    .tag(tagVO)
                    .item(item)
                    .build();

            tagItemMapper.insertTagItem(tagItem);
        });
    }

    @Transactional
    @Override
    public void deleteTagItem(long tagItemId, MemberVO member) {
        TagItemVO tagItem = tagItemMapper.findTagItemById(tagItemId)
                .orElseThrow(() -> new TagException(TagExceptionType.NOT_EXIST_TAG_ITEM));
        permissionCheck.hasPermission(() ->
                !Objects.equals(tagItem.getItem().getSeller().getId(), member.getId())
                        || !member.getAuth().equals(AuthType.ROLE_ADMIN));
        tagItemMapper.deleteTagItem(tagItemId);
    }

    @Transactional
    @Override
    public void createTagMember(TagMemberRequest request) {
        List<TagMemberVO> tagMembers = tagMemberMapper.findTagMembersByTagId(request.tagId());

        if (tagMembers.stream().anyMatch(tagMember ->
                Objects.equals(tagMember.getMember().getId(), request.member().getId()))) {
            throw new TagException(TagExceptionType.ALREADY_EXIST_TAG_MEMBER);
        }

        TagVO tag = tagMapper.findTagById(request.tagId())
                .orElseThrow(() -> new TagException(TagExceptionType.TAG_NOT_FOUND));

        TagMemberVO tagMember = TagMemberVO.builder()
                .tag(tag)
                .member(request.member())
                .build();
        tagMemberMapper.insertTagMember(tagMember);
    }

    @Transactional
    @Override
    public void deleteTagMember(long tagMemberId, MemberVO member) {
        TagMemberVO tagMember = tagMemberMapper.findTagMemberById(tagMemberId)
                .orElseThrow(() -> new TagException(TagExceptionType.NOT_EXIST_TAG_MEMBER));

        permissionCheck.hasPermission(() ->
                !Objects.equals(tagMember.getMember().getId(), member.getId())
                        || !member.getAuth().equals(AuthType.ROLE_ADMIN));
        tagMemberMapper.deleteTagMember(tagMemberId);
    }

    @Override
    public List<TagItemVO> findTagItemsByTagId(long TagId) {
        return tagItemMapper.findTagItemsByTagId(TagId);
    }

    @Override
    public List<TagItemVO> findTagItemsByItemId(long itemId) {
        return tagItemMapper.findTagItemsByItemId(itemId);
    }

    @Override
    public List<TagMemberVO> findTagMembersByMemberId(long memberId) {
        return tagMemberMapper.findTagMembersByMemberId(memberId);
    }
}
