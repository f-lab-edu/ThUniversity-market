package university.market.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import university.market.member.annotation.AuthCheck;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.exception.MemberException;
import university.market.member.exception.MemberExceptionType;
import university.market.member.mapper.MemberMapper;
import university.market.member.service.dto.request.JoinRequest;
import university.market.member.service.dto.request.LoginRequest;
import university.market.member.service.dto.response.LoginResponse;
import university.market.member.utils.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void joinMember(JoinRequest joinRequest) {
        joinUser(joinRequest, AuthType.ROLE_USER);
    }

    @Transactional
    public void joinAdminUser(JoinRequest joinRequest) {
        joinUser(joinRequest, AuthType.ROLE_ADMIN);
    }

    private void joinUser(JoinRequest joinRequest, AuthType authType) {
        if (memberMapper.findMemberByEmail(joinRequest.email()) != null) {
            throw new MemberException(MemberExceptionType.ALREADY_EXISTED_MEMBER);
        }

        final MemberVO memberVO = MemberVO.builder()
                .name(joinRequest.name())
                .email(joinRequest.email())
                .password(passwordEncoder.encode(joinRequest.password()))
                .university(joinRequest.university())
                .auth(authType)
                .build();

        try {
            memberMapper.joinMember(memberVO);
        } catch (Exception e) {
            throw new MemberException(MemberExceptionType.DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public LoginResponse loginMember(LoginRequest loginRequest) {
        final MemberVO member = memberMapper.findMemberByEmail(loginRequest.email());
        if (member == null || !passwordEncoder.matches(loginRequest.password(), member.getPassword())) {
            throw new MemberException(MemberExceptionType.INVALID_LOGIN_CREDENTIALS);
        }

        return LoginResponse.builder()
                .memberId(member.getId())
                .token(jwtTokenProvider.generateToken(member.getEmail()))
                .build();
    }

    @Transactional(readOnly = true)
    public MemberVO findMemberByEmail(String email) {
        final MemberVO member = memberMapper.findMemberByEmail(email);
        return member;
    }

    @AuthCheck(AuthType.ROLE_ADMIN)
    @Transactional
    public void deleteMember(Long id) {
        memberMapper.deleteMemberById(id);
    }

    @AuthCheck({AuthType.ROLE_ADMIN, AuthType.ROLE_USER})
    @Transactional
    public void deleteMyself(String token) {
        memberMapper.deleteMemberByEmail(jwtTokenProvider.extractEmail(token));
    }
}
