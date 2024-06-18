package university.market.dibs.exception;

import university.market.base.exception.BaseException;
import university.market.base.exception.BaseExceptionType;

public class DibsException extends BaseException {
    private final DibsExceptionType exceptionType;

    public DibsException(final DibsExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
