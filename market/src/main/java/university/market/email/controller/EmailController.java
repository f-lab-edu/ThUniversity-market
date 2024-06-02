package university.market.email.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import university.market.email.service.EmailService;
import university.market.email.service.dto.CheckVerificationCodeRequest;
import university.market.email.service.dto.EmailRequest;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/")
    public ResponseEntity<Void> mailSend(@RequestBody EmailRequest request) {
        emailService.sendVerificationCodeByEmail(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/check")
    public ResponseEntity<Void> verificationCheck(@RequestBody CheckVerificationCodeRequest request) {
        emailService.checkVerificationCode(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
