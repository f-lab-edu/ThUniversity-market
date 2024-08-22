package university.market.tag.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import university.market.member.annotation.AuthCheck;
import university.market.member.domain.auth.AuthType;
import university.market.tag.domain.TagItemVO;
import university.market.tag.domain.TagMemberVO;
import university.market.tag.service.TagService;

@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> deleteTag(@PathVariable long tagId) {
        tagService.deleteTag(tagId);
        return ResponseEntity.ok().build();
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @DeleteMapping("/item/{tagItemId}")
    public ResponseEntity<Void> deleteTagItem(@PathVariable long tagItemId) {
        tagService.deleteTagItem(tagItemId);
        return ResponseEntity.ok().build();
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @DeleteMapping("/member/{tagMemberId}")
    public ResponseEntity<Void> deleteTagMember(@PathVariable long tagMemberId) {
        tagService.deleteTagMember(tagMemberId);
        return ResponseEntity.ok().build();
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @GetMapping("/item/{tagId}")
    public ResponseEntity<List<TagItemVO>> findTagItemsByTagId(@PathVariable long tagId) {
        List<TagItemVO> tagItems = tagService.findTagItemsByTagId(tagId);
        return ResponseEntity.ok(tagItems);
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<TagItemVO>> findTagItemsByItemId(@PathVariable long itemId) {
        List<TagItemVO> tagItems = tagService.findTagItemsByItemId(itemId);
        return ResponseEntity.ok(tagItems);
    }

    @AuthCheck({AuthType.ROLE_VERIFY_USER, AuthType.ROLE_ADMIN})
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<TagMemberVO>> findTagMembersByMemberId(@PathVariable long memberId) {
        List<TagMemberVO> tagItems = tagService.findTagMembersByMemberId(memberId);
        return ResponseEntity.ok(tagItems);
    }
}
