package university.market.member.service;


import university.market.member.domain.MemberVO;
import university.market.member.service.dto.request.JoinRequest;
import university.market.member.service.dto.request.LoginRequest;
import university.market.member.service.dto.response.LoginResponse;
import university.market.verify.email.service.dto.CheckVerificationCodeRequest;

public interface MemberService {
    void joinMember(JoinRequest joinRequest);

    void joinAdminUser(JoinRequest joinRequest);

    LoginResponse loginMember(LoginRequest loginRequest);

    MemberVO findMemberByEmail(String email);

    void deleteMember(Long id);

    void deleteMyself(String token);

    void verifyEmailUser(CheckVerificationCodeRequest checkVerificationCodeRequest);
}
