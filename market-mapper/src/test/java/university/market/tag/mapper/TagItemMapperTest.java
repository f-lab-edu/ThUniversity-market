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
import university.market.item.domain.ItemVO;
import university.market.item.mapper.ItemMapper;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.mapper.MemberMapper;
import university.market.tag.domain.TagItemVO;
import university.market.tag.domain.TagVO;
import university.market.utils.test.helper.item.ItemFixture;
import university.market.utils.test.helper.member.MemberFixture;
import university.market.utils.test.helper.tag.TagFixture;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = MarketMapperApplication.class)
public class TagItemMapperTest {
    @Autowired
    TagItemMapper tagItemMapper;

    @Autowired
    TagMapper tagMapper;

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    MemberMapper memberMapper;

    private MemberVO member;

    private TagVO tag;

    private ItemVO item;

    private TagItemVO tagItem;

    @BeforeEach
    public void init() {
        member = MemberFixture.testMember(AuthType.ROLE_VERIFY_USER);
        memberMapper.joinMember(member);

        tag = TagFixture.testTag();
        tagMapper.insertTag(tag);

        item = ItemFixture.testItem(member);
        itemMapper.postItem(item);

        tagItem = TagItemVO.builder()
                .tag(tag)
                .item(item)
                .build();
    }

    @Test
    @Transactional
    @DisplayName("[success] 태그 아이템 추가 성공")
    public void insertTagItem_태그_아이템_추가_성공() {
        tagItemMapper.insertTagItem(tagItem);

        assertThat(tagItem.getId()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("[success] 태그 아이템 조회 성공")
    public void findTagItemById_태그_아이템_조회_성공() {
        tagItemMapper.insertTagItem(tagItem);

        TagItemVO findTagItem = tagItemMapper.findTagItemById(tagItem.getId()).get();

        assertThat(findTagItem.getTag().getId()).isEqualTo(tag.getId());
        assertThat(findTagItem.getItem().getId()).isEqualTo(item.getId());
    }

    @Test
    @Transactional
    @DisplayName("[success] 태그 아이템 삭제 성공")
    public void deleteTagItem_태그_아이템_삭제_성공() {
        tagItemMapper.insertTagItem(tagItem);

        tagItemMapper.deleteTagItem(tagItem.getId());

        assertThat(tagItemMapper.findTagItemById(tagItem.getId())).isEmpty();
    }

    @Test
    @Transactional
    @DisplayName("[success] 태그 아이템 태그로 조회 성공")
    public void findTagItemsByTagId_태그_아이템_태그로_조회_성공() {
        tagItemMapper.insertTagItem(tagItem);

        ItemVO item2 = ItemFixture.testItem(member);
        itemMapper.postItem(item2);

        TagItemVO tagItem2 = TagItemVO.builder()
                .tag(tag)
                .item(item2)
                .build();
        tagItemMapper.insertTagItem(tagItem2);

        List<TagItemVO> tagItems = tagItemMapper.findTagItemsByTagId(tag.getId());

        assertThat(tagItems.size()).isEqualTo(2);
        assertThat(tagItems.get(0).getItem().getId()).isEqualTo(item.getId());
        assertThat(tagItems.get(1).getItem().getId()).isEqualTo(item2.getId());
    }

    @Test
    @Transactional
    @DisplayName("[success] 태그 아이템 아이템으로 조회 성공")
    public void findTagItemsByItemId_태그_아이템_아이템으로_조회_성공() {
        tagItemMapper.insertTagItem(tagItem);

        TagVO tag2 = TagFixture.testTag();
        tagMapper.insertTag(tag2);

        TagItemVO tagItem2 = TagItemVO.builder()
                .tag(tag2)
                .item(item)
                .build();
        tagItemMapper.insertTagItem(tagItem2);

        List<TagItemVO> tagItems = tagItemMapper.findTagItemsByItemId(item.getId());

        assertThat(tagItems.size()).isEqualTo(2);
        assertThat(tagItems.get(0).getTag().getId()).isEqualTo(tag.getId());
        assertThat(tagItems.get(1).getTag().getId()).isEqualTo(tag2.getId());
    }
}
