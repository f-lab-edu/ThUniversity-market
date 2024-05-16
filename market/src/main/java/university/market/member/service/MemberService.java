package university.market.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import university.market.member.domain.MemberVO;
import university.market.member.exception.LoginException;
import university.market.member.exception.LoginExceptionType;
import university.market.member.service.dto.request.JoinRequest;
import university.market.member.mapper.MemberMapper;
import university.market.member.service.dto.request.LoginRequest;
import university.market.member.service.dto.response.LoginResponse;
import university.market.member.utils.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public boolean joinMember(JoinRequest joinRequest) {
        if(memberMapper.findMemberByEmail(joinRequest.email()) != null) {
            throw new LoginException(LoginExceptionType.ALREADY_EXISTED_MEMBER);
        }

        MemberVO memberVO = MemberVO.builder()
                .name(joinRequest.name())
                .email(joinRequest.email())
                .password(passwordEncoder.encode(joinRequest.password()))
                .university(joinRequest.university())
                .build();

        return memberMapper.joinMember(memberVO);
    }

    @Transactional(readOnly = true)
    public LoginResponse loginMember(LoginRequest loginRequest) {
        MemberVO member = memberMapper.findMemberByEmail(loginRequest.email());
        if (member == null || !passwordEncoder.matches(loginRequest.password(), member.getPassword())) {
            throw new LoginException(LoginExceptionType.INVALID_LOGIN_CREDENTIALS);
        }

        return LoginResponse.builder()
                .memberId(member.getId())
                .token(jwtTokenProvider.generateToken(member.getEmail()))
                .build();
    }
}
