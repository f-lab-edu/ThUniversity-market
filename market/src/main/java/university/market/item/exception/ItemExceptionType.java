package university.market.item.exception;

import org.springframework.http.HttpStatus;
import university.market.base.exception.BaseExceptionType;

public enum ItemExceptionType implements BaseExceptionType {
    INVALID_ITEM(HttpStatus.NOT_FOUND, "존재하지 않는 아이템입니다.", 404201);
    private final HttpStatus httpStatus;
    private final String errorMessage;
    private final int errorCode;

    ItemExceptionType(final HttpStatus httpStatus, final String errorMessage, int errorCode) {
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
