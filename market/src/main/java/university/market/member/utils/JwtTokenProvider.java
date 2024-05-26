package university.market.member.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import university.market.member.exception.MemberException;
import university.market.member.exception.MemberExceptionType;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.secret-key}")
    private String secretKey;
    @Value("${security.jwt.expire-length}")
    private int expireTimeMilliSecond;

    public String generateToken(final String email) {
        final Date now = new Date();

        return Jwts.builder()
                .claim("email", email)
                .expiration(new Date(now.getTime() + expireTimeMilliSecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String extractEmail(final String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey).build()
                    .parseSignedClaims(token)
                    .getBody()
                    .get("email")
                    .toString();
        } catch (final ExpiredJwtException exception) {
            throw new MemberException(MemberExceptionType.EXPIRED_ACCESS_TOKEN);
        } catch (final Exception exception) {
            throw new MemberException(MemberExceptionType.INVALID_ACCESS_TOKEN);
        }
    }
}
