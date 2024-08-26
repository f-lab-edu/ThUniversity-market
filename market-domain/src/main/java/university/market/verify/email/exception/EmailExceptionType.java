package university.market.verify.email.exception;

import org.springframework.http.HttpStatus;
import university.market.base.exception.BaseExceptionType;

public enum EmailExceptionType implements BaseExceptionType {
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송 에러", 500001),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "데이터베이스에 이메일이 존재하지 않습니다.", 404002),
    INVALID_VERIFICATION_CODE(HttpStatus.UNAUTHORIZED,"유효하지 않는 인증번호 입니다.", 401003);
    private final HttpStatus httpStatus;
    private final String errorMessage;
    private final int errorCode;

    EmailExceptionType(final HttpStatus httpStatus, final String errorMessage, final int errorCode) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }

    public int errorCode() {
        return errorCode;
    }
}
