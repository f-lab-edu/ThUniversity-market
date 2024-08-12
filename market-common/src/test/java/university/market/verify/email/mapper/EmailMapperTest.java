package university.market.verify.email.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import university.market.verify.email.domain.EmailVO;

@MybatisTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class EmailMapperTest {
    @Autowired
    private EmailMapper emailMapper;

    private EmailVO email;

    @BeforeEach
    public void init() {
        // given
        String verificationCode = "123abc";
        email = EmailVO.builder()
                .email("test@example.com")
                .verificationCode(verificationCode)
                .build();
    }

    @Test
    @DisplayName("[success] emailV0 저장후 찾기 성공")
    public void emailVO_저장후_찾기_성공() {
        // when
        emailMapper.saveVerificationCode(email);

        // then
        EmailVO retrievedEmailVO = emailMapper.findEmailToVerification(email.getEmail());
        Assertions.assertNotNull(retrievedEmailVO);
        Assertions.assertEquals(email.getEmail(), retrievedEmailVO.getEmail());
        Assertions.assertEquals(email.getVerificationCode(), retrievedEmailVO.getVerificationCode());
    }
}
