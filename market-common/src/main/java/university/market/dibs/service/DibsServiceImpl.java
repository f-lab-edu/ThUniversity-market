package university.market.dibs.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import university.market.dibs.domain.DibsVO;
import university.market.dibs.exception.DibsException;
import university.market.dibs.exception.DibsExceptionType;
import university.market.dibs.mapper.DibsMapper;
import university.market.item.domain.ItemVO;
import university.market.item.service.ItemService;
import university.market.member.annotation.AuthCheck;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.utils.auth.PermissionCheck;

@Service
@RequiredArgsConstructor
public class DibsServiceImpl implements DibsService {

    private ItemService itemService;

    private DibsMapper dibsMapper;

    private PermissionCheck permissionCheck;

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Transactional
    @Override
    public void addDibs(Long itemId, MemberVO currentMember) {
        ItemVO item = itemService.getItemById(itemId);

        permissionCheck.hasPermission(() -> item.getSeller().equals(currentMember),
                new DibsException(DibsExceptionType.NO_DIBS_MYSELF));

        DibsVO dibsVO = DibsVO.builder()
                .member(currentMember)
                .item(item)
                .build();

        dibsMapper.addDibs(dibsVO);
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Transactional
    @Override
    public void removeDibs(Long dibsId) {
        dibsMapper.removeDibs(dibsId);
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @Transactional(readOnly = true)
    @Override
    public List<DibsVO> getDibsByMemberId(MemberVO currentMember) {
        return dibsMapper.getDibsByMemberId(currentMember.getId());
    }
}
