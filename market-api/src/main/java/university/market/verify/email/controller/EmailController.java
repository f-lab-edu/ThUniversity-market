package university.market.verify.email.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import university.market.member.annotation.AuthCheck;
import university.market.member.domain.auth.AuthType;
import university.market.verify.email.service.EmailVerificationService;
import university.market.verify.email.service.dto.CheckVerificationCodeRequest;
import university.market.verify.email.service.dto.EmailRequest;

@Tag(name = "Email", description = "이메일 인증 관련 API")
@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailVerificationService emailVerificationService;

    @Operation(summary = "이메일 인증 코드 전송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "클라이언트 에러", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json")),
    })
    @AuthCheck(AuthType.ROLE_USER)
    @PostMapping("/")
    public ResponseEntity<Void> mailSend(@RequestBody EmailRequest request) {
        emailVerificationService.sendVerificationCodeByEmail(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "이메일 인증 코드 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "클라이언트 에러", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json")),
    })
    @AuthCheck(AuthType.ROLE_USER)
    @PostMapping("/check")
    public ResponseEntity<Void> verificationCheck(@RequestBody CheckVerificationCodeRequest request) {
        emailVerificationService.checkVerificationCode(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
