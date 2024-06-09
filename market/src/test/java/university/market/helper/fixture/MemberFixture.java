package university.market.helper.fixture;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import university.market.member.domain.MemberVO;
import university.market.member.domain.auth.AuthType;
import university.market.member.domain.university.UniversityType;
import university.market.verify.email.utils.random.RandomUtil;
import university.market.verify.email.utils.random.RandomUtilImpl;

public class MemberFixture {

    private static final RandomUtil randomUtil = new RandomUtilImpl();;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private MemberFixture() {
    }

    public static MemberVO testMember() {
        return MemberVO.builder()
                .name(randomUtil.generateRandomCode('0', 'z', 10))
                .email(randomUtil.generateRandomCode('0', 'z', 10) + "@" + randomUtil.generateRandomCode('A', 'z', 10)
                        + randomUtil.generateRandomCode('A', 'z', 3))
                .password(passwordEncoder.encode(randomUtil.generateRandomCode('0', 'z', 10)))
                .university(UniversityType.SEOUL.name())
                .auth(AuthType.ROLE_USER)
                .emailVerify(true)
                .build();
    }
}
