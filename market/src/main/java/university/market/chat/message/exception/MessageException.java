package university.market.chat.message.exception;

import university.market.base.exception.BaseException;
import university.market.base.exception.BaseExceptionType;

public class MessageException extends BaseException {
    private final MessageExceptionType exceptionType;

    public MessageException(final MessageExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
