package university.market.verify.email.utils.content;

public interface EmailContent {
    String buildVerificationEmailContent(String verificationCode);
    String buildVerificationEmailTitle();
}
