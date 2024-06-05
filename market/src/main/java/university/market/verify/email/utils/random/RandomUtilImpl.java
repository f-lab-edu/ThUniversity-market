package university.market.verify.email.utils.random;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class RandomUtilImpl implements RandomUtil{
    public String generateRandomCode(final char leftLimit, final char rightLimit, final int limit) {
        SecureRandom secureRandom;
        try {
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        StringBuilder randomString = new StringBuilder(limit);

        for (int i = 0; i < limit; i++) {
            int randomCodePoint = leftLimit + secureRandom.nextInt(rightLimit - leftLimit + 1);
            randomString.append((char) randomCodePoint);
        }

        return randomString.toString();
    }
}
