package university.market.dibs.exception;

import org.springframework.http.HttpStatus;
import university.market.base.exception.BaseExceptionType;

public enum DibsExceptionType implements BaseExceptionType {
    NO_DIBS_MYSELF(HttpStatus.BAD_REQUEST, "자신의 상품을 찜할 수 없습니다.", 400401);

    private final HttpStatus httpStatus;
    private final String errorMessage;
    private final int errorCode;

    DibsExceptionType(final HttpStatus httpStatus, final String errorMessage, int errorCode) {
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

    @Override
    public int errorCode() {
        return errorCode;
    }
}
