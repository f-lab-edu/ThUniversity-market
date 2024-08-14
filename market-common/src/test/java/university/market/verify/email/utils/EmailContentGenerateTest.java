package university.market.verify.email.utils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import university.market.verify.email.utils.content.EmailContent;

@ExtendWith(MockitoExtension.class)
public class EmailContentGenerateTest {

    @Mock
    EmailContent emailContent;

    @Test
    @DisplayName("[success] 인증번호 적용 이메일 내용 생성")
    public void 인증번호_적용_이메일_내용_생성() {
        // given
        String verificationCode = "123abc";
        String completedContent = "회원가입 인증 번호입니다.<br><br>" +
                "인증 번호는 123abc입니다.<br>" +
                "인증번호를 정확히 입력해주세요";

        // mocking
        when(emailContent.buildVerificationEmailContent(verificationCode)).thenReturn(completedContent);

        // when
        final String generateContent = emailContent.buildVerificationEmailContent(verificationCode);

        // then
        assertThat(generateContent).isEqualTo(generateContent);
    }
}
