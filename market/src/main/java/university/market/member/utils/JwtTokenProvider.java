package university.market.member.utils;
public interface JwtTokenProvider {

    String generateToken(String email);

    String generateToken(String email, ExpireDateSupplier expireDateSupplier);

    void validateToken(String token);

    String extractEmail(String token);

}
