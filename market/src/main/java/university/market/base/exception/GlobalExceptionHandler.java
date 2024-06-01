package university.market.base.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import university.market.base.exception.response.ErrorResponse;
import university.market.member.exception.MemberException;
import university.market.member.exception.MemberExceptionType;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ErrorResponse> handleMemberException(MemberException ex) {
        HttpStatus originalStatus = ex.exceptionType().httpStatus();
        HttpStatus statusToReturn;

        switch (originalStatus.series()) {
            case CLIENT_ERROR:
                statusToReturn = HttpStatus.BAD_REQUEST;
                break;
            case SERVER_ERROR:
                statusToReturn = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
            default:
                statusToReturn = originalStatus;
                break;
        }

        return ResponseEntity
                .status(statusToReturn)
                .body(new ErrorResponse(
                        ex.exceptionType().errorMessage(),
                        ex.exceptionType().errorCode(),
                        true));
    }
}
