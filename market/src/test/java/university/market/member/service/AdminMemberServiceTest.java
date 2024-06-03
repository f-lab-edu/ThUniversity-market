package university.market.member.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import university.market.member.domain.MemberVO;
import university.market.member.service.dto.request.JoinRequest;
import university.market.member.service.dto.request.LoginRequest;
import university.market.member.service.dto.response.LoginResponse;
import university.market.member.utils.JwtTokenProvider;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminMemberServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberServiceImpl memberService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private JoinRequest adminJoinRequest;

    private LoginRequest adminLoginRequest;

    @BeforeEach
    void init() {
        //given
        adminJoinRequest = new JoinRequest("Admin User", "admin@example.com", "adminpassword", "pusan");
        adminLoginRequest = new LoginRequest("admin@example.com", "adminpassword");

        //when
        memberService.joinAdminUser(adminJoinRequest);
    }

    @Test
    @Transactional
    @DisplayName("[success] admin delete user")
    void admin_delete_user() throws Exception {
        //given
        JoinRequest joinRequest = new JoinRequest("Test User", "test@example.com", "password", "pusan");
        memberService.joinMember(joinRequest);

        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");
        LoginResponse loginResponse = memberService.loginMember(loginRequest);

        LoginResponse adminLoginResponse = memberService.loginMember(adminLoginRequest);
        String extractedEmail = jwtTokenProvider.extractEmail(adminLoginResponse.token());
        assertThat(extractedEmail).isEqualTo(adminLoginRequest.email());

        //when
        mockMvc.perform(delete("/api/member/" + loginResponse.memberId())
                        .header("Authorization" , "Bearer " + adminLoginResponse.token())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        MemberVO member = memberService.findMemberByEmail(joinRequest.email());

        //then
        assertThat(member).isNull();

    }

    @Test
    @Transactional
    @DisplayName("[success] admin delete myself is success")
    public void delete_myself_is_success() throws Exception {
        //given
        LoginResponse adminLoginResponse = memberService.loginMember(adminLoginRequest);
        String extractedEmail = jwtTokenProvider.extractEmail(adminLoginResponse.token());
        Assertions.assertThat(extractedEmail).isEqualTo(adminLoginRequest.email());

        //when
        mockMvc.perform(delete("/api/member/")
                        .header("Authorization", "Bearer " + adminLoginResponse.token())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        MemberVO member = memberService.findMemberByEmail(extractedEmail);

        //then
        Assertions.assertThat(member).isNull();
    }
}
