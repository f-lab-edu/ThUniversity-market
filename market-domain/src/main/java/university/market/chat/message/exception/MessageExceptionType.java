package university.market.chat.message.exception;

import org.springframework.http.HttpStatus;
import university.market.base.exception.BaseExceptionType;

public enum MessageExceptionType implements BaseExceptionType {
    SOCKET_NOT_CONNECTED(HttpStatus.BAD_REQUEST, "소켓 연결이 되어있지 않습니다.", 400300),
    NOT_SEND_MESSAGE(HttpStatus.BAD_REQUEST, "메시지를 보낼 수 없습니다.", 400301);

    private final HttpStatus httpStatus;
    private final String errorMessage;
    private final int errorCode;

    MessageExceptionType(final HttpStatus httpStatus, final String errorMessage, final int errorCode) {
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
