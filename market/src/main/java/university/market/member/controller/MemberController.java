package university.market.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import university.market.member.annotation.AuthCheck;
import university.market.member.domain.auth.AuthType;
import university.market.member.service.MemberService;
import university.market.member.service.dto.request.JoinRequest;
import university.market.member.service.dto.request.LoginRequest;
import university.market.member.service.dto.response.LoginResponse;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<Void> joinMember(@RequestBody JoinRequest joinRequest) {
        memberService.joinMember(joinRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginMember(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = memberService.loginMember(loginRequest);
        return ResponseEntity.ok(response);
    }

    @AuthCheck(AuthType.ROLE_ADMIN)
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @AuthCheck({AuthType.ROLE_ADMIN, AuthType.ROLE_USER})
    @DeleteMapping("/")
    public ResponseEntity<Void> deleteMyself(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        memberService.deleteMyself(token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
