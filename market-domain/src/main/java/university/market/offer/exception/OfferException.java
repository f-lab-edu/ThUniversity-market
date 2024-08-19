package university.market.offer.exception;

import university.market.base.exception.BaseException;
import university.market.base.exception.BaseExceptionType;

public class OfferException extends BaseException {

    private final OfferExceptionType exceptionType;
    public OfferException(final OfferExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
