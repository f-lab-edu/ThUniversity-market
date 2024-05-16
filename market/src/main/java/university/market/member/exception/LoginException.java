package university.market.member.exception;

import university.market.base.exception.BaseException;
import university.market.base.exception.BaseExceptionType;

public class LoginException extends BaseException {
    private final LoginExceptionType exceptionType;

    public LoginException(final LoginExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
