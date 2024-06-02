package university.market.email.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import jakarta.mail.MessagingException;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import university.market.email.domain.EmailVO;
import university.market.email.exception.EmailException;
import university.market.email.exception.EmailExceptionType;
import university.market.email.mapper.EmailMapper;
import university.market.email.service.dto.CheckVerificationCodeRequest;
import university.market.email.service.dto.EmailRequest;
import university.market.email.utils.RandomUtil;
import university.market.email.utils.content.EmailContent;

import jakarta.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private EmailMapper emailMapper;

    @Mock
    private EmailContent emailContent;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field usernameField = EmailServiceImpl.class.getDeclaredField("username");
        usernameField.setAccessible(true);
        usernameField.set(emailService, "test@example.com");
    }

    @Test
    @DisplayName("[success] 이메일 인증 번호 전송 성공")
    public void 이메일_인증_번호_전송_성공() throws MessagingException {
        // given
        EmailRequest emailRequest = new EmailRequest("test@example.com");
        String verificationCode = RandomUtil.generateRandomCode('0', 'z', 6);

        // mocking
        when(emailContent.buildVerificationEmailTitle()).thenReturn("Verification Code");
        when(emailContent.buildVerificationEmailContent(anyString())).thenReturn("Your verification code is: " + verificationCode);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(javaMailSender).send(mimeMessage);

        // when
        emailService.sendVerificationCodeByEmail(emailRequest);

        // then
        verify(javaMailSender, times(1)).send(mimeMessage);
        verify(emailMapper, times(1)).saveVerificationCode(any(EmailVO.class));
    }

    @Test
    @DisplayName("[fail] 이메일 인증 번호 전송 실패")
    public void 이메일_인증_번호_전송_실패() throws Exception {
        // given
        EmailRequest emailRequest = new EmailRequest("test@example.com");
        String verificationCode = RandomUtil.generateRandomCode('0', 'z', 6);

        // mocking
        when(emailContent.buildVerificationEmailTitle()).thenReturn("Verification Code");
        when(emailContent.buildVerificationEmailContent(anyString())).thenReturn("Your verification code is: " + verificationCode);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new EmailException(EmailExceptionType.EMAIL_SEND_FAILED)).when(javaMailSender).send(mimeMessage);

        // when, then
        try {
            emailService.sendVerificationCodeByEmail(emailRequest);
        } catch (EmailException e) {
            assert e.exceptionType() == EmailExceptionType.EMAIL_SEND_FAILED;
        }

        verify(javaMailSender, times(1)).send(mimeMessage);
        verify(emailMapper, times(0)).saveVerificationCode(any(EmailVO.class));
    }


    @Test
    @DisplayName("[success] 이메일 인증 번호 검증 성공")
    public void 이메일_인증_번호_검증_성공() {
        // given
        String email = "test@example.com";
        String verificationCode = "123456";
        CheckVerificationCodeRequest request = new CheckVerificationCodeRequest(email, verificationCode);
        EmailVO emailVO = EmailVO.builder()
                .email(email)
                .verificationCode(verificationCode)
                .build();

        // mocking
        when(emailMapper.findEmailToVerification(email)).thenReturn(emailVO);

        // when
        emailService.checkVerificationCode(request);

        // then
        verify(emailMapper, times(1)).findEmailToVerification(email);
    }

    @Test
    @DisplayName("[fail] 이메일 인증 번호 검증 인증 코드 불일치")
    public void 이메일_인증_번호_검증_인증_코드_불일치() {
        // given
        String email = "test@example.com";
        String verificationCode = "123456";
        CheckVerificationCodeRequest request = new CheckVerificationCodeRequest(email, verificationCode);
        EmailVO emailVO = EmailVO.builder()
                .email(email)
                .verificationCode("654321")
                .build();

        // mocking
        when(emailMapper.findEmailToVerification(email)).thenReturn(emailVO);

        // when, then
        try {
            emailService.checkVerificationCode(request);
        } catch (EmailException e) {
            assert e.exceptionType() == EmailExceptionType.INVALID_VERIFICATION_CODE;
        }

        verify(emailMapper, times(1)).findEmailToVerification(email);
    }
}
