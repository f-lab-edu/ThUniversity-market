package university.market.member.utils;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import lombok.extern.slf4j.Slf4j;
import university.market.member.exception.MemberException;
import university.market.member.exception.MemberExceptionType;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class TokenGenerateTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("[success] login success generate token")
    void generateToken_login_success_generate_token() {
        // given
        final String memberEmail = "test@test";
        final String token = "testToken";

        // mocking
        when(jwtTokenProvider.generateToken(memberEmail)).thenReturn(token);
        when(jwtTokenProvider.extractEmail(token)).thenReturn(memberEmail);

        // when
        final String generatedToken = jwtTokenProvider.generateToken(memberEmail);
        final String extractedMemberEmail = jwtTokenProvider.extractEmail(generatedToken);

        // then
        assertThat(extractedMemberEmail).isEqualTo(memberEmail);
        log.info("Access Token : {}", generatedToken);
    }

    @Test
    @DisplayName("[fail] token expired")
    void token_expired_not_valid() {
        // given
        final String memberEmail = "test@test";
        final String token = "expiredToken";
        final LocalDateTime now = LocalDateTime.now();
        final long expireTime = 1000L;

        ExpireDateSupplier expireDateSupplier = () -> Date.from(LocalDateTime.from(now.minusSeconds(expireTime))
                .atZone(ZoneId.systemDefault()).toInstant());

        // mocking
        when(jwtTokenProvider.generateToken(memberEmail, expireDateSupplier)).thenReturn(token);
        doThrow(new MemberException(MemberExceptionType.EXPIRED_ACCESS_TOKEN)).when(jwtTokenProvider).validateToken(token);

        // when
        final String generatedToken = jwtTokenProvider.generateToken(memberEmail, expireDateSupplier);

        // then
        assertThatCode(() -> jwtTokenProvider.validateToken(generatedToken))
                .isInstanceOf(MemberException.class)
                .hasMessage("만료된 Access Token입니다.");
    }
}
