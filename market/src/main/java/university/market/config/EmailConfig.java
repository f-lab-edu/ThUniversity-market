package university.market.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.transport.protocol}")
    private String transportProtocol;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;

    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private int timeout;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean starttlsEnable;

    @Value("${spring.mail.properties.mail.smtp.ssl.protocols}")
    private String sslProtocols;

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.transport.protocol", transportProtocol);
        javaMailProperties.put("mail.smtp.auth", auth);
        javaMailProperties.put("mail.smtp.timeout", timeout);
        javaMailProperties.put("mail.smtp.starttls.enable", starttlsEnable);
        javaMailProperties.put("mail.smtp.ssl.trust", host);
        javaMailProperties.put("mail.smtp.ssl.protocols", sslProtocols);

        mailSender.setJavaMailProperties(javaMailProperties);

        return mailSender;
    }
}
