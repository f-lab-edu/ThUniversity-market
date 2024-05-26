package university.market.member.annotation.aspect;

import jakarta.servlet.http.HttpServletRequest;
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
import university.market.member.exception.MemberException;
import university.market.member.exception.MemberExceptionType;
import university.market.member.mapper.MemberMapper;
import university.market.member.utils.JwtTokenProvider;


@Aspect
@Component
public class AuthCheckAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Before("@annotation(university.market.member.annotation.AuthCheck)")
    public void checkAuth(JoinPoint joinPoint) {
        MemberVO currentUser = getCurrentUser();

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

    private MemberVO getCurrentUser() {
        String token = getTokenFromRequest();
        String userId = jwtTokenProvider.extractEmail(token);
        return memberMapper.findMemberByEmail(userId);
    }

    private String getTokenFromRequest() {
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            throw new MemberException(MemberExceptionType.INVALID_ACCESS_TOKEN);
        }
        return token.substring(7);
    }
}

