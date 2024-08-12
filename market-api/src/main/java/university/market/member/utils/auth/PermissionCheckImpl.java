package university.market.member.utils.auth;

import org.springframework.stereotype.Component;
import university.market.base.exception.BaseException;
import university.market.member.exception.MemberException;
import university.market.member.exception.MemberExceptionType;

@Component
public class PermissionCheckImpl implements PermissionCheck {
    @Override
    public void hasPermission(CheckTemplate checkTemplate) {
        if (checkTemplate.checkAuth()) {
            throw new MemberException(MemberExceptionType.UNAUTHORIZED_PERMISSION);
        }
    }

    @Override
    public void hasPermission(CheckTemplate checkTemplate, BaseException exception) {
        if (checkTemplate.checkAuth()) {
            throw exception;
        }
    }
}
