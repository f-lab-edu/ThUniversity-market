package university.market.utils.test.helper.tag;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import university.market.tag.domain.TagVO;
import university.market.utils.random.RandomUtil;
import university.market.utils.random.RandomUtilImpl;

public class TagFixture {
    private final static RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private TagFixture() {
    }

    public static TagVO testTag() {
        return TagVO.builder()
                .title(randomUtil.generateRandomCountCode('a', 'z', 1, 100))
                .build();
    }

    public static TagVO testIdTag(String title) {
        return new TagVO(
                Long.parseLong(randomUtil.generateRandomCode('0', '9', 10)),
                randomUtil.generateRandomCountCode('A', 'z', 1, 100),
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        );
    }
}
