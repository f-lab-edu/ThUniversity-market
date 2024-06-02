package university.market.email.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CheckVerificationCodeRequest(
        @Email
        @NotEmpty
        String email,

        @NotNull
        String verificationCode
) {
}
