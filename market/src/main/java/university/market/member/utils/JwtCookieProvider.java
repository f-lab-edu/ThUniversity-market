package university.market.member.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtCookieProvider {
    @Value("${security.cookie.expire-length}")
    private int expireTimeMilliSecond;
    public void addJwtTokenToCookie(HttpServletResponse response, String token){
        Cookie cookie = new Cookie("jwtToken", token);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true); // HTTPS 환경에서만 사용
        cookie.setPath("/");
        cookie.setMaxAge(expireTimeMilliSecond);

        response.addCookie(cookie);
    }
}
