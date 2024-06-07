package university.market.item.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;
import university.market.item.domain.ItemVO;
import university.market.item.domain.status.StatusType;
import university.market.member.domain.MemberVO;
import university.market.member.mapper.MemberMapper;

@MybatisTest
@AutoConfigureTestDatabase(replace = NONE)
public class ItemMapperTest {
    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private MemberMapper memberMapper;

    private ItemVO itemVO;

    private MemberVO memberVO;

    @BeforeEach
    @Transactional
    public void init() {
        // given
        memberVO = memberMapper.findMemberByEmail("admin@example.com");
        itemVO = ItemVO.builder()
                .title("test")
                .description("testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest")
                .imageUrl("http://testtesttest.testtesttest.testtesttest")
                .seller(memberVO)
                .statusType(StatusType.SELLING)
                .price(3000)
                .build();
    }

    @Test
    @Transactional
    @DisplayName("[success] item 등록후 찾기 성공")
    public void item_등록_성공() {
        // when
        itemMapper.postItem(itemVO);
        ItemVO item = itemMapper.getItemById(1L);

        // then
        assertThat(item.getTitle()).isEqualTo(itemVO.getTitle());
        assertThat(item.getDescription()).isEqualTo(itemVO.getDescription());
    }


    @Test
    @Transactional
    @DisplayName("[success] item 업데이트 성공")
    public void item_업데이트_성공() {
        // given
        itemMapper.postItem(itemVO);

        ItemVO updatedItemVO = ItemVO.builder()
                .title("updateTest")
                .description("updatetesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest")
                .imageUrl("http://testtesttest.testtesttest.testtesttest")
                .seller(memberVO)
                .statusType(StatusType.SELLING)
                .price(30000)
                .build();

        // when
        itemMapper.updateItem(1L, updatedItemVO);
        ItemVO updatedItem = itemMapper.getItemById(1L);

        // then
        assertThat(updatedItem.getTitle()).isEqualTo(updatedItemVO.getTitle());
        assertThat(updatedItem.getDescription()).isEqualTo(updatedItemVO.getDescription());
        assertThat(updatedItem.getPrice()).isEqualTo(updatedItemVO.getPrice());
    }
}
