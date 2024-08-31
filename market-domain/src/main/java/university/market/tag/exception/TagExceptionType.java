package university.market.tag.exception;

import org.springframework.http.HttpStatus;
import university.market.base.exception.BaseExceptionType;

public enum TagExceptionType implements BaseExceptionType {
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "태그를 찾을 수 없습니다", 404900),
    ALREADY_EXIST_TAG_MEMBER(HttpStatus.BAD_REQUEST, "이미 태그 멤버로 등록되어 있습니다.", 400901),
    NOT_EXIST_TAG_MEMBER(HttpStatus.NOT_FOUND, "태그 멤버를 찾을 수 없습니다.", 404902),
    NOT_EXIST_TAG_ITEM(HttpStatus.NOT_FOUND, "태그 아이템을 찾을 수 없습니다.", 404903),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 에러", 500904);

    private final HttpStatus httpStatus;
    private final String errorMessage;
    private final int errorCode;

    TagExceptionType(final HttpStatus httpStatus, final String errorMessage, final int errorCode) {
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
