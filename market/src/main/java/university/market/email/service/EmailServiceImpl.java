package university.market.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import university.market.email.domain.EmailVO;
import university.market.email.exception.EmailException;
import university.market.email.exception.EmailExceptionType;
import university.market.email.mapper.EmailMapper;
import university.market.email.service.dto.CheckVerificationCodeRequest;
import university.market.email.service.dto.EmailRequest;
import university.market.email.utils.RandomUtil;
import university.market.email.utils.content.EmailContent;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;
    private final EmailMapper emailMapper;
    private final EmailContent emailContent;

    @Value("${mail.username}")
    private String username;



    @Override
    public void sendVerificationCodeByEmail(EmailRequest emailRequest) {
        String verificationCode = RandomUtil.generateRandomCode('0','z',6);
        String setFrom = this.username;
        String toMail = emailRequest.email();
        String title = emailContent.buildVerificationEmailTitle();
        String content = emailContent.buildVerificationEmailContent(verificationCode);
        mailSend(setFrom, toMail, title, content, verificationCode);
    }

    @Override
    @Transactional(readOnly = true)
    public void checkVerificationCode(CheckVerificationCodeRequest request) {
        EmailVO findEmail = emailMapper.findEmailToVerification(request.email());
        if (findEmail == null) {
            throw new EmailException(EmailExceptionType.EMAIL_NOT_FOUND);
        } else if (!Objects.equals(findEmail.getVerificationCode(), request.verificationCode())) {
            throw new EmailException(EmailExceptionType.INVALID_VERIFICATION_CODE);
        }
    }

    @Transactional
    protected void saveVerificationCode(String email, String verificationCode) {
        emailMapper.saveVerificationCode(
                EmailVO.builder()
                        .email(email)
                        .verificationCode(verificationCode)
                        .build()
        );
    }

    private void mailSend(String setFrom, String toMail, String title, String content, String verificationCode) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");
            helper.setFrom(setFrom);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content,true);
            javaMailSender.send(message);
            saveVerificationCode(toMail, verificationCode);
        } catch (MessagingException e) {
            throw new EmailException(EmailExceptionType.EMAIL_SEND_FAILED);
        }
    }
}
