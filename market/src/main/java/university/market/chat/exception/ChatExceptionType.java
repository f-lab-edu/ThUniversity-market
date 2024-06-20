package university.market.chat.exception;

import org.springframework.http.HttpStatus;
import university.market.base.exception.BaseExceptionType;

public enum ChatExceptionType implements BaseExceptionType {
    NOT_EXISTED_CHAT_MEMBER(HttpStatus.NOT_FOUND, "채팅방에 참여하지 않은 유저입니다.", 404503);
    private final HttpStatus httpStatus;
    private final String errorMessage;
    private final int errorCode;

    ChatExceptionType(final HttpStatus httpStatus, final String errorMessage, final int errorCode) {
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
