package university.market.verify.email.utils.random;

public interface RandomUtil {
    String generateRandomCode(final char leftLimit, final char rightLimit, final int limit);
}
