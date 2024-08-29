package university.market.tag.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import university.market.MarketMapperApplication;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.mapper.MemberMapper;
import university.market.tag.domain.TagMemberVO;
import university.market.tag.domain.TagVO;
import university.market.utils.test.helper.member.MemberFixture;
import university.market.utils.test.helper.tag.TagFixture;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = MarketMapperApplication.class)
public class TagMemberMapperTest {
    @Autowired
    TagMapper tagMapper;

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    TagMemberMapper tagMemberMapper;

    private TagVO tag;

    private MemberVO member;

    private TagMemberVO tagMember;

    @BeforeEach
    public void init() {
        tag = TagFixture.testTag();
        tagMapper.insertTag(tag);

        member = MemberFixture.testMember(AuthType.ROLE_VERIFY_USER);
        memberMapper.joinMember(member);

        tagMember = TagMemberVO.builder()
                .tag(tag)
                .member(member)
                .build();
    }

    @Test
    @Transactional
    @DisplayName("[success] 태그 멤버 추가 성공")
    public void insertTagMember_태그_멤버_추가_성공() {
        // when
        tagMemberMapper.insertTagMember(tagMember);

        // then
        assertThat(tagMember.getId()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("[success] 태그 멤버 삭제 성공")
    public void deleteTagMember_태그_멤버_삭제_성공() {
        // given
        tagMemberMapper.insertTagMember(tagMember);

        // when
        tagMemberMapper.deleteTagMember(tagMember.getId());

        // then
        assertThat(tagMemberMapper.findTagMemberById(tagMember.getId())).isEmpty();
    }

    @Test
    @Transactional
    @DisplayName("[success] 태그 멤버 조회 성공")
    public void findTagMemberById_태그_멤버_조회_성공() {
        // given
        tagMemberMapper.insertTagMember(tagMember);

        // when
        TagMemberVO findTagMember = tagMemberMapper.findTagMemberById(tagMember.getId()).get();

        // then
        assertThat(findTagMember.getTag().getTitle()).isEqualTo(tag.getTitle());
        assertThat(findTagMember.getMember().getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @Transactional
    @DisplayName("[success] 태그 아이디로 태그 멤버 조회 성공")
    public void findTagMembersByTagId_태그_아이디로_태그_멤버_조회_성공() {
        // given
        tagMemberMapper.insertTagMember(tagMember);

        MemberVO member2 = MemberFixture.testMember(AuthType.ROLE_VERIFY_USER);
        memberMapper.joinMember(member2);
        TagMemberVO tagMember2 = TagMemberVO.builder()
                .tag(tag)
                .member(member2)
                .build();
        tagMemberMapper.insertTagMember(tagMember2);

        // when
        List<TagMemberVO> findTagMembers = tagMemberMapper.findTagMembersByTagId(tag.getId());

        // then
        assertThat(findTagMembers.size()).isEqualTo(2);
        assertThat(findTagMembers.get(0).getMember().getEmail()).isEqualTo(member.getEmail());
        assertThat(findTagMembers.get(1).getMember().getEmail()).isEqualTo(member2.getEmail());
    }

    @Test
    @Transactional
    @DisplayName("[success] 멤버 아이디로 태그 멤버 조회 성공")
    public void findTagMembersByMemberId_멤버_아이디로_태그_멤버_조회_성공() {
        // given
        tagMemberMapper.insertTagMember(tagMember);

        TagVO tag2 = TagFixture.testTag();
        tagMapper.insertTag(tag2);
        TagMemberVO tagMember2 = TagMemberVO.builder()
                .tag(tag2)
                .member(member)
                .build();
        tagMemberMapper.insertTagMember(tagMember2);

        // when
        List<TagMemberVO> findTagMembers = tagMemberMapper.findTagMembersByMemberId(member.getId());

        // then
        assertThat(findTagMembers.size()).isEqualTo(2);
        assertThat(findTagMembers.get(0).getTag().getTitle()).isEqualTo(tag.getTitle());
        assertThat(findTagMembers.get(1).getTag().getTitle()).isEqualTo(tag2.getTitle());
    }
}
