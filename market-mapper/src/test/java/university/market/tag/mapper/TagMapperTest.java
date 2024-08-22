package university.market.tag.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import university.market.MarketMapperApplication;
import university.market.tag.domain.TagVO;
import university.market.utils.test.helper.tag.TagFixture;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = MarketMapperApplication.class)
public class TagMapperTest {
    @Autowired
    TagMapper tagMapper;

    private TagVO tagVO;

    @BeforeEach
    public void init() {
        tagVO = TagFixture.testTag();
    }

    @Test
    @Transactional
    @DisplayName("[success] 태그 추가 성공")
    public void insertTagTest() {
        tagMapper.insertTag(tagVO);

        assertThat(tagVO.getId()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("[success] 태그 조회 성공")
    public void selectTagTest() {
        tagMapper.insertTag(tagVO);

        TagVO findTag = tagMapper.findTagById(tagVO.getId()).get();

        assertThat(findTag.getTitle()).isEqualTo(tagVO.getTitle());

    }

    @Test
    @Transactional
    @DisplayName("[success] 태그 이름으로 조회 성공")
    public void selectTagByTitleTest() {
        tagMapper.insertTag(tagVO);

        TagVO findTag = tagMapper.findTagByTitle(tagVO.getTitle()).get();

        assertThat(findTag.getTitle()).isEqualTo(tagVO.getTitle());
    }

    @Test
    @Transactional
    @DisplayName("[success] 태그 삭제 성공")
    public void deleteTagTest() {
        tagMapper.insertTag(tagVO);
        tagMapper.deleteTag(tagVO.getId());

        assertThat(tagMapper.findTagById(tagVO.getId())).isEmpty();
    }
}
