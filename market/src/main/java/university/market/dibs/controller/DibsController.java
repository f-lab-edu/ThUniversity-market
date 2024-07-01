package university.market.dibs.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import university.market.dibs.domain.DibsVO;
import university.market.dibs.service.DibsService;
import university.market.member.annotation.AuthCheck;
import university.market.member.domain.auth.AuthType;

@RestController
@RequestMapping("/api/dibs")
@RequiredArgsConstructor
public class DibsController {

    private final DibsService dibsService;

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @GetMapping("/{itemId}")
    public ResponseEntity<Void> addDibs(@PathVariable Long itemId) {
        dibsService.addDibs(itemId);
        return ResponseEntity.ok().build();
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @DeleteMapping("/{dibsId}")
    public ResponseEntity<Void> removeDibs(@PathVariable Long dibsId) {
        dibsService.removeDibs(dibsId);
        return ResponseEntity.ok().build();
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @GetMapping("/list")
    public ResponseEntity<List<DibsVO>> getDibsByMemberId() {
        List<DibsVO> dibsList = dibsService.getDibsByMemberId();
        return ResponseEntity.ok(dibsList);
    }
}
