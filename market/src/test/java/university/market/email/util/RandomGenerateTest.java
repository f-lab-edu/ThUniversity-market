package university.market.email.util;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import university.market.email.utils.RandomUtil;


public class RandomGenerateTest {

    @Test
    @DisplayName("[success] 랜덤 문자 생성 성공")
    public void 랜덤_문자_생성_성공() {
        // given
        char leftLimit = '0';
        char rightLimit = 'Z';
        int limit = 6;

        // when
        final String generateRandomCode = RandomUtil.generateRandomCode(leftLimit, rightLimit, limit);

        // then
        assertThat(generateRandomCode).isNotNull();
        assertThat(generateRandomCode.length()).isEqualTo(limit);
        assertThat(generateRandomCode.chars().allMatch(i -> (i >= '0' && i <= '9') || (i >= 'A' && i <= 'Z') || (i >= 'a' && i <= 'z'))).isTrue();
    }
}