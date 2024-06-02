package university.market.email.service;

import university.market.email.service.dto.CheckVerificationCodeRequest;
import university.market.email.service.dto.EmailRequest;

public interface EmailService {
    void sendVerificationCodeByEmail(EmailRequest emailRequest);
    void checkVerificationCode(CheckVerificationCodeRequest checkVerificationCodeRequest);
}
