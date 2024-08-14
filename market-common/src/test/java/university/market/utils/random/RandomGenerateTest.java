package university.market.utils.random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RandomGenerateTest {
    @Mock
    private RandomUtil randomUtil;

    @Test
    @DisplayName("[success] 랜덤 문자 생성 성공")
    public void 랜덤_문자_생성_성공() {
        // given
        char leftLimit = '0';
        char rightLimit = 'Z';
        int limit = 6;
        String verificationCode = "123abc";

        when(randomUtil.generateRandomCode(leftLimit, rightLimit, limit)).thenReturn(verificationCode);

        // when
        final String generateRandomCode = randomUtil.generateRandomCode(leftLimit, rightLimit, limit);

        // then
        verify(randomUtil, times(1)).generateRandomCode(leftLimit, rightLimit, limit);
        assertThat(verificationCode).isEqualTo(generateRandomCode);
    }
}