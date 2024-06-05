package university.market.base.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import university.market.base.exception.response.ErrorResponse;
import university.market.verify.email.exception.EmailException;
import university.market.member.exception.MemberException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MemberException.class, EmailException.class})
    public ResponseEntity<ErrorResponse> handleException(BaseException ex) {
        return createErrorResponse(ex.exceptionType().httpStatus(), ex.exceptionType().errorMessage(), ex.exceptionType().errorCode());
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus originalStatus, String errorMessage, int errorCode) {
        HttpStatus statusToReturn = determineHttpStatus(originalStatus);
        return ResponseEntity
                .status(statusToReturn)
                .body(new ErrorResponse(errorMessage, errorCode, true));
    }

    private HttpStatus determineHttpStatus(HttpStatus originalStatus) {
        return switch (originalStatus.series()) {
            case CLIENT_ERROR -> HttpStatus.BAD_REQUEST;
            case SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
            default -> originalStatus;
        };
    }
}
