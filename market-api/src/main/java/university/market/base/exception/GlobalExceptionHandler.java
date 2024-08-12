package university.market.base.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import university.market.base.exception.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    Logger defaultLog = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    Logger exceptionLog = LoggerFactory.getLogger("ExceptionLogger");

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleException(BaseException ex) {
        defaultLog.error(ex.getMessage());
        exceptionLog.error(ex.getMessage());
        return createErrorResponse(ex.exceptionType().httpStatus(), ex.exceptionType().errorMessage(),
                ex.exceptionType().errorCode());
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus originalStatus, String errorMessage,
                                                              int errorCode) {
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception e) {
        defaultLog.error(e.getMessage());
        exceptionLog.error(e.getMessage(), e);
        return ResponseEntity.internalServerError().build();
    }
}
