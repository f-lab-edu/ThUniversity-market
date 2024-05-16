package university.market.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import university.market.member.domain.MemberVO;
import university.market.member.exception.LoginException;
import university.market.member.exception.LoginExceptionType;
import university.market.member.service.dto.request.JoinRequest;
import university.market.member.mapper.MemberMapper;
import university.market.member.service.dto.request.LoginRequest;
import university.market.member.utils.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public boolean joinMember(JoinRequest joinRequest) {
        if(memberMapper.findMemberByEmail(joinRequest.getEmail()) != null) {
            throw new LoginException(LoginExceptionType.ALREADY_EXISTED_MEMBER);
        }

        MemberVO memberVO = MemberVO.builder()
                .name(joinRequest.getName())
                .email(joinRequest.getEmail())
                .password(joinRequest.getPassword())
                .university(joinRequest.getUniversity())
                .build();

        return memberMapper.joinMember(memberVO);
    }

    @Transactional(readOnly = true)
    public String loginMember(LoginRequest loginRequest) {
        MemberVO member = memberMapper.loginMember(loginRequest.getEmail(), loginRequest.getPassword());
        if (member == null) {
            throw new LoginException(LoginExceptionType.INVALID_LOGIN_CREDENTIALS);
        }
        return jwtTokenProvider.generateToken(member.getEmail());
    }
}
