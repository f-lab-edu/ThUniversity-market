package university.market.member.exception;

import university.market.base.exception.BaseException;
import university.market.base.exception.BaseExceptionType;

public class MemberException extends BaseException {
    private final MemberExceptionType exceptionType;

    public MemberException(final MemberExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
