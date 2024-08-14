package university.market.dibs.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;
import university.market.dibs.domain.DibsVO;
import university.market.item.domain.ItemVO;
import university.market.item.mapper.ItemMapper;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.mapper.MemberMapper;
import university.market.utils.test.helper.dibs.DibsFixture;
import university.market.utils.test.helper.item.ItemFixture;
import university.market.utils.test.helper.member.MemberFixture;

@MybatisTest
@AutoConfigureTestDatabase(replace = NONE)
public class DibsMapperTest {
    @Autowired
    private DibsMapper dibsMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private ItemMapper itemMapper;

    private MemberVO member;

    private MemberVO seller;

    private ItemVO item;

    private DibsVO dibs;

    @Transactional
    @BeforeEach
    public void init() {
        member = MemberFixture.testMember(AuthType.ROLE_VERIFY_USER);
        memberMapper.joinMember(member);

        seller = MemberFixture.testMember(AuthType.ROLE_VERIFY_USER);
        memberMapper.joinMember(seller);

        item = ItemFixture.testItem(seller);
        itemMapper.postItem(item);

        dibs = DibsFixture.testDibs(member, item);
    }

    @Test
    @DisplayName("[success] 찜 추가 성공")
    public void 찜_추가_성공() {
        // when
        dibsMapper.addDibs(dibs);
        DibsVO retrievedDibsVO = dibsMapper.getDibsByMemberId(member.getId()).get(0);

        // then
        assertThat(retrievedDibsVO).isNotNull();
        assertThat(retrievedDibsVO.getMember().getId()).isEqualTo(member.getId());
        assertThat(retrievedDibsVO.getItem().getId()).isEqualTo(item.getId());
    }

    @Test
    @DisplayName("[success] 찜 삭제 성공")
    public void 찜_삭제_성공() {
        // given
        dibsMapper.addDibs(dibs);
        DibsVO retrievedDibsVO = dibsMapper.getDibsByMemberId(member.getId()).get(0);

        // when
        dibsMapper.removeDibs(retrievedDibsVO.getId());
        boolean isEmpty = dibsMapper.getDibsByMemberId(member.getId()).isEmpty();

        // then
        assertThat(isEmpty).isEqualTo(true);
    }

    @Test
    @DisplayName("[success] 멤버 기준 찜 조회 성공")
    public void 찜_조회_성공() {
        // given
        ItemVO item2 = ItemFixture.testItem(seller);
        itemMapper.postItem(item2);

        DibsVO dibs2 = DibsFixture.testDibs(member, item2);
        dibsMapper.addDibs(dibs);
        dibsMapper.addDibs(dibs2);

        // when
        List<DibsVO> dibsList = dibsMapper.getDibsByMemberId(member.getId());

        // then
        assertThat(dibsList.size()).isEqualTo(2);
        assertThat(dibsList.get(0).getId()).isEqualTo(dibs.getId());
        assertThat(dibsList.get(1).getId()).isEqualTo(dibs2.getId());
    }
}
