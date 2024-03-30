package com.example.clms.controller.user;

import com.example.clms.dto.department.DepartmentResponse;
import com.example.clms.dto.university.UniversityDto;
import com.example.clms.dto.user.RegisterManagerRequest;
import com.example.clms.dto.user.RegisterStudentRequest;
import com.example.clms.dto.user.VerificationRequest;
import com.example.clms.repository.department.DepartmentRepository;
import com.example.clms.repository.mail.MailRepository;
import com.example.clms.repository.university.UniversityRepository;
import com.example.clms.repository.user.UserRepository;
import com.example.clms.service.department.DepartmentService;
import com.example.clms.service.mail.EmailService;
import com.example.clms.service.university.UniversityService;
import com.example.clms.service.user.RegisterService;
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
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private RegisterService registerService;

    @MockBean
    private UniversityService universityService;

    @MockBean
    private DepartmentService departmentService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private MailRepository mailRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UniversityRepository universityRepository;

    @MockBean
    private DepartmentRepository departmentRepository;

    private static final String BASE_URL = "http://localhost:8080";

    @Test
    @WithMockUser // 스프링 시큐리티 통과
    @DisplayName("모든 학교 조회")
    void 모든학교조회() throws Exception {
        // given
        UniversityDto universityDto1 = UniversityDto.builder()
                .id(1)
                .name("경상국립대학교")
                .build();
        UniversityDto universityDto2 = UniversityDto.builder()
                .id(2)
                .name("부산대학교")
                .build();


        List<UniversityDto> universityDtoList = Arrays.asList(universityDto1, universityDto2);
        when(universityService.findAllUniversity()).thenReturn(universityDtoList);;

        // when // then
        mvc.perform(get(BASE_URL + "/register/universities")
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("경상국립대학교"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].name").value("부산대학교"))
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    @WithMockUser // 스프링 시큐리티 통과
    @DisplayName("모든 학과 조회")
    void 모든학과조회() throws Exception {
        // given
        DepartmentResponse departmentResponse = DepartmentResponse.builder()
                .id(Long.parseLong("1"))
                .name("컴퓨터과학과")
                .build();

        List<DepartmentResponse> departmentResponses = Arrays.asList(departmentResponse);
        when(departmentService.findAllDepartment(1)).thenReturn(departmentResponses);

        // when // then
        mvc.perform(get(BASE_URL + "/register/departments")
                        .param("universityId", "1")
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data[0].name").value("컴퓨터과학과"));
    }

    @Test
    @WithMockUser // 스프링 시큐리티 통과
    @DisplayName("학습자 회원가입 정상 데이터")
    void 학습자회원가입_유효한값() throws Exception {
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
    @WithMockUser
    @DisplayName("학습자 회원가입 NOT 이메일")
    void 학습자회원가입_NOT_이메일() throws Exception {
        // given
        String username = "donghyeon09gmailcom";
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
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("잘못된 요청 데이터입니다."));
    }
    @Test
    @WithMockUser
    @DisplayName("학습자 회원가입 아이디 공백")
    void 학습자회원가입_아이디_공백() throws Exception {
        // given
        String username = "";
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
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("잘못된 요청 데이터입니다."));
    }
    @Test
    @WithMockUser
    @DisplayName("학습자 회원가입 비밀번호 공백")
    void 학습자회원가입_비밀번호_공백() throws Exception {
        // given
        String username = "donghyeon09@gmail.com";
        String password = "";
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
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("잘못된 요청 데이터입니다."));
    }
    @Test
    @WithMockUser
    @DisplayName("학습자 회원가입 sql injection") //, ", #, --, = 등 특수문자와 명령어 필터링
    void 학습자회원가입_SQL_Injection() throws Exception {
        // given
        String username = "donghyeon09@gmail.com";
        String password = "admin' or '1'='1";
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
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("잘못된 요청 데이터입니다."));

    }

    @Test
    @WithMockUser
    @DisplayName("학습자 회원가입 유효하지 않은 형식 비밀번호")
    void 학습자회원가입_Invalid_Password() throws Exception {
        // given
        String username = "donghyeon09@gmail.com";
        String password = "0000000000";
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
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("잘못된 요청 데이터입니다."));
    }

    @Test
    @WithMockUser
    @DisplayName("교수자 회원가입 테스트")
    void 교수자회원가입_유효한값() throws Exception {
        // given
        String username = "donghyeon09@gmail.com";
        String password = "donghyeon09";
        String phone = "055-772-1234";
        String universityId = "1";
        String departmentId = "1";
        String name = "강동현";

        // when
        String body = mapper.writeValueAsString(
                RegisterManagerRequest.builder()
                        .username(username)
                        .password(password)
                        .universityId(universityId)
                        .departmentId(departmentId)
                        .phone(phone)
                        .name(name)
                        .build()
        );

        // then
        mvc.perform(post(BASE_URL + "/register/manager")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    @WithMockUser
    @DisplayName("교수자 회원가입 NOT 이메일")
    void 교수자회원가입_NOT_이메일() throws Exception {
        // given
        String username = "donghyeon09gmailcom";
        String password = "donghyeon09";
        String phone = "055-772-1234";
        String universityId = "1";
        String departmentId = "1";
        String name = "강동현";

        // when
        String body = mapper.writeValueAsString(
                RegisterManagerRequest.builder()
                        .username(username)
                        .password(password)
                        .universityId(universityId)
                        .departmentId(departmentId)
                        .phone(phone)
                        .name(name)
                        .build()
        );

        // then
        mvc.perform(post(BASE_URL + "/register/manager")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("잘못된 요청 데이터입니다."));

    }

    @Test
    @WithMockUser
    @DisplayName("교수자 회원가입 아이디 공백")
    void 교수자회원가입_아이디_공백() throws Exception {
        // given
        String username = "";
        String password = "donghyeon09";
        String phone = "055-772-1234";
        String universityId = "1";
        String departmentId = "1";
        String name = "강동현";

        // when
        String body = mapper.writeValueAsString(
                RegisterManagerRequest.builder()
                        .username(username)
                        .password(password)
                        .universityId(universityId)
                        .departmentId(departmentId)
                        .phone(phone)
                        .name(name)
                        .build()
        );

        // then
        mvc.perform(post(BASE_URL + "/register/manager")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("잘못된 요청 데이터입니다."));
    }
    @Test
    @WithMockUser
    @DisplayName("교수자 회원가입 비밀번호 공백")
    void 교수자회원가입_비밀번호_공백() throws Exception {
        // given
        String username = "donghyeon09@gmail.com";
        String password = "";
        String phone = "055-772-1234";
        String universityId = "1";
        String departmentId = "1";
        String name = "강동현";

        // when
        String body = mapper.writeValueAsString(
                RegisterManagerRequest.builder()
                        .username(username)
                        .password(password)
                        .universityId(universityId)
                        .departmentId(departmentId)
                        .phone(phone)
                        .name(name)
                        .build()
        );

        // then
        mvc.perform(post(BASE_URL + "/register/manager")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("잘못된 요청 데이터입니다."));
    }

    @Test
    @WithMockUser
    @DisplayName("교수자 회원가입 sql injection")
    void 교수자회원가입_SQL_Injection() throws Exception {
        // given
        String username = "donghyeon09@gmail.com";
        String password = "admin' or '1'='1";
        String phone = "055-772-1234";
        String universityId = "1";
        String departmentId = "1";
        String name = "강동현";

        // when
        String body = mapper.writeValueAsString(
                RegisterManagerRequest.builder()
                        .username(username)
                        .password(password)
                        .universityId(universityId)
                        .departmentId(departmentId)
                        .phone(phone)
                        .name(name)
                        .build()
        );

        // then
        mvc.perform(post(BASE_URL + "/register/manager")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("잘못된 요청 데이터입니다."));
    }

    @Test
    @WithMockUser
    @DisplayName("교수자 회원가입 유효하지 않은 형식 비밀번호")
    void 교수자회원가입_Invalid_Password() throws Exception {
        // given
        String username = "donghyeon09@gmail.com";
        String password = "0000000000";
        String phone = "055-772-1234";
        String universityId = "1";
        String departmentId = "1";
        String name = "강동현";

        // when
        String body = mapper.writeValueAsString(
                RegisterManagerRequest.builder()
                        .username(username)
                        .password(password)
                        .universityId(universityId)
                        .departmentId(departmentId)
                        .phone(phone)
                        .name(name)
                        .build()
        );

        // then
        mvc.perform(post(BASE_URL + "/register/manager")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("잘못된 요청 데이터입니다."));
    }


    @Test
    @WithMockUser
    @DisplayName("회원가입 인증번호 보내기")
    void 회원가입_인증번호_보내기() throws Exception {
        // given
        String email = "donghyeon09@naver.com";

        // when // then
        mvc.perform(get(BASE_URL + "/register/verification")
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    @WithMockUser
    @DisplayName("회원가입 인증번호 확인")
    void 회원가입_인증번호_확인() throws Exception {
        // given
        String body = mapper.writeValueAsString(
                VerificationRequest.builder()
                        .email("donghyeon09@naver.com")
                        .authNumber("123123")
                        .build()
        );

        // when // then
        mvc.perform(post(BASE_URL + "/register/verification")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }
}