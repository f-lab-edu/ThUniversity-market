package university.market.verify.email.service;

import university.market.verify.email.service.dto.CheckVerificationCodeRequest;
import university.market.verify.email.service.dto.EmailRequest;

public interface EmailVerificationService {
    void sendVerificationCodeByEmail(EmailRequest emailRequest);
    void checkVerificationCode(CheckVerificationCodeRequest checkVerificationCodeRequest);
}
