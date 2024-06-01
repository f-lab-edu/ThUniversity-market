package university.market.member.utils;
public interface JwtTokenProvider {

    String generateToken(final String email);

    String generateToken(final String email, final ExpireDateSupplier expireDateSupplier);

    void validateToken(String token);

    String extractEmail(final String token);

}
