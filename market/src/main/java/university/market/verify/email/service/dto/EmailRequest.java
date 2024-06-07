package university.market.verify.email.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record EmailRequest(
        @Email
        @NotEmpty
        String email
) {}
