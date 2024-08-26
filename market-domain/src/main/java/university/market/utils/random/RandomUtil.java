package university.market.utils.random;

public interface RandomUtil {
    String generateRandomCode(final char leftLimit, final char rightLimit, final int limit);

    String generateRandomCountCode(final char leftLimit, final char rightLimit, final int leftCountLimit,
                                   final int rightCountLimit);

    int generateRandomIntCode(final int leftLimit, final int rightLimit);
}
