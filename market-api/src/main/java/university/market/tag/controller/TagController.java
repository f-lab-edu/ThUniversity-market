package university.market.tag.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import university.market.member.annotation.AuthCheck;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.utils.http.HttpRequest;
import university.market.tag.domain.TagItemVO;
import university.market.tag.domain.TagMemberVO;
import university.market.tag.service.TagService;
import university.market.tag.service.dto.request.TagMemberRequest;

@Tag(name = "Tag", description = "태그 관련 API")
@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    private final HttpRequest httpRequest;

    @Operation(summary = "태그 삭제")
    @AuthCheck(AuthType.ROLE_ADMIN)
    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> deleteTag(@PathVariable long tagId) {
        tagService.deleteTag(tagId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "태그 아이템 삭제")
    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @DeleteMapping("/item/{tagItemId}")
    public ResponseEntity<Void> deleteTagItem(@PathVariable long tagItemId, MemberVO member) {
        tagService.deleteTagItem(tagItemId, member);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "태그 멤버 삭제")
    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @DeleteMapping("/member/{tagMemberId}")
    public ResponseEntity<Void> deleteTagMember(@PathVariable long tagMemberId) {
        tagService.deleteTagMember(tagMemberId, httpRequest.getCurrentMember());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "태그에 속한 아이템 조회")
    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @GetMapping("/item/{tagId}")
    public ResponseEntity<List<TagItemVO>> findTagItemsByTagId(@PathVariable long tagId) {
        List<TagItemVO> tagItems = tagService.findTagItemsByTagId(tagId);
        return ResponseEntity.ok(tagItems);
    }

    @Operation(summary = "아이템에 속한 태그 조회")
    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<TagItemVO>> findTagItemsByItemId(@PathVariable long itemId) {
        List<TagItemVO> tagItems = tagService.findTagItemsByItemId(itemId);
        return ResponseEntity.ok(tagItems);
    }

    @Operation(summary = "멤버가 좋아하는 태그 조회")
    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @GetMapping("/member")
    public ResponseEntity<List<TagMemberVO>> findTagMembersByMemberId() {
        List<TagMemberVO> tagItems = tagService.findTagMembersByMemberId(httpRequest.getCurrentMember().getId());
        return ResponseEntity.ok(tagItems);
    }

    @PostMapping("/member")
    public ResponseEntity<Void> createTagMember(TagMemberRequest request) {
        tagService.createTagMember(request, httpRequest.getCurrentMember());
        return ResponseEntity.ok().build();
    }
}
