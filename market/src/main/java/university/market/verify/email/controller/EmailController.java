package university.market.verify.email.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import university.market.verify.email.service.EmailVerificationService;
import university.market.verify.email.service.dto.CheckVerificationCodeRequest;
import university.market.verify.email.service.dto.EmailRequest;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/")
    public ResponseEntity<Void> mailSend(@RequestBody EmailRequest request) {
        emailVerificationService.sendVerificationCodeByEmail(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/check")
    public ResponseEntity<Void> verificationCheck(@RequestBody CheckVerificationCodeRequest request) {
        emailVerificationService.checkVerificationCode(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
