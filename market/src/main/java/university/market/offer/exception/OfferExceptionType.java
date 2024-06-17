package university.market.offer.exception;

import org.springframework.http.HttpStatus;
import university.market.base.exception.BaseExceptionType;

public enum OfferExceptionType implements BaseExceptionType {
    NO_OFFER_MYSELF(HttpStatus.BAD_REQUEST, "자신에게 오퍼를 보낼 수 없습니다.", 400002);

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
