package university.market.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import university.market.member.domain.MemberVO;
import university.market.member.service.dto.request.JoinRequest;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberServiceImpl memberService;

    @Test
    @Transactional
    @DisplayName("[success] JoinRequest is accept")
    public void joinRequest_is_accept() {
        //given
        JoinRequest joinRequest = new JoinRequest("Test User","test@example.com", "password", "pusan");

        //when
        memberService.joinMember(joinRequest);
        MemberVO member = memberService.findMemberByEmail(joinRequest.email());

        //then
        assertThat(joinRequest.email()).isEqualTo(member.getEmail());
    }
}
