package university.market.verify.email.exception;

import university.market.base.exception.BaseException;
import university.market.base.exception.BaseExceptionType;

public class EmailException extends BaseException {

    private final EmailExceptionType exceptionType;

    public EmailException(final EmailExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }


    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
