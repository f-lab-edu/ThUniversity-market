package university.market.tag.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import university.market.utils.test.helper.item.ItemFixture;
import university.market.utils.test.helper.member.MemberFixture;
import university.market.utils.test.helper.tag.TagFixture;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {
    @Mock
    private TagMapper tagMapper;

    @Mock
    private TagItemMapper tagItemMapper;

    @Mock
    private TagMemberMapper tagMemberMapper;

    @InjectMocks
    private TagServiceImpl tagService;

    private MemberVO member;

    private MemberVO seller;

    private ItemVO item;

    private TagVO tag;

    private TagItemVO tagItem;

    private TagMemberVO tagMember;

    @BeforeEach
    public void init() {
        member = MemberFixture.testMember(AuthType.ROLE_VERIFY_USER);
        seller = MemberFixture.testIdMember(AuthType.ROLE_VERIFY_USER);
        item = ItemFixture.testItem(seller);

        tag = TagFixture.testIdTag();
    }

    @Test
    @DisplayName("[success] 태그 아이템 생성 - 존재하는 태그")
    void createTagItem_태그_아이템_생성_존재하는_태그() {
        // mocking
        when(tagMapper.findTagByTitle(tag.getTitle())).thenReturn(Optional.of(tag));
        doNothing().when(tagItemMapper).insertTagItem(any(TagItemVO.class));

        // when
        tagService.createTagItem(List.of(tag.getTitle()), item);

        // then
        verify(tagMapper).findTagByTitle(tag.getTitle());
        verify(tagItemMapper).insertTagItem(any(TagItemVO.class));
    }

    @Test
    @DisplayName("[success] 태그 아이템 생성 - 존재하지 않는 태그")
    void createTagItem_태그_아이템_생성_존재하지_않는_태그() {
        // given
        TagVO newTag = TagFixture.testTag();

        // mocking
        when(tagMapper.findTagByTitle(newTag.getTitle())).thenReturn(Optional.empty());
        doNothing().when(tagMapper).insertTag(newTag);
        doNothing().when(tagItemMapper).insertTagItem(any(TagItemVO.class));

        // when
        tagService.createTagItem(List.of(newTag.getTitle()), item);

        // then
        verify(tagMapper).findTagByTitle(newTag.getTitle());
        verify(tagMapper).insertTag(newTag);
        verify(tagItemMapper).insertTagItem(any(TagItemVO.class));
    }

    @Test
    @DisplayName("[success] 태그 멤버 생성")
    void createTagMember_태그_멤버_생성() {
        // given
        TagMemberRequest tagMemberRequest = TagMemberRequest
                .builder()
                .tagId(tag.getId())
                .member(member)
                .build();

        tagMember = TagMemberVO.builder()
                .tag(tag)
                .member(member)
                .build();

        // mocking
        when(tagMemberMapper.findTagMembersByTagId(tag.getId())).thenReturn(List.of());
        when(tagMapper.findTagById(tag.getId())).thenReturn(Optional.of(tag));
        doNothing().when(tagMemberMapper).insertTagMember(tagMember);

        // when
        tagService.createTagMember(tagMemberRequest);

        // then
        verify(tagMemberMapper).findTagMembersByTagId(tag.getId());
        verify(tagMapper).findTagById(tag.getId());
        verify(tagMemberMapper).insertTagMember(tagMember);
    }

    @Test
    @DisplayName("[fail] 태그 멤버 생성 - 이미 존재하는 멤버")
    void createTagMember_태그_멤버_생성_이미_존재하는_멤버() {
        // given
        TagMemberRequest tagMemberRequest = TagMemberRequest
                .builder()
                .tagId(tag.getId())
                .member(member)
                .build();

        tagMember = TagMemberVO.builder()
                .tag(tag)
                .member(member)
                .build();

        // mocking
        when(tagMemberMapper.findTagMembersByTagId(tag.getId())).thenReturn(List.of(tagMember));

        // when
        // then
        assertThatThrownBy(() -> tagService.createTagMember(tagMemberRequest))
                .isInstanceOf(TagException.class)
                .hasMessage(TagExceptionType.ALREADY_EXIST_TAG_MEMBER.errorMessage());
    }
}