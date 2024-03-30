package com.example.clms.service.user;

import com.example.clms.common.exception.DuplicateEmailException;
import com.example.clms.dto.user.UserRegisterDto;
import com.example.clms.entity.department.Department;
import com.example.clms.entity.university.University;
import com.example.clms.entity.user.Roles;
import com.example.clms.entity.user.User;
import com.example.clms.repository.department.DepartmentRepository;
import com.example.clms.repository.university.UniversityRepository;
import com.example.clms.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class RegisterServiceImplTest {
    private RegisterService registerService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private UniversityRepository universityRepository;

    @BeforeEach
    void setUp() {
        registerService = new RegisterServiceImpl(userRepository, departmentRepository, universityRepository);
    }

    @Test
    void 학습자_회원가입() {
        // given
        UserRegisterDto userDto = UserRegisterDto.builder()
                .username("donghyeon09@naver.com")
                .password("2313123123")
                .no(2018123123)
                .universityId(1)
                .departmentId(Long.parseLong("1"))
                .name("donghyeon")
                .build();
        String role = "USER";

        // when
        registerService.register(userDto, role);

        // then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void 이메일_중복() {
        // given
        Department department = null;
        University university = null;
        UserRegisterDto userDto = UserRegisterDto.builder()
                .username("donghyeon09@naver.com")
                .password("2313123123")
                .no(2018123123)
                .universityId(1)
                .departmentId(Long.parseLong("1"))
                .name("donghyeon")
                .build();
        String role = "USER";
        User user = userDto.toUserEntity(department, university, Roles.USER);
        Optional<User> optionalUser = Optional.of(user);
        when(userRepository.findByUsername("donghyeon09@naver.com")).thenReturn(optionalUser);

        // when // then
        assertThatThrownBy(() -> registerService.register(userDto, role))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    void 교수자_회원가입() {
        // given
        UserRegisterDto userDto = UserRegisterDto.builder()
                .username("donghyeon09@naver.com")
                .password("2313123123")
                .phone("055-123-1234")
                .universityId(1)
                .departmentId(Long.parseLong("1"))
                .name("donghyeon")
                .build();
        String role = "MANAGER";

        // when
        registerService.register(userDto, role);

        // then
        verify(userRepository, times(1)).save(any(User.class));
    }

}