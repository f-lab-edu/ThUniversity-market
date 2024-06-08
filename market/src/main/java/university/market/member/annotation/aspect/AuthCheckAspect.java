package university.market.member.annotation.aspect;

import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import university.market.member.annotation.AuthCheck;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.utils.http.HttpRequest;

@Aspect
@Component
public class AuthCheckAspect {

    @Autowired
    private HttpRequest httpRequest;

    @Before("@annotation(university.market.member.annotation.AuthCheck)")
    public void checkAuth(JoinPoint joinPoint) {
        MemberVO currentUser = httpRequest.getMemberSession();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AuthCheck authCheck = method.getAnnotation(AuthCheck.class);

        AuthType[] requiredAuths = authCheck.value();
        boolean hasRequiredAuth = false;

        for (AuthType requiredAuth : requiredAuths) {
            if (currentUser.getAuth().compareTo(requiredAuth) >= 0) {
                hasRequiredAuth = true;
                break;
            }
        }

        if (!hasRequiredAuth) {
            throw new SecurityException("권한 부족");
        }
    }
}

