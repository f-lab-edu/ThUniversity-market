package university.market.helper.fixture;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.domain.memberstatus.MemberStatus;
import university.market.member.domain.university.UniversityType;
import university.market.verify.email.utils.random.RandomUtil;
import university.market.verify.email.utils.random.RandomUtilImpl;

public class MemberFixture {

    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    ;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private MemberFixture() {
    }

    public static MemberVO testMember(AuthType authType) {
        return MemberVO.builder()
                .name(randomUtil.generateRandomCode('0', 'z', 10))
                .email(randomUtil.generateRandomCode('0', 'z', 10) + "@" + randomUtil.generateRandomCode('A', 'z', 10)
                        + randomUtil.generateRandomCode('A', 'z', 3))
                .password(passwordEncoder.encode(randomUtil.generateRandomCode('0', 'z', 10)))
                .university(UniversityType.SEOUL.name())
                .auth(authType)
                .build();
    }

    public static MemberVO testIdMember(AuthType authType) {
        return new MemberVO(
                Long.parseLong(randomUtil.generateRandomCode('0', '9', 16)),
                randomUtil.generateRandomCode('0', 'z', 10),
                randomUtil.generateRandomCode('0', 'z', 10) + "@" + randomUtil.generateRandomCode('A', 'z', 10)
                        + randomUtil.generateRandomCode('A', 'z', 3),
                passwordEncoder.encode(randomUtil.generateRandomCode('0', 'z', 10)),
                UniversityType.SEOUL,
                authType,
                MemberStatus.OFFLINE,
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        );
    }
}
