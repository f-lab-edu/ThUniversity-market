package university.market.item.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

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
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.mapper.MemberMapper;
import university.market.utils.test.helper.item.ItemFixture;
import university.market.utils.test.helper.member.MemberFixture;

@MybatisTest
@AutoConfigureTestDatabase(replace = NONE)
@ContextConfiguration(classes = MarketMapperApplication.class)
public class ItemMapperTest {
    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private MemberMapper memberMapper;

    private ItemVO item;

    private MemberVO member;

    @BeforeEach
    @Transactional
    public void init() {
        // given
        member = MemberFixture.testMember(AuthType.ROLE_VERIFY_USER);
        memberMapper.joinMember(member);

        item = ItemFixture.testItem(member);
    }

    @Test
    @Transactional
    @DisplayName("[success] item 등록후 찾기 성공")
    public void item_등록_성공() {
        // when
        itemMapper.postItem(item);
        ItemVO postedItem = itemMapper.getItemById(item.getId());

        // then
        assertThat(postedItem.getTitle()).isEqualTo(item.getTitle());
        assertThat(postedItem.getDescription()).isEqualTo(item.getDescription());
    }


    @Test
    @Transactional
    @DisplayName("[success] item 업데이트 성공")
    public void item_업데이트_성공() {
        // given
        itemMapper.postItem(item);

        ItemVO updateItem = ItemFixture.testItem(member);

        // when
        itemMapper.updateItem(item.getId(), updateItem);
        ItemVO updatedItem = itemMapper.getItemById(item.getId());

        // then
        assertThat(updatedItem.getTitle()).isEqualTo(updateItem.getTitle());
        assertThat(updatedItem.getDescription()).isEqualTo(updateItem.getDescription());
        assertThat(updatedItem.getPrice()).isEqualTo(updateItem.getPrice());
    }

    @Test
    @Transactional
    @DisplayName("[success] item 삭제 성공")
    public void item_삭제_성공() {
        // given
        itemMapper.postItem(item);

        // when
        itemMapper.deleteItem(item.getId());
        ItemVO deletedItem = itemMapper.getItemById(item.getId());

        // then
        assertThat(deletedItem).isNull();
    }

    @Test
    @Transactional
    @DisplayName("[success] 대학교 item 찾기 성공")
    public void 대학교_item_찾기_성공() {
        // given
        itemMapper.postItem(item);

        // when
        List<ItemVO> foundItem = itemMapper.getItemsByUniversity(member.getUniversity());

        // then
        assertThat(foundItem.size()).isEqualTo(1);
        assertThat(foundItem.get(0).getTitle()).isEqualTo(item.getTitle());
    }
}
