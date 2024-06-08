package university.market.member.exception;

import org.springframework.http.HttpStatus;
import university.market.base.exception.BaseExceptionType;

public enum MemberExceptionType implements BaseExceptionType {
    ALREADY_EXISTED_MEMBER(HttpStatus.CONFLICT, "존재하는 유저입니다.", 409100),
    INVALID_LOGIN_CREDENTIALS(HttpStatus.UNAUTHORIZED, "유효하지 않은 로그인 정보입니다.", 401101),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 Access Token입니다.", 401102),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Access Token입니다.", 401103),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다.", 404104),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다.", 500105),
    SESSION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "세션 오류가 발생했습니다.", 500106),
    INVALID_CASTING(HttpStatus.INTERNAL_SERVER_ERROR, "유효하지 않은 형변환입니다.", 500107);

    private final HttpStatus httpStatus;
    private final String errorMessage;
    private final int errorCode;

    MemberExceptionType(final HttpStatus httpStatus, final String errorMessage, final int errorCode) {
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

    public int errorCode() {
        return errorCode;
    }
}
