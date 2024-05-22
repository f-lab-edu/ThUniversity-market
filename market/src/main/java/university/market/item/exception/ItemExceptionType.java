package university.market.item.exception;

import org.springframework.http.HttpStatus;
import university.market.base.exception.BaseExceptionType;

public enum ItemExceptionType implements BaseExceptionType {
    INVALID_ITEM(HttpStatus.NOT_FOUND, "존재하지 않는 아이템입니다.");
    private final HttpStatus httpStatus;
    private final String errorMessage;

    ItemExceptionType(final HttpStatus httpStatus, final String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }
}
