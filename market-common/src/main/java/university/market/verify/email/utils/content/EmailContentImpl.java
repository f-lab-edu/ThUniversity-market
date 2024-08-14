package university.market.verify.email.utils.content;

import org.springframework.stereotype.Component;

@Component
public class EmailContentImpl implements EmailContent {
    @Override
    public String buildVerificationEmailContent(String verificationCode) {
        return "회원가입 인증 번호입니다.<br><br>" +
                "인증 번호는 " + verificationCode + "입니다.<br>" +
                "인증번호를 정확히 입력해주세요";
    }

    @Override
    public String buildVerificationEmailTitle() {
        return "회원 가입 인증 이메일 입니다.";
    }
}
