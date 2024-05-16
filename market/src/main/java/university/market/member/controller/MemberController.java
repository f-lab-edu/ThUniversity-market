package university.market.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<Boolean> joinMember(@RequestBody JoinRequest joinRequest) {
        Boolean loginState = memberService.joinMember(joinRequest);
        return ResponseEntity.ok(loginState);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginMember(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = memberService.loginMember(loginRequest);;
        return ResponseEntity.ok(response);
    }
}
