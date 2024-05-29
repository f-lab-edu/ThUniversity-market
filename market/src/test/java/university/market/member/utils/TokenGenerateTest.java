package university.market.member.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import university.market.member.exception.MemberException;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
public class TokenGenerateTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("[success] login success generate token")
    void generateToken_login_success_generate_token() {
        //given
        final String memberEmail = "test@test";

        //when
        final String token = jwtTokenProvider.generateToken(memberEmail);
        final String extractedMemberEmail = jwtTokenProvider.extractEmail(token);

        //then
        assertThat(extractedMemberEmail).isEqualTo(memberEmail);
        log.info("Access Token : {}", token);
    }

    @Test
    @DisplayName("[fail] token expired")
    void token_expired_not_valid() throws InterruptedException {
        //given
        final String memberEmail = "test@test";

        //when
        final LocalDateTime now = LocalDateTime.now();
        final long expireTime = 1000L;

        ExpireDateSupplier expireDateSupplier = () -> Date.from(LocalDateTime.from(now.plusSeconds(expireTime/1000L)).atZone(
                ZoneId.systemDefault()).toInstant());
        final String token = jwtTokenProvider.generateToken(memberEmail, expireDateSupplier);

        Thread.sleep(expireTime + 1000L);
        // then

        assertThatThrownBy(() -> jwtTokenProvider.extractEmail(token))
                .isInstanceOf(MemberException.class)
                .hasMessage("만료된 Access Token입니다.");
    }
}
