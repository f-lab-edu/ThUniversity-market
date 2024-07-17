package university.market.chat.room.exception;

import university.market.base.exception.BaseException;
import university.market.base.exception.BaseExceptionType;

public class ChatException extends BaseException {
    private final ChatExceptionType exceptionType;

    public ChatException(final ChatExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
