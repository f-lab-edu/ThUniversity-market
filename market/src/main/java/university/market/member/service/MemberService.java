package university.market.member.service;


import university.market.member.service.dto.request.JoinRequest;
import university.market.member.service.dto.request.LoginRequest;
import university.market.member.service.dto.response.LoginResponse;

public interface MemberService {
    public boolean joinMember(JoinRequest joinRequest);
    public LoginResponse loginMember(LoginRequest loginRequest);
}
