package university.market.verify.email.utils.random;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class RandomUtilImpl implements RandomUtil{

    private final SecureRandom secureRandom;

    public RandomUtilImpl() throws NoSuchAlgorithmException {
        this.secureRandom = SecureRandom.getInstanceStrong();
    }

    public String generateRandomCode(final char leftLimit, final char rightLimit, final int limit) {
        StringBuilder randomString = new StringBuilder(limit);

        for (int i = 0; i < limit; i++) {
            int randomCodePoint = leftLimit + secureRandom.nextInt(rightLimit - leftLimit + 1);
            randomString.append((char) randomCodePoint);
        }

        return randomString.toString();
    }
}
