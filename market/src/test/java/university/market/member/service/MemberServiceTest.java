package university.market.member.service;

import static org.hamcrest.Matchers.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
public class MemberServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberServiceImpl memberService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private JoinRequest joinRequest;

    @BeforeEach
    void init() {
        //given
        joinRequest = new JoinRequest("Test User","test@example.com", "password", "pusan");

        //when
        memberService.joinMember(joinRequest);
    }

    @Test
    @Transactional
    @DisplayName("[success] JoinRequest is accept")
    public void joinRequest_is_accept() {
        //when
        MemberVO member = memberService.findMemberByEmail(joinRequest.email());

        //then
        assertThat(joinRequest.email()).isEqualTo(member.getEmail());
    }

    @Test
    @Transactional
    @DisplayName("[success] Login is success")
    public void login_is_success() {
        //given
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");

        //when
        LoginResponse loginResponse = memberService.loginMember(loginRequest);
        String extractedEmail = jwtTokenProvider.extractEmail(loginResponse.token());

        //then
        assertThat(extractedEmail).isEqualTo(loginRequest.email());
    }

    @Test
    @Transactional
    @DisplayName("[success] delete myself is success")
    public void delete_myself_is_success() throws Exception {
        //given
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");
        LoginResponse loginResponse = memberService.loginMember(loginRequest);
        String extractedEmail = jwtTokenProvider.extractEmail(loginResponse.token());
        assertThat(extractedEmail).isEqualTo(loginRequest.email());

        //when
        mockMvc.perform(delete("/api/member/")
                        .header("Authorization", "Bearer " + loginResponse.token())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        MemberVO member = memberService.findMemberByEmail(extractedEmail);

        //then
        assertThat(member).isNull();
    }

    @Test
    @Transactional
    @DisplayName("[fail] not authorization guest delete myself")
    public void not_authorization_guest_delete_myself() throws Exception {
        mockMvc.perform(delete("/api/member/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("유효하지 않은 Access Token입니다.")))
                .andExpect(jsonPath("$.code", is(401103)))
                .andExpect(jsonPath("$.error", is(true)));
    }

    @Test
    @Transactional
    @DisplayName("[fail] No authentication guest delete user")
    public void no_authentication_guest_delete_user() throws Exception {
        //given
        String memberEmail = "test@example.com";
        Long memberId = memberService.findMemberByEmail(memberEmail).getId();

        //when, then
        mockMvc.perform(delete("/api/member/" + memberId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("유효하지 않은 Access Token입니다.")))
                .andExpect(jsonPath("$.code", is(401103)))
                .andExpect(jsonPath("$.error", is(true)));

    }
}