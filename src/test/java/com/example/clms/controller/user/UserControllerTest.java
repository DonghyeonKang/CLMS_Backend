package com.example.clms.controller.user;

import com.example.clms.WithMockCustomUser;
import com.example.clms.dto.user.ManagerAuthorityDto;
import com.example.clms.dto.user.ResetPasswordRequest;
import com.example.clms.entity.user.Roles;
import com.example.clms.service.user.ManagerAuthoritiesService;
import com.example.clms.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(UserController.class)
class UserControllerTest { // controller 테스트의 목적 : 정상 데이터를 받아서 서비스에 넘겨주는가?
    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    @MockBean
    private SecurityContext securityContext;
    @MockBean
    private UserService userService;
    @MockBean
    private ManagerAuthoritiesService managerAuthoritiesService;
    @MockBean
    private PasswordEncoder passwordEncoder;
    private static final String BASE_URL = "http://localhost:8080";

    @Test
    @WithMockUser
    @DisplayName("회원 탈퇴")
    void deleteUser() throws Exception {
        // given
        String username = "donghyeon09@naver.com";

        // when // then
        mvc.perform(delete(BASE_URL + "/user")
                        .param("username", username)
                        .with(csrf())
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("비밀번호 재설정")
    void resetPassword() throws Exception {
        // given
        String body = mapper.writeValueAsString(
                ResetPasswordRequest.builder()
                        .username("donghyeon09@naver.com")
                        .password("dsfsfwedfc123")
                        .build()
        );

        // when // then
        mvc.perform(put(BASE_URL + "/user/password")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("관리자 인증 요청 리스트 조회")
    void getManagerVerificationList() throws Exception {
        // given
        // when // then
        mvc.perform(get(BASE_URL + "/user/manager/verification")
                        .with(csrf())
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser(username = "donghyeon09@naver.com", name = "donghyeon", roles = Roles.USER)
    @DisplayName("관리자 인증 요청하기")
    void managerVerificationRequest() throws Exception {
        // given
        String body = mapper.writeValueAsString(
                ManagerAuthorityDto.builder()
                        .username("donghyeon09@naver.com")
                        .build()
        );

        // when // then
        mvc.perform(post(BASE_URL + "/user/verification/manager")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser(username = "donghyeon09@naver.com", name = "donghyeon", roles = Roles.ADMIN)
    @DisplayName("관리자 인증 요청 승인")
    void approveManagerVerification() throws Exception {
        // given
        Map<String, String> data = new HashMap<>();
        data.put("username", "donghyeon09@naver.com");
        String body = mapper.writeValueAsString(data);

        // when // then
        mvc.perform(post(BASE_URL + "/user/manager/verification/accept")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser(username = "donghyeon09@naver.com", name = "donghyeon", roles = Roles.ADMIN)
    @DisplayName("관리자 인증 요청 거절")
    void denyManagerVerification() throws Exception {
        // given
        Map<String, String> data = new HashMap<>();
        data.put("username", "donghyeon09@naver.com");
        String body = mapper.writeValueAsString(data);

        // when // then
        mvc.perform(post(BASE_URL + "/user/manager/verification/deny")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("관리자 인증 요청 삭제")
    void deleteManagerVerification() throws Exception {
        // given
        Map<String, String> data = new HashMap<>();
        data.put("username", "donghyeon09@naver.com");
        String body = mapper.writeValueAsString(data);

        // when // then
        mvc.perform(post(BASE_URL + "/user/manager/verification/delete")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andExpect(status().isOk());
    }
}