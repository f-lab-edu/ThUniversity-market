package university.market.offer.exception;

import org.springframework.http.HttpStatus;
import university.market.base.exception.BaseExceptionType;

public enum OfferExceptionType implements BaseExceptionType {
    OFFER_NOT_FOUND(HttpStatus.NOT_FOUND, "오퍼를 찾을 수 없습니다.", 404001);

    private final HttpStatus httpStatus;
    private final String errorMessage;
    private final int errorCode;

    OfferExceptionType(final HttpStatus httpStatus, final String errorMessage, final int errorCode) {
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
