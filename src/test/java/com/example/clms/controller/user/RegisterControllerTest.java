package com.example.clms.controller.user;

import com.example.clms.dto.user.RegisterStudentRequest;
import com.example.clms.repository.user.UserRepository;
import com.example.clms.service.user.RegisterService;
import com.example.clms.service.user.RegisterServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegisterController.class)
class RegisterControllerTest {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    @MockBean
    private RegisterService registerService; // 실제 데이터베이스에 연결되는 서비스

    @MockBean
    private RegisterServiceImpl registerServiceImpl; // 실제 데이터베이스에 연결되는 서비스

    @MockBean
    private PasswordEncoder passwordEncoder; // 실제 데이터베이스에 연결되는 서비스

    @MockBean
    private UserRepository userRepository;


    private static final String BASE_URL = "http://localhost:8080";
    @Test
    @WithMockUser
    @DisplayName("학습자 회원가입 테스트")
    void 학습자회원가입_유효한값_성공() throws Exception {
        // given
        String username = "donghyeon09@gmail.com";
        String password = "donghyeon09";
        String universityId = "1";
        String departmentId = "1";
        int no = 2018010836;
        String name = "강동현";

        // when
        String body = mapper.writeValueAsString(
                RegisterStudentRequest.builder()
                        .username(username)
                        .password(password)
                        .universityId(universityId)
                        .departmentId(departmentId)
                        .no(no)
                        .name(name)
                        .build()
        );

        // then
        mvc.perform(post(BASE_URL + "/register/student")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    @DisplayName("교수자 회원가입 테스트")
    void registerManager() {
        // given
        String username = "donghyeon09@gmail.com";
        String password = "donghyeon09";
        String phone = "055-772-1234";
        String universityId = "1";
        String departmentId = "1";
        String name = "강동현";

        // when

        // then

    }
}