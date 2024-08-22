package university.market.tag.exception;

import university.market.base.exception.BaseException;
import university.market.base.exception.BaseExceptionType;

public class TagException extends BaseException {

    private final TagExceptionType exceptionType;

    public TagException(final TagExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
