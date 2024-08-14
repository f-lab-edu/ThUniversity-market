package university.market.dibs.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import university.market.dibs.domain.DibsVO;
import university.market.dibs.exception.DibsException;
import university.market.dibs.exception.DibsExceptionType;
import university.market.dibs.mapper.DibsMapper;
import university.market.helper.fixture.DibsFixture;
import university.market.helper.fixture.ItemFixture;
import university.market.helper.fixture.MemberFixture;
import university.market.item.domain.ItemVO;
import university.market.item.service.ItemService;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.utils.http.HttpRequest;
import university.market.utils.auth.PermissionCheck;

@ExtendWith(MockitoExtension.class)
public class DibsServiceTest {
    @Mock
    private HttpRequest httpRequest;

    @Mock
    private ItemService itemService;

    @Mock
    private DibsMapper dibsMapper;

    @Mock
    private PermissionCheck permissionCheck;

    @InjectMocks
    private DibsServiceImpl dibsService;

    private DibsVO dibs;

    private MemberVO member;

    private MemberVO seller;

    private ItemVO item;

    @BeforeEach
    public void init() {
        seller = MemberFixture.testIdMember(AuthType.ROLE_VERIFY_USER);

        member = MemberFixture.testIdMember(AuthType.ROLE_VERIFY_USER);

        item = ItemFixture.testIdItem(seller);

        dibs = DibsFixture.testDibs(member, item);
    }

    @Test
    @DisplayName("[success] 찜 추가 성공")
    public void 찜_추가_성공() {
        // mocking
        when(itemService.getItemById(dibs.getItem().getId())).thenReturn(dibs.getItem());
        doNothing().when(permissionCheck).hasPermission(any(), any());

        // when
        dibsService.addDibs(dibs.getItem().getId(), member);

        // then
        verify(dibsMapper).addDibs(dibs);
    }

    @Test
    @DisplayName("[fail] 자기 자신 아이템 찜 추가 실패")
    public void 자기_자신_아이템_찜_추가_실패() {
        // mocking
        when(itemService.getItemById(dibs.getItem().getId())).thenReturn(dibs.getItem());
        doThrow(new DibsException(DibsExceptionType.NO_DIBS_MYSELF)).when(permissionCheck).hasPermission(any(), any());

        // when
        DibsException exception = assertThrows(DibsException.class, () -> {
            dibsService.addDibs(item.getId(), seller);
        });

        // then
        assertThat(exception.exceptionType().errorCode()).isEqualTo(DibsExceptionType.NO_DIBS_MYSELF.errorCode());
    }
}
