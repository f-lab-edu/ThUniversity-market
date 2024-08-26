package university.market.utils.auth;

import university.market.base.exception.BaseException;

public interface PermissionCheck {
    void hasPermission(CheckTemplate checkTemplate);

    void hasPermission(CheckTemplate checkTemplate, BaseException exception);
}
