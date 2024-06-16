package university.market.item.exception;

import university.market.base.exception.BaseException;
import university.market.base.exception.BaseExceptionType;

public class ItemException extends BaseException {
    private final ItemExceptionType exceptionType;

    public ItemException(final ItemExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
